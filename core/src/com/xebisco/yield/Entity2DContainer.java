/*
 * Copyright [2022-2023] [Xebisco]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xebisco.yield;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class Entity2DContainer {
    private final Application application;
    private List<Entity2D> entities = new ArrayList<>();

    public Entity2DContainer(Application application) {
        this.application = application;
    }

    public Entity2D instantiate(Entity2DPrefab prefab, EntityStarter entityStarter) {
        ComponentBehavior[] components = new ComponentBehavior[prefab.getComponents().length];
        for (int i = 0; i < components.length; i++) {
            ComponentCreation cc = prefab.getComponents()[i];
            try {
                components[i] = cc.getComponentClass().getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e) {
                throw new IllegalStateException("Components need to have one constructor without any arguments. '" + cc.getComponentClass() + "'");
            }
            if (cc.getComponentModifier() != null)
                cc.getComponentModifier().modify(components[i]);
        }
        Entity2D entity = new Entity2D(application, components);
        entity.setTags(prefab.getTags());
        entity.setParent(this);
        for (Entity2DPrefab p : prefab.getChildren()) {
            entity.instantiate(p);
        }
        List<Entity2D> entities = new ArrayList<>(getEntities());
        entities.add(entity);
        entities.sort(Entity2D::compareTo);
        setEntities(entities);
        if (entityStarter != null)
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
