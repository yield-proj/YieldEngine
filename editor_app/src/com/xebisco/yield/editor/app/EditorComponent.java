package com.xebisco.yield.editor.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditorComponent {
    private List<Pair<String, Class<?>>> fields = new ArrayList<>();
    private final String className;

    private boolean canRemove = true;

    public EditorComponent(String className) {
        this.className = className;
    }

    public List<Pair<String, Class<?>>> fields() {
        return fields;
    }

    public EditorComponent setFields(List<Pair<String, Class<?>>> fields) {
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
