package com.xebisco.yield.editor.prop;

import com.xebisco.yield.editor.Assets;
import com.xebisco.yield.editor.FontSelector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class FontProp extends Prop {

    public FontProp(String name) {
        super(name, null);
    }

    public void updateFont(Component panel, JButton button) {
        setValue(FontSelector.open((Font) FontProp.this.getValue(), panel));
        String text = FontProp.this.getValue() == null ? "null" : ((Font) FontProp.this.getValue()).getFamily();
        if(button != null) button.setText(text);
    }

    @Override
    public JPanel panel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(new JLabel(Assets.language.getProperty(getName()) + " "), BorderLayout.WEST);
        JButton button = new JButton();
        button.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateFont(panel, button);
            }
        });
        button.setText(getValue() == null ? "null" : ((Font) getValue()).getFamily());
        panel.add(button);
        return panel;
    }
}
