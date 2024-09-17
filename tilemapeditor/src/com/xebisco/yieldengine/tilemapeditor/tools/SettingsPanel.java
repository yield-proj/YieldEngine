package com.xebisco.yieldengine.tilemapeditor.tools;

import javax.swing.*;
import java.awt.*;

public class SettingsPanel extends JPanel {
    public SettingsPanel() {
        setLayout(new BorderLayout());
        JLabel title = new JLabel("Settings", JLabel.CENTER);
        title.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        add(title, BorderLayout.NORTH);
    }
}
