package com.xebisco.yield.editor.app;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EditorComponent implements Serializable {
    @Serial
    private static final long serialVersionUID = -9027305243114159863L;
    private List<Pair<Pair<String, Class<?>>, String[]>> fields = new ArrayList<>();
    private final String className;

    private boolean canRemove = true;

    public EditorComponent(String className) {
        this.className = className;
    }

    public List<Pair<Pair<String, Class<?>>, String[]>> fields() {
        return fields;
    }

    public EditorComponent setFields(List<Pair<Pair<String, Class<?>>, String[]>> fields) {
        this.fields = fields;
        return this;
    }

    public boolean canRemove() {
        return canRemove;
    }

    public EditorComponent setCanRemove(boolean canRemove) {
        this.canRemove = canRemove;
        return this;
    }

    public String className() {
        return className;
    }
}
