package com.xebisco.yield.editor.scene;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.io.File;

public class SceneExplorerCellRenderer  extends DefaultTreeCellRenderer {

    private static final Icon closed =
            (Icon) UIManager.get("InternalFrame.maximizeIcon");
    private static final Icon open =
            (Icon) UIManager.get("InternalFrame.minimizeIcon");

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                  boolean sel, boolean exp, boolean leaf, int row, boolean hasFocus) {
        setOpenIcon(getDefaultOpenIcon());
        setClosedIcon(getDefaultClosedIcon());
        setLeafIcon(getDefaultClosedIcon());
        if(((DefaultMutableTreeNode) value).getUserObject() instanceof String s) {
            setText(s);
            super.getTreeCellRendererComponent(
                    tree, s, sel, exp, true, row, hasFocus);
        } else {
            SceneObject f = (SceneObject) ((DefaultMutableTreeNode) value).getUserObject();
            super.getTreeCellRendererComponent(
                    tree, f.entityPrefab().name(), sel, exp, leaf, row, hasFocus);
        }
        return this;
    }
}
