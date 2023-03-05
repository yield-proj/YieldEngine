package com.xebisco.yield;

public class Entity2DPrefab {
    private final ComponentBehavior[] components;
    private final Entity2D[] children;

    public Entity2DPrefab(Entity2D[] children, ComponentBehavior... components) {
        this.components = components;
        this.children = children;
    }

    public Entity2DPrefab(ComponentBehavior... components) {
        this(new Entity2D[0], components);
    }

    public ComponentBehavior[] getComponents() {
        return components;
    }

    public Entity2D[] getChildren() {
        return children;
    }
}
