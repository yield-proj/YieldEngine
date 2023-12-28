package com.xebisco.yield.editor.app.props;

import com.xebisco.yield.editor.app.Srd;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ColorProp extends Prop {
    public ColorProp(String name, Color value) {
        super(name, value);
    }

    @Override
    public JComponent render() {
        JPanel panel = new JPanel(new BorderLayout());
        String text = Srd.LANG.getProperty(name());
        if (text == null) text = name();
        panel.add(new JLabel(text + " "), BorderLayout.WEST);
        panel.add(new JButton(new AbstractAction("Color") {
            @Override
            public void actionPerformed(ActionEvent e) {
                setValue(JColorChooser.showDialog(null, "Select Color", (Color) value()));
            }
        }) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (value != null)
                    setBackground((Color) value());
            }
        });
        return panel;
    }
}
