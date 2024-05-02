/*
 * Copyright [2022-2024] [Xebisco]
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

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a 2D entity within a game or application.
 * This class extends {@link Entity2DContainer} and provides functionality for managing components, updating, rendering,
 * and transforming the entity within its hierarchy.
 */
public final class Entity2D extends Entity2DContainer {
    private final Transform2D transform = new Transform2D();
    private final ComponentBehavior[] components;
    private final Entity2DContainer parent;
    private int renderIndex;

    private final String[] tags;
    private boolean visible = true;

    /**
     * Constructs a new {@link Entity2D} object with the specified components, parent, and tags.
     *
     * @param application The application that this entity belongs to.
     * @param components   The array of components that this entity will manage.
     * @param parent        The parent entity of this entity, or null if it has no parent.
     * @param tags          The array of tags that describe this entity.
     */
    Entity2D(Application application, ComponentBehavior[] components, Entity2DContainer parent, String[] tags) {
        super(application);
        this.parent = parent;
        this.tags = tags;
        this.components = components;
    }

    @Override
    public void onUpdate(ContextTime time) {
        if(frames() == 0) {
            for (ComponentBehavior component : components) {
                if (component != null) {
                    component.setEntity(this);
                    component.onCreate();
                }
            }
        }
        for (ComponentBehavior component : components) {
            if (component != null) {
                component.setEntity(this);
                component.tick(time);
            }
        }
        for (ComponentBehavior component : components) {
            if (component != null) {
                component.setEntity(this);
                component.onLateUpdate(time);
            }
        }
    }

    @Override
    public void render(Renderer renderer) {
        for (ComponentBehavior component : components) {
            if (component != null) {
                component.setEntity(this);
                component.render(renderer);
            }
        }
    }

    /**
     * Calculates the transformations applied to this entity by its parent and itself.
     *
     * @return The transformations applied to this entity.
     */
    public Transform2D hierarchyTransform() {
        Transform2D t = new Transform2D(transform);
        if(parent != null && parent instanceof Entity2D ep) {
            Transform2D parentTransform = ep.hierarchyTransform();
            t.rotate(parentTransform.zRotation());
            t.translate(parentTransform.position());
            t.scale(parentTransform.scale());
        }
        return t;
    }

     //For each component, set the entity and frames, and call onStart and onUpdate.
    /*public DrawInstruction process() {
        frames++;
        if (visible) {
            entityDrawInstruction.setX(transform().position().x());
            entityDrawInstruction.setY(transform().position().y());
            entityDrawInstruction.setScaleX(transform().scale().x());
            entityDrawInstruction.setScaleY(transform().scale().y());
            entityDrawInstruction.setRotation(transform().zRotation());
            entityDrawInstruction.childrenInstructions().clear();
            entityDrawInstruction.setCenterOffsetX(transform().centerOffset().x());
            entityDrawInstruction.setCenterOffsetY(transform().centerOffset().y());
        }
        try {
            for (int i = 0; i < components.size(); i++) {
                ComponentBehavior component = null;

                component = components.get(i);

                if (component != null) {
                    component.setEntity(this);
                    component.setFrames(component.frames() + 1);
                    if (component.frames() == 1)
                        component.onStart();
                    component.onUpdate();
                    if (visible) {
                        DrawInstruction di = component.render();
                        if (di != null)
                            entityDrawInstruction.childrenInstructions().add(di);
                    }
                }
            }
        } catch (IndexOutOfBoundsException ignore) {

        }
        for (Entity2D child : entities()) {
            DrawInstruction di = child.process();
            child.updateEntityList();
            if (visible)
                entityDrawInstruction.childrenInstructions().add(di);
        }
        if (visible)
            return entityDrawInstruction;
        return null;
    }*/

    @Override
    public void close() throws IOException {
        for (Entity2D entity : entities()) {
            entity.close();
        }
        for (ComponentBehavior component : components) {
            component.close();
        }
        if (parent() != null) {
            parent().toRemoveEntities().add(this);
        }
    }

    /**
     * Checks if a given tag is present in the array of tags.
     *
     * @param tag The tag to check for.
     * @return True if the tag is present in the array of tags, false otherwise.
     */
    public boolean containsTag(String tag) {
        for (String t : tags) {
            if (t.hashCode() == tag.hashCode() && t.equals(tag))
                return true;
        }
        return false;
    }

    /**
     * Gets the component of the specified type at the specified renderIndex.
     *
     * @param componentType The type of component to retrieve.
     * @param index          The renderIndex of the component to retrieve.
     * @return The component of the specified type at the specified renderIndex, or null if no such component exists.
     */
    public <T extends ComponentBehavior> T component(Class<T> componentType, int index) {
        int i = 0;
        for (ComponentBehavior c : components) {
            if (c.getClass().hashCode() == componentType.hashCode() && c.getClass().equals(componentType)) {
                if (index == i) {
                    //noinspection unchecked
                    return (T) c;
                }
                i++;
            }
        }
        return null;
    }

    /**
     * Gets the first component of the specified type.
     *
     * @param componentType The type of component to retrieve.
     * @param <T>            The type of component to retrieve.
     * @return The first component of the specified type, or null if no such component exists.
     */
    public <T extends ComponentBehavior> T component(Class<T> componentType) {
        return component(componentType, 0);
    }

    /**
     * Gets the transformations applied to this entity.
     *
     * @return The transformations applied to this entity.
     */
    public Transform2D transform() {
        return transform;
    }

    /**
     * Gets the array of components that this entity manages.
     *
     * @return The array of components that this entity manages.
     */
    public ComponentBehavior[] components() {
        return components;
    }

    /**
     * Gets the parent entity of this entity, or null if it has no parent.
     *
     * @return The parent entity of this entity, or null if it has no parent.
     */
    public Entity2DContainer parent() {
        return parent;
    }

    /**
     * Gets the renderIndex of this entity.
     *
     * @return The renderIndex of this entity.
     */
    public int renderIndex() {
        return renderIndex;
    }

    /**
     * Sets the renderIndex of this entity.
     *
     * @param renderIndex The new renderIndex for this entity.
     * @return This entity, for method chaining.
     */
    public Entity2D setRenderIndex(int renderIndex) {
        this.renderIndex = renderIndex;
        return this;
    }

    /**
     * Gets the array of tags that describe this entity.
     *
     * @return The array of tags that describe this entity.
     */
    public String[] tags() {
        return tags;
    }

    /**
     * Gets the visibility of this entity.
     *
     * @return The visibility of this entity.
     */
    public boolean visible() {
        return visible;
    }

    /**
     * Sets the visibility of this entity.
     *
     * @param visible True to make this entity visible, false to hide it.
     * @return This entity, for method chaining.
     */
    public Entity2D setVisible(boolean visible) {
        this.visible = visible;
        return this;
    }
}
