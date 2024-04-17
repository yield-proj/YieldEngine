package com.xebisco.yield.uiutils.props;

import com.xebisco.yield.uiutils.Srd;

import javax.swing.*;
import java.io.Serializable;

public abstract class Prop {
    private final String name;
    protected Serializable value;

    public Prop(String name, Serializable value) {
        this.name = name;
        this.value = value;
    }

    protected JLabel westLabel(boolean prettyString) {
        String text = Srd.LANG.getProperty(name());
        if (text == null) text = name();
        if(prettyString) text = Srd.prettyString(text);
        JLabel west = new JLabel(text);
        west.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 6));
        return west;
    }

    public abstract JComponent render();

    public String name() {
        return name;
    }

    public Serializable value() {
        return value;
    }

    public Prop setValue(Serializable value) {
        this.value = value;
        return this;
    }
}
