package com.xebisco.yieldengine.uiutils.fields;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public abstract class FieldPanel<T extends Serializable> extends JPanel {
    private final String name;
    private final boolean editable;

    public FieldPanel(String name, boolean editable) {
        this.name = name;
        this.editable = editable;
        setMaximumSize(new Dimension(Integer.MAX_VALUE, getFont().getSize() + 20));
    }

    public abstract T getValue();
    public abstract void setValue(T value);

    @Override
    public String getName() {
        return name;
    }

    public boolean isEditable() {
        return editable;
    }
}
