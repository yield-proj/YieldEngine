package com.xebisco.yield.uiutils.props;

import com.xebisco.yield.uiutils.Srd;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
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
        options.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setValue((Serializable) options.getSelectedItem());
            }
        });
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


    public Object[] values() {
        return values;
    }

    public ComboProp setValues(Serializable[] values) {
        this.values = values;
        return this;
    }
}
