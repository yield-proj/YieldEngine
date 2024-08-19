package com.xebisco.yieldengine.uiutils.fields;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

import static com.xebisco.yieldengine.uiutils.Lang.getString;

public class StringFieldPanel extends FieldPanel<Serializable> {

    private final JTextField textField;

    protected StringFieldPanel(String name, JTextField textField, boolean editable) {
        super(name, editable);
        this.textField = textField;

        setLayout(new BorderLayout());
        add(new JLabel(getString(name) + ": "), BorderLayout.WEST);
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        p.add(textField, gbc);
        textField.setEditable(editable);
        add(p);
    }

    public StringFieldPanel(String name, String value, boolean editable) {
        this(name, new JTextField(value), editable);
    }

    @Override
    public Serializable getValue() {
        return textField.getText();
    }

    @Override
    public void setValue(Serializable value) {
        textField.setText(String.valueOf(value));
    }

    public JTextField getTextField() {
        return textField;
    }
}
