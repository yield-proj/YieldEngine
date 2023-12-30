package com.xebisco.yield.editor.app;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EditorEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = -5169409393577048649L;
    private String entityName = "Empty Entity";
    private boolean enabled = true;
    private List<EditorComponent> components = new ArrayList<>();

    public EditorEntity() {
        clearComponents();
    }

    public void clearComponents() {
        components.clear();
        EditorComponent transform = new EditorComponent("com.xebisco.yield.Transform2D");
        components.add(transform);
    }

    public String entityName() {
        return entityName;
    }

    public EditorEntity setEntityName(String entityName) {
        this.entityName = entityName;
        return this;
    }

    public List<EditorComponent> components() {
        return components;
    }

    public EditorEntity setComponents(List<EditorComponent> components) {
        this.components = components;
        return this;
    }

    public boolean enabled() {
        return enabled;
    }

    public EditorEntity setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }
}
