package dev.openfga.intellijplugin.servers.ui.tree;

import dev.openfga.intellijplugin.servers.model.Server;
import dev.openfga.intellijplugin.servers.service.OpenFGAServers;
import dev.openfga.intellijplugin.util.notifications.ToolWindowNotifier;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import java.util.List;

public class RootNode extends DefaultMutableTreeNode implements TreeNode {

    private final ToolWindowNotifier toolWindowNotifier;

    public RootNode(ToolWindowNotifier toolWindowNotifier) {
        super("OpenFGA Servers", true);
        this.toolWindowNotifier = toolWindowNotifier;
    }

    @Override
    public NodeType getType() {
        return NodeType.ROOT_NODE;
    }

    @Override
    public String getText() {
        return String.valueOf(getUserObject());
    }

    private List<Server> getServers() {
        return OpenFGAServers.getInstance().getServers();
    }

    public void reloadChildren(OpenFgaTreeModel model) {
        removeAllChildren();
        for (Server server : getServers()) {
            MutableTreeNode newChild = new ServerNode(server, toolWindowNotifier);
            model.insertNodeInto(newChild, this, ((MutableTreeNode) this).getChildCount());

        }
    }
}
