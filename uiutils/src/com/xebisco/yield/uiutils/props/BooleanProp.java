package com.xebisco.yield.uiutils.props;

import com.xebisco.yield.uiutils.Srd;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class BooleanProp extends Prop {
    private final JCheckBox checkBox;
    public BooleanProp(String name, boolean value) {
        super(name, value);
        checkBox = new JCheckBox(Srd.LANG.getProperty(name()), value);
        checkBox.addItemListener(e -> {
            this.value = !(boolean) value;
        });
    }

    @Override
    public JComponent render() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(checkBox, BorderLayout.WEST);
        return panel;
    }

    @Override
    public Prop setValue(Serializable value) {
        checkBox.setSelected((boolean) value);
        return super.setValue(value);
    }
}
