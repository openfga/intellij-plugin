package dev.openfga.intellijplugin.servers.ui;

import dev.openfga.intellijplugin.servers.model.Server;
import javax.swing.tree.DefaultMutableTreeNode;

class ServerTreeNode extends DefaultMutableTreeNode {
    private final Server server;

    public ServerTreeNode(Server server) {
        super(server, false);
        this.server = server;
    }

    public Server getServer() {
        return server;
    }
}
