package com.xebisco.yield.uiutils.props;

import javax.swing.*;
import java.io.Serializable;

public abstract class Prop {
    private final String name;
    protected Serializable value;

    public Prop(String name, Serializable value) {
        this.name = name;
        this.value = value;
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
