package com.github.le_yams.openfga4intellij.servers.ui;

import com.github.le_yams.openfga4intellij.servers.model.Server;

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
