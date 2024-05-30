package com.xebisco.yieldengine.uiutils.props;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ColorProp extends Prop {
    private final boolean prettyString;
    public ColorProp(String name, Color value, boolean prettyString) {
        super(name, value);
        this.prettyString = prettyString;
    }

    @Override
    public JComponent render() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(westLabel(prettyString), BorderLayout.WEST);
        JButton button;
        panel.add(new JButton(new AbstractAction("Color") {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color v = JColorChooser.showDialog(null, "Select Color", (Color) value());
                if (v != null)
                    setValue(v);
            }
        }) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (value != null) {
                    setBackground((Color) value());
                    setForeground(new Color(255 - ((Color) value()).getRed(), 255 - ((Color) value()).getGreen(), 255 - ((Color) value()).getBlue()));
                }
            }
        });
        return panel;
    }
}
