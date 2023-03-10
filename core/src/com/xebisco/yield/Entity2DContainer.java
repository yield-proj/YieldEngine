package com.xebisco.yield;

import java.util.ArrayList;
import java.util.List;

public class Entity2DContainer {
    private List<Entity2D> entities = new ArrayList<>();
    private final Application application;

    public Entity2DContainer(Application application) {
        this.application = application;
    }

    public Entity2D instantiate(Entity2DPrefab prefab, EntityStarter entityStarter) {
        Entity2D entity = new Entity2D(application, prefab.getChildren(), prefab.getComponents());
        List<Entity2D> entities = new ArrayList<>(getEntities());
        entities.add(entity);
        entities.sort(Entity2D::compareTo);
        setEntities(entities);
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

    public List<Entity2D> getEntities() {
        return entities;
    }

    public void setEntities(List<Entity2D> entities) {
        this.entities = entities;
    }

    public Application getApplication() {
        return application;
    }
}
