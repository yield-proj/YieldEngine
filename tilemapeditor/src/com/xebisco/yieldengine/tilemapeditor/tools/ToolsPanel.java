package com.xebisco.yieldengine.tilemapeditor.tools;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ToolsPanel extends JPanel {
    public ToolsPanel() {
        setLayout(new BorderLayout());
        JLabel title = new JLabel("Tools", JLabel.CENTER);
        title.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        add(title, BorderLayout.NORTH);

        JToolBar toolBar = new JToolBar(SwingConstants.VERTICAL);
        toolBar.setFloatable(false);
        toolBar.setRollover(true);

        toolBar.add(new AbstractAction("Paint") {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        toolBar.add(new AbstractAction("Remove") {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        add(toolBar, BorderLayout.CENTER);
    }
}
