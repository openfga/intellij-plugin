package dev.openfga.intellijplugin.servers.ui;

import dev.openfga.intellijplugin.servers.model.Server;
import dev.openfga.intellijplugin.servers.service.OpenFGAServers;
import com.intellij.openapi.application.ApplicationManager;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.List;

class RootTreeNode extends DefaultMutableTreeNode {

    public RootTreeNode() {
        super("OpenFGA Servers");
        reload();
    }

    private List<Server> getServers() {
        return ApplicationManager.getApplication().getService(OpenFGAServers.class).getServers();
    }

    void reload() {
        removeAllChildren();
        for (Server server : getServers()) {
            this.add(new ServerTreeNode(server));
        }
    }
}
