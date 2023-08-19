package com.xebisco.yield.editor.prop;

import com.xebisco.yield.editor.Assets;

import javax.swing.*;
import java.awt.*;

public class ButtonProp extends Prop {
    public ButtonProp(String name, AbstractAction action) {
        super(name, action);
    }

    @Override
    public JPanel panel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(new JLabel(Assets.language.getProperty(getName()) + " "), BorderLayout.WEST);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(new JButton((AbstractAction) getValue()));
        panel.add(buttonPanel, BorderLayout.EAST);
        return panel;
    }
}
