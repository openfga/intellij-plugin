package dev.openfga.intellijplugin.servers.ui.tree;

import dev.openfga.intellijplugin.OpenFGAIcons;
import dev.openfga.sdk.api.model.Store;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

import static dev.openfga.intellijplugin.servers.ui.tree.NodeType.STORE_NODE;

public class StoreNode extends DefaultMutableTreeNode implements TreeNode {

    private final Store store;

    public StoreNode(Store store) {
        super(store, false);
        this.store = store;
    }

    public Store getStore() {
        return store;
    }

    @Override
    public NodeType getType() {
        return STORE_NODE;
    }

    @Override
    public String getText() {
        return store.getName();
    }

    @Override
    public String getToolTipText() {
        return store.getId();
    }

    @Override
    public Icon getIcon() {
        return OpenFGAIcons.STORE_NODE;
    }
}
