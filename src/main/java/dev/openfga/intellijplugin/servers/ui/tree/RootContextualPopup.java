package dev.openfga.intellijplugin.servers.ui.tree;

import com.intellij.icons.AllIcons;

import javax.swing.*;

public class RootContextualPopup extends JPopupMenu {

    private final Runnable addServerAction;

    public RootContextualPopup(Runnable addServerAction) {
        this.addServerAction = addServerAction;

        add(addServerMenuItem());
    }

    private JMenuItem addServerMenuItem() {
        var menuItem = new JMenuItem("Add server", AllIcons.General.Add);
        menuItem.addActionListener(e -> addServerAction.run());
        return menuItem;
    }
}
