package com.xebisco.yield.uiutils.props;

import com.xebisco.yield.uiutils.Srd;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.Serializable;

public class TextFieldProp extends Prop {
    private final JTextField field;
    private final boolean prettyString;

    public TextFieldProp(String name, String value, boolean prettyString) {
        super(name, value);
        this.prettyString = prettyString;
        field = new JTextField((String) value());
        field.setMaximumSize(field.getPreferredSize());
        field.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                TextFieldProp.this.value = field.getText();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                TextFieldProp.this.value = field.getText();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                TextFieldProp.this.value = field.getText();
            }
        });
    }

    @Override
    public JComponent render() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(westLabel(prettyString), BorderLayout.WEST);
        JPanel p = new JPanel();
        p.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        p.add(field, gbc);
        panel.add(p);
        return panel;
    }

    public JTextField field() {
        return field;
    }

    @Override
    public Prop setValue(Serializable value) {
        field.setText((String) value);
        field.paintImmediately(0, 0, field.getWidth(), field.getHeight());
        return super.setValue(value);
    }
}
