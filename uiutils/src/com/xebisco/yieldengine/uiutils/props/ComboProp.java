package com.xebisco.yieldengine.uiutils.props;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class ComboProp extends Prop {
    private Serializable[] values;
    private final JComboBox<Serializable> options;

    private final boolean prettyLabel;

    public ComboProp(String name, Serializable value, Serializable[] values, boolean prettyLabel) {
        super(name, value);
        this.values = values;
        options = new JComboBox<>(values);
        this.prettyLabel = prettyLabel;
        options.addItemListener(e -> setValue((Serializable) options.getSelectedItem()));
        options.setSelectedItem(value);
    }

    @Override
    public JComponent render() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(westLabel(prettyLabel), BorderLayout.WEST);
        JPanel comboBoxPanel = new JPanel();
        comboBoxPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        comboBoxPanel.add(options, gbc);
        panel.add(comboBoxPanel);
        return panel;
    }

    @Override
    public Prop setValue(Serializable value) {
        if (options.getSelectedItem() != value)
            options.setSelectedItem(value);
        return super.setValue(value);
    }

    @Override
    public Serializable value() {
        return super.value();
    }

    public Object[] values() {
        return values;
    }

    public ComboProp setValues(Serializable[] values) {
        this.values = values;
        return this;
    }
}
