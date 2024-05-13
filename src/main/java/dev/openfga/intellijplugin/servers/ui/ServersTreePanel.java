package dev.openfga.intellijplugin.servers.ui;

import dev.openfga.intellijplugin.servers.model.Server;
import dev.openfga.intellijplugin.servers.service.OpenFGAServers;
import dev.openfga.intellijplugin.servers.ui.tree.*;
import dev.openfga.intellijplugin.util.notifications.ToolWindowNotifier;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.AnActionButton;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.treeStructure.Tree;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;

import static dev.openfga.intellijplugin.servers.ui.tree.NodeType.SERVER_NODE;

public class ServersTreePanel extends JPanel {

    private final ToolWindow toolWindow;
    private final ToolWindowNotifier toolWindowNotifier;
    private final OpenFGAServers servers;
    private final Tree tree;
    private final RootNode root;
    private final OpenFgaTreeModel treeModel;


    public ServersTreePanel(ToolWindow toolWindow, OpenFGAServers servers) {
        super(new BorderLayout());
        this.toolWindow = toolWindow;
        this.toolWindowNotifier = new ToolWindowNotifier(toolWindow);
        this.servers = servers;
        treeModel = new OpenFgaTreeModel(toolWindowNotifier);
        tree = new Tree(treeModel);
        root = treeModel.getRootNode();
        tree.setExpandableItemsEnabled(true);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setCellRenderer(new CellRenderer());
        tree.addTreeWillExpandListener(treeModel);

        var toolbarDecorator = ToolbarDecorator.createDecorator(tree);

        toolbarDecorator.setAddActionUpdater(updater -> {
            var selectedNode = getSelectedNode();
            return selectedNode.isEmpty() || selectedNode.get().is(NodeType.ROOT_NODE);

        });
        toolbarDecorator.setAddAction(this::addAction);

        toolbarDecorator.setEditActionUpdater(updater -> isSelectedNode(SERVER_NODE));
        toolbarDecorator.setEditAction(this::editAction);

        toolbarDecorator.setRemoveActionUpdater(updater -> isSelectedNode(SERVER_NODE));
        toolbarDecorator.setRemoveAction(this::removeAction);

        toolbarDecorator.setMoveDownActionUpdater(updater -> isSelectedNode(SERVER_NODE));
        toolbarDecorator.setMoveDownAction(this::moveDownAction);
        toolbarDecorator.setMoveUpActionUpdater(updater -> isSelectedNode(SERVER_NODE));
        toolbarDecorator.setMoveUpAction(this::moveUpAction);

        add(toolbarDecorator.createPanel(), BorderLayout.CENTER);

        tree.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                if (!SwingUtilities.isRightMouseButton(event)) {
                    return;
                }
                int selectedRow = tree.getLeadSelectionRow();
                TreePath selectedPath = tree.getLeadSelectionPath();

                TreeNode selectedNode = null;
                if (selectedPath != null) {
                    selectedNode = (TreeNode) selectedPath.getLastPathComponent();
                }
                var nodeType = selectedNode != null
                        ? selectedNode.getType()
                        : NodeType.ROOT_NODE;

                JPopupMenu popupMenu = null;
                switch (nodeType) {
                    case ROOT_NODE -> popupMenu = new RootContextualPopup(() -> addAction(null));
                    case SERVER_NODE -> {
                        var serverNode = (ServerNode) selectedNode;
                        tree.setSelectionRow(selectedRow);
                        popupMenu = new ServerContextualPopup(
                                treeModel,
                                serverNode,
                                () -> editAction(null),
                                () -> removeAction(null)
                        );
                    }
                    case STORE_NODE -> {
                        var storeNode = (StoreNode) selectedNode;
                        tree.setSelectionRow(selectedRow);
                        popupMenu = new StoreContextualPopup(treeModel, storeNode);
                    }

                }
                if (popupMenu != null) {
                    popupMenu.show(event.getComponent(), event.getX(), event.getY());
                }
            }
        });
    }

    private void runInBackground(String name, Runnable runnable) {
        ProgressManager.getInstance().run(
                new Task.Backgroundable(toolWindow.getProject(), name, false) {
                    @Override
                    public void run(@NotNull ProgressIndicator indicator) {
                        runnable.run();
                    }
                }
        );

    }

    private void selectTreePath(TreePath treePath) {
        SwingUtilities.invokeLater(() -> {
            tree.setSelectionPath(treePath);
            tree.scrollPathToVisible(treePath);
            tree.updateUI();
        });
    }

    private void addAction(AnActionButton anActionButton) {
        ServerDialog.showAddServerDialog(toolWindow)
                .ifPresent(server -> runInBackground("Adding OpenFGA Server", () -> {
                    servers.add(server);
                    var newNode = new ServerNode(server, toolWindowNotifier);
                    root.add(newNode);
                    var treePath = new TreePath(newNode.getPath());
                    selectTreePath(treePath);
                }));
    }

    private void editAction(AnActionButton anActionButton) {
        getSelectedServerNode()
                .flatMap(node -> ServerDialog.showEditServerDialog(toolWindow, node.getServer()))
                .ifPresent(server -> runInBackground("Updating OpenFGA Server", () -> {
                    servers.update(server);
                    SwingUtilities.invokeLater(tree::updateUI);
                }));
    }

    private void removeAction(AnActionButton anActionButton) {
        var selectedNode = getSelectedServerNode();
        selectedNode.ifPresent(node -> {
            var server = node.getServer();
            var title = "Confirm OpenFGA Server Deletion";
            var message = "Deleting the OpenFGA server '" + server.getName() + "' is not reversible. Do you confirm the server deletion?";
            if (Messages.showYesNoDialog(this, message, title, null) != Messages.YES) {
                return;
            }
            runInBackground("Deleting OpenFGA Server", () -> {
                servers.remove(server);
                SwingUtilities.invokeLater(() -> {
                    root.reloadChildren(treeModel);
                    tree.updateUI();
                });
            });
        });
    }

    private void moveDownAction(AnActionButton anActionButton) {
        getSelectedServerNode().ifPresent(node -> moveServerNode(node, servers::moveDown));
    }

    private void moveUpAction(AnActionButton anActionButton) {
        getSelectedServerNode().ifPresent(node -> moveServerNode(node, servers::moveUp));
    }

    private void moveServerNode(ServerNode node, Function<Server, Integer> moveFunction) {
        runInBackground("Moving OpenFGA Server", () -> {
            var indexToSelect = moveFunction.apply(node.getServer());
            SwingUtilities.invokeLater(() -> {
                root.reloadChildren(treeModel);
                var serverNode = (ServerNode) root.getChildAt(indexToSelect);
                var pathToSelect = new TreePath(serverNode.getPath());
                tree.setSelectionPath(pathToSelect);
                tree.updateUI();
            });
        });

    }

    private Optional<TreeNode> getSelectedNode() {
        return getSelectedNode(null);
    }

    private Optional<TreeNode> getSelectedNode(NodeType nodeType) {
        return Arrays
                .stream(tree.getSelectedNodes(TreeNode.class, node -> nodeType == null || node.is(nodeType)))
                .findFirst();
    }

    private Optional<ServerNode> getSelectedServerNode() {
        return getSelectedNode(SERVER_NODE)
                .map(ServerNode.class::cast);
    }

    private boolean isSelectedNode(NodeType nodeType) {
        return getSelectedNode(nodeType).isPresent();
    }
}
