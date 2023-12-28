package com.xebisco.yield.editor.app.props;

import com.xebisco.yield.editor.app.Srd;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.Serializable;

public class TextFieldProp extends Prop {
    private final JTextField field;

    public TextFieldProp(String name, String value) {
        super(name, value);
        field = new JTextField((String) value());
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
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        String text = Srd.LANG.getProperty(name());
        if (text == null) text = name();
        JLabel west = new JLabel(text);
        west.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 6));
        panel.add(west, BorderLayout.WEST);
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
        SwingUtilities.invokeLater(() -> {
            field.setText((String) value);
            field.repaint();
        });

        return super.setValue(value);
    }
}
