package com.xebisco.yield.editor.app;

import java.util.ArrayList;
import java.util.List;

public class EditorEntity {
    private String entityName = "Empty Entity";
    private List<EditorComponent> components = new ArrayList<>();

    public EditorEntity() {
        clearComponents();
    }

    public void clearComponents() {
        components.clear();
        EditorComponent transform = new EditorComponent("com.xebisco.yield.Transform2D");
        components.add(transform);
    }
}
