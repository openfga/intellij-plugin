package dev.openfga.intellijplugin.servers.ui.tree;

import com.intellij.icons.AllIcons;

import javax.swing.*;
import java.awt.event.ActionEvent;

import static dev.openfga.intellijplugin.servers.util.UIUtil.copyToClipboard;

public class ServerContextualPopup extends JPopupMenu {

    private final ServerNode serverNode;
    private final OpenFgaTreeModel model;
    private final Runnable editServerAction;
    private final Runnable removeServerAction;

    public ServerContextualPopup(OpenFgaTreeModel model, ServerNode serverNode, Runnable editServerAction, Runnable removeServerAction) {
        this.model = model;
        this.serverNode = serverNode;
        this.editServerAction = editServerAction;
        this.removeServerAction = removeServerAction;

        add(editServerMenuItem());
        add(removeServerMenuItem());
        addSeparator();
        add(refreshMenuItem());
        addSeparator();
        add(copyNameMenuItem());
        add(copyUrlMenuItem());
    }

    private JMenuItem editServerMenuItem() {
        var menuItem = new JMenuItem("edit", AllIcons.Actions.Edit);
        menuItem.addActionListener(e -> editServerAction.run());
        return menuItem;
    }

    private JMenuItem removeServerMenuItem() {
        var menuItem = new JMenuItem("delete", AllIcons.General.Remove);
        menuItem.addActionListener(e -> removeServerAction.run());
        return menuItem;
    }

    private JMenuItem refreshMenuItem() {
        var menuItem = new JMenuItem("refresh", AllIcons.Actions.Refresh);
        menuItem.addActionListener(this::refresh);
        return menuItem;
    }

    private void refresh(ActionEvent event) {
        serverNode.forceNextReload();
        serverNode.loadChildren(model);
    }

    private JMenuItem copyNameMenuItem() {
        var menuItem = new JMenuItem("copy name");
        menuItem.addActionListener(e -> copyToClipboard(serverNode.getServer().getName()));
        return menuItem;
    }

    private JMenuItem copyUrlMenuItem() {
        var menuItem = new JMenuItem("copy url");
        menuItem.addActionListener(e -> copyToClipboard(serverNode.getServer().getUrl()));
        return menuItem;
    }
}
