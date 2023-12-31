package com.xebisco.yield.editor.app;

import java.awt.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EditorScene implements Serializable {
    @Serial
    private static final long serialVersionUID = -642348471217520688L;
    private String name = "Empty Scene";
    private Color backgroundColor = new Color(45, 46, 49);

    private List<EditorEntity> entities = new ArrayList<>();

    public String name() {
        return name;
    }

    public EditorScene setName(String name) {
        this.name = name;
        return this;
    }

    public Color backgroundColor() {
        return backgroundColor;
    }

    public EditorScene setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public List<EditorEntity> entities() {
        return entities;
    }

    public EditorScene setEntities(List<EditorEntity> entities) {
        this.entities = entities;
        return this;
    }
}
