package com.xebisco.yield.editor.app;

import java.io.Serializable;

public class EditorProject implements Serializable {
    private String name;

    public String name() {
        return name;
    }

    public EditorProject setName(String name) {
        this.name = name;
        return this;
    }
}
