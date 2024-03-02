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
 * The Entity2D class is a final class that extends Entity2DContainer and implements Renderable and Disposable, and
 * contains various methods and properties for managing components and rendering.
 */
public final class Entity2D extends Entity2DContainer {
    private final Transform2D transform = new Transform2D();
    private final ComponentBehavior[] components;
    private final Entity2DContainer parent;
    private int renderIndex;

    private final String[] tags;
    private boolean visible = true;

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

    /**
     * For each component, set the entity and frames, and call onStart and onUpdate.
     */
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
     * This function checks if a given tag is present in an array of tags.
     *
     * @param tag The parameter "tag" is a String representing the tag that we want to check if it exists in the "tags"
     *            array.
     * @return A boolean value is being returned.
     */
    public boolean containsTag(String tag) {
        for (String t : tags) {
            if (t.hashCode() == tag.hashCode() && t.equals(tag))
                return true;
        }
        return false;
    }

    /**
     * Get the component of the specified type at the specified renderIndex.
     * The first line of the function is a generic type declaration. This is a way of saying that the function will return
     * a component of the specified type
     *
     * @param componentType The type of component you want to get.
     * @param index         The renderIndex of the component you want to get.
     * @return A component of the specified type.
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
     * Returns the first component of the specified type, or null if no such component exists.
     *
     * @param componentType The type of the component to retrieve.
     * @return The first component of the given type.
     */
    public <T extends ComponentBehavior> T component(Class<T> componentType) {
        return component(componentType, 0);
    }

    public Transform2D transform() {
        return transform;
    }

    public ComponentBehavior[] components() {
        return components;
    }

    public Entity2DContainer parent() {
        return parent;
    }

    public int renderIndex() {
        return renderIndex;
    }

    public Entity2D setRenderIndex(int renderIndex) {
        this.renderIndex = renderIndex;
        return this;
    }

    public String[] tags() {
        return tags;
    }

    public boolean visible() {
        return visible;
    }

    public Entity2D setVisible(boolean visible) {
        this.visible = visible;
        return this;
    }
}
