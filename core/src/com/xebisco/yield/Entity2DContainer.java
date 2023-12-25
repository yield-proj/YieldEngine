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

import com.xebisco.yield.rendering.Renderer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * This is a class that represents a container for 2D entities, allowing for instantiation and removal of entities.
 */
public class Entity2DContainer extends AbstractBehavior implements Renderable {
    private volatile List<Entity2D> entities = new ArrayList<>();
    private final Set<Entity2D> toAddEntities = new HashSet<>(), toRemoveEntities = new HashSet<>();
    private final Application application;

    public Entity2DContainer(Application application) {
        this.application = application;
    }

    /**
     * This function instantiates a 2D entity using a prefab and its components, sets its tags and parent, adds it to a
     * list of entities, and starts it.
     *
     * @param prefab        The prefab parameter is an instance of the Entity2DPrefab class, which contains information about the
     *                      components and children of the entity to be instantiated.
     * @param entityStarter entityStarter is an optional parameter of type EntityStarter, which is an interface that
     *                      defines a method called "start" that takes an Entity2D object as a parameter. This parameter allows for additional
     *                      functionality to be executed on the instantiated entity after it has been created.
     * @return The method is returning an instance of the Entity2D class.
     */
    public Entity2D instantiate(Entity2DPrefab prefab, EntityStarter entityStarter) {
        ComponentBehavior[] components = new ComponentBehavior[prefab.components().length];
        for (int i = 0; i < components.length; i++) {
            ComponentCreation cc = prefab.components()[i];
            try {
                components[i] = cc.getComponentClass().getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e) {
                throw new IllegalStateException("Components need to have one constructor without any arguments. '" + cc.getComponentClass() + "'");
            }
            if (cc.getComponentModifier() != null) {
                try {
                    cc.getComponentModifier().modify(components[i]);
                } catch (OnlyModifiableBeforeCreation e) {
                    throw new RuntimeException(e);
                }
            }
        }
        Entity2D entity = new Entity2D(application, components, this, prefab.tags().clone());
        for (Entity2DPrefab p : prefab.children()) {
            entity.instantiate(p);
        }
        toAddEntities.add(entity);
        if (entityStarter != null)
            entityStarter.start(entity);
        return entity;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void render(Renderer renderer) {
        entities.sort(Comparator.comparing(Entity2D::renderIndex).reversed());

        entities.forEach(e -> e.render(renderer));
    }

    @Override
    public void onUpdate(ContextTime time) {
        if (!toAddEntities.isEmpty()) {
            entities.addAll(toAddEntities);
            toAddEntities.clear();
        }

        if (!toRemoveEntities.isEmpty()) {
            entities.removeAll(toRemoveEntities);
            toRemoveEntities.clear();
        }

        entities.forEach(e -> e.tick(time));
    }

    /**
     * This function instantiates a 2D entity using a prefab and returns it.
     *
     * @param prefab The prefab parameter is an instance of the Entity2DPrefab class, which contains information about the
     *               components and children of the entity to be instantiated.
     * @return The method is returning an instance of the Entity2D class.
     */
    public Entity2D instantiate(Entity2DPrefab prefab) {
        return instantiate(prefab, null);
    }

    public boolean remove(Entity2D entity) throws IOException {
        entity.close();
        return toRemoveEntities.add(entity);
    }

    public Application application() {
        return application;
    }

    public List<Entity2D> entities() {
        return entities;
    }

    public Entity2DContainer setEntities(List<Entity2D> entities) {
        this.entities = entities;
        return this;
    }

    public Set<Entity2D> toAddEntities() {
        return toAddEntities;
    }

    public Set<Entity2D> toRemoveEntities() {
        return toRemoveEntities;
    }

    @Override
    public void close() throws IOException {

    }
}
