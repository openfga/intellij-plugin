package dev.openfga.intellijplugin.servers.ui.tree;

import dev.openfga.intellijplugin.util.notifications.ToolWindowNotifier;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.ui.treeStructure.Tree;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;


public class OpenFgaTreeModel extends DefaultTreeModel implements TreeWillExpandListener {

    private static final Logger logger = Logger.getInstance(OpenFgaTreeModel.class);

    private final RootNode rootNode;

    public OpenFgaTreeModel(ToolWindowNotifier toolWindowNotifier) {
        this(new RootNode(toolWindowNotifier));
    }

    private OpenFgaTreeModel(RootNode rootNode) {
        super(rootNode);
        this.rootNode = rootNode;
        rootNode.reloadChildren(this);
    }

    public RootNode getRootNode() {
        return rootNode;
    }

    @Override
    public void treeWillExpand(TreeExpansionEvent event) {
        var node = (TreeNode) event.getPath().getLastPathComponent();
        if (node.getType() == NodeType.SERVER_NODE) {
            var serverNode = (ServerNode) node;
            serverNode.loadChildren(this).thenAccept(unused -> {
                var tree = (Tree) event.getSource();
                SwingUtilities.invokeLater(() -> tree.expandPath(new TreePath(serverNode.getPath())));
            });
        }
    }

    @Override
    public void treeWillCollapse(TreeExpansionEvent event) {
    }
}
