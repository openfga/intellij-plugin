package dev.openfga.intellijplugin.servers.ui;

import com.intellij.openapi.application.ApplicationManager;
import dev.openfga.intellijplugin.servers.model.Server;
import dev.openfga.intellijplugin.servers.service.OpenFGAServers;
import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;

class RootTreeNode extends DefaultMutableTreeNode {

    public RootTreeNode() {
        super("OpenFGA Servers");
        reload();
    }

    private List<Server> getServers() {
        return ApplicationManager.getApplication()
                .getService(OpenFGAServers.class)
                .getServers();
    }

    void reload() {
        removeAllChildren();
        for (Server server : getServers()) {
            this.add(new ServerTreeNode(server));
        }
    }
}
