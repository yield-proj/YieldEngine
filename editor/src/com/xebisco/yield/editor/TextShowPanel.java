package com.xebisco.yield.editor;

import javax.swing.*;
import java.awt.*;

public record TextShowPanel(String text) {
    public JPanel panel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(new JLabel(Assets.language.getProperty(text) + ' '), BorderLayout.WEST);
        JPanel p1 = new JPanel();
        p1.setLayout(new BorderLayout());
        p1.add(new JPanel(), BorderLayout.NORTH);
        p1.add(new JPanel(), BorderLayout.SOUTH);
        p1.add(new JSeparator(JSeparator.HORIZONTAL));
        panel.add(p1);
        return panel;
    }
}
