package com.xebisco.yield.editor;

import javax.swing.*;
import javax.swing.plaf.basic.BasicToolBarUI;
import java.awt.*;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;

public class YieldToolBar extends JToolBar {
    public YieldToolBar(String name) {
        super(name);
        final HierarchyListener hierarchyListener = new HierarchyListener() {

            @Override
            public void hierarchyChanged(HierarchyEvent e) {

                if ((e.getChangeFlags() & HierarchyEvent.PARENT_CHANGED) == 0) return;
                JToolBar bar = (JToolBar) e.getComponent();
                if (!((BasicToolBarUI) bar.getUI()).isFloating()) return;

                Window topLevel = SwingUtilities.windowForComponent(bar);
                if(topLevel == null) return;

                topLevel.dispose();
                ((JDialog) topLevel).setUndecorated(true);
                topLevel.setVisible(true);

            }
        };
        addHierarchyListener(hierarchyListener);
    }
}
