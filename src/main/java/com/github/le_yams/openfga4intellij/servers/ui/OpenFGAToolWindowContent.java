package com.github.le_yams.openfga4intellij.servers.ui;

import com.github.le_yams.openfga4intellij.servers.service.OpenFGAServers;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.treeStructure.Tree;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.util.Arrays;
import java.util.Optional;

class OpenFGAToolWindowContent {
    private final ToolWindow toolWindow;
    private Tree tree;

    OpenFGAToolWindowContent(ToolWindow toolWindow) {
        this.toolWindow = toolWindow;
    }

    public JComponent getContentPanel() {
        var mainPanel = new JPanel(new BorderLayout());

        var root = new RootTreeNode();
        tree = new Tree(root);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        var toolbarDecorator = ToolbarDecorator.createDecorator(tree);


        toolbarDecorator.setAddAction(anActionButton -> {
            var server = ServerDialog.showAddServerDialog(toolWindow);
            if (server == null) {
                return;
            }

            servers().add(server);
            var newNode = new ServerTreeNode(server);
            root.add(newNode);
            var treePath = new TreePath(newNode.getPath());
            tree.setSelectionPath(treePath);
            tree.scrollPathToVisible(treePath);
            tree.updateUI();

        });
        toolbarDecorator.setEditActionUpdater(updater -> getSelectedNode().isPresent());
        toolbarDecorator.setEditAction(anActionButton -> getSelectedNode()
                .ifPresent(node -> {
                    ServerDialog.showEditServerDialog(toolWindow, node.getServer());
                    tree.updateUI();
                }));
        toolbarDecorator.setRemoveActionUpdater(updater -> getSelectedNode().isPresent());
        toolbarDecorator.setRemoveAction(anActionButton -> {
            var selectedNode = getSelectedNode();
            selectedNode.ifPresent(node -> {
                var server = node.getServer();

                var title = "Confirm OpenFGA Server Deletion";
                var message = "Deleting the OpenFGA server '" + server.getName() + "' is not reversible. Do you confirm the server deletion?";
                Messages.showYesNoDialog(mainPanel, message, title, null);

                servers().remove(server);
                var childIndex = root.getIndex(node);
                var pathToSelect = new TreePath(root.getPath());
                if (root.getChildCount() != 0) {
                    var indexToSelect = childIndex >= root.getChildCount() ? root.getChildCount() - 1 : childIndex;
                    var serverNode = (ServerTreeNode) root.getChildAt(indexToSelect);
                    pathToSelect = new TreePath(serverNode.getPath());
                }
                tree.setSelectionPath(pathToSelect);
                tree.updateUI();
            });
        });

        toolbarDecorator.setMoveDownAction(anActionButton -> {
            getSelectedNode().ifPresent(node -> {
                var indexToSelect = servers().moveDown(node.getServer());
                root.reload();
                var serverNode = (ServerTreeNode) root.getChildAt(indexToSelect);
                var pathToSelect = new TreePath(serverNode.getPath());
                tree.setSelectionPath(pathToSelect);
                tree.updateUI();
            });
        });

        toolbarDecorator.setMoveUpAction(anActionButton -> {
            getSelectedNode().ifPresent(node -> {
                var indexToSelect = servers().moveUp(node.getServer());
                root.reload();
                var serverNode = (ServerTreeNode) root.getChildAt(indexToSelect);
                var pathToSelect = new TreePath(serverNode.getPath());
                tree.setSelectionPath(pathToSelect);
                tree.updateUI();
            });
        });

        mainPanel.add(toolbarDecorator.createPanel(), BorderLayout.CENTER);
        return mainPanel;
    }

    private static OpenFGAServers servers() {
        return OpenFGAServers.getInstance();
    }

    @NotNull
    private Optional<ServerTreeNode> getSelectedNode() {
        return Arrays.stream(getSelectedNodes()).findFirst();
    }

    @NotNull
    private ServerTreeNode[] getSelectedNodes() {
        return tree.getSelectedNodes(ServerTreeNode.class, null);
    }


}
