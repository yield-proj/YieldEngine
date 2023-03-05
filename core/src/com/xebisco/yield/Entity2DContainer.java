package com.xebisco.yield;

import java.util.TreeSet;

public class Entity2DContainer {
    private TreeSet<Entity2D> entities = new TreeSet<>();
    private final Application application;

    public Entity2DContainer(Application application) {
        this.application = application;
    }

    public Entity2D instantiate(Entity2DPrefab prefab, EntityStarter entityStarter) {
        Entity2D entity = new Entity2D(application, prefab.getChildren(), prefab.getComponents());
        getEntities().add(entity);
        if(entityStarter != null)
            entityStarter.start(entity);
        return entity;
    }

    public Entity2D instantiate(Entity2DPrefab prefab) {
        return instantiate(prefab, null);
    }

    public boolean remove(Entity2D entity) {
        entity.dispose();
        return getEntities().remove(entity);
    }

    public TreeSet<Entity2D> getEntities() {
        return entities;
    }

    public void setEntities(TreeSet<Entity2D> entities) {
        this.entities = entities;
    }

    public Application getApplication() {
        return application;
    }
}
