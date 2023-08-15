package com.xebisco.yield.editor.prop;

import com.xebisco.yield.editor.Assets;

import javax.swing.*;
import java.awt.*;

public class TextShowProp extends Prop {

    private final String text;

    public TextShowProp(String text) {
        super(null, null);
        this.text = text;
    }

    @Override
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
