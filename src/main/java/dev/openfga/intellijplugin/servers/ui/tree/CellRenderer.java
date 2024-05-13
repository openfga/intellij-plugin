package dev.openfga.intellijplugin.servers.ui.tree;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

public class CellRenderer extends DefaultTreeCellRenderer {

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        var treeNode = (TreeNode) value;
        setText(treeNode.getText());
        setIcon(treeNode.getIcon());
        setToolTipText(treeNode.getToolTipText());
        return this;
    }
}
