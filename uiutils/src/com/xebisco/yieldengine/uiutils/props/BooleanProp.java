package com.xebisco.yieldengine.uiutils.props;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class BooleanProp extends Prop {
    private final JCheckBox checkBox;
    private final boolean prettyString;

    public BooleanProp(String name, boolean value, boolean prettyString) {
        super(name, value);
        checkBox = new JCheckBox("", value);
        this.prettyString = prettyString;
        checkBox.addItemListener(e -> {
            this.value = !(boolean) value;
        });
    }

    @Override
    public JComponent render() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(westLabel(prettyString), BorderLayout.WEST);
        panel.add(checkBox);
        return panel;
    }

    @Override
    public Prop setValue(Serializable value) {
        checkBox.setSelected((boolean) value);
        return super.setValue(value);
    }
}
