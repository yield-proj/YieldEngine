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

import com.xebisco.yield.physics.ContactListener;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

/**
 * The Entity2D class is a final class that extends Entity2DContainer and implements Renderable and Disposable, and
 * contains various methods and properties for managing components and rendering.
 */
public final class Entity2D extends Entity2DContainer implements Disposable {
    private final Transform2D transform = new Transform2D();
    private final Application application;
    private List<ComponentBehavior> components = new ArrayList<>();
    private Entity2DContainer parent;
    private int index;

    private List<ContactListener> contactListeners = new ArrayList<>();

    private String[] tags;
    private int frames;
    private boolean visible = true;

    private final DrawInstruction entityDrawInstruction = new DrawInstruction();

    Entity2D(Application application, ComponentBehavior[] components) {
        super(application);
        this.application = application;
        for (ComponentBehavior c : components) {
            c.setEntity(this);
        }
        this.components.addAll(List.of(components));
    }

    public void processPhysics() {
        if(frames <= 1)
            return;
        try {
            for (int i = 0; i < components.size(); i++) {
                ComponentBehavior component = null;

                component = components.get(i);
                if (component != null)
                    component.onPhysicsUpdate();
            }
        } catch (IndexOutOfBoundsException ignore) {

        }
        try {
            for (Entity2D child : getEntities()) {
                child.processPhysics();
            }
        } catch (ConcurrentModificationException ignore) {

        }
    }

    /**
     * For each component, set the entity and frames, and call onStart and onUpdate.
     */
    public DrawInstruction process() {
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
                    component.setFrames(component.getFrames() + 1);
                    if (component.getFrames() == 1)
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
        for (Entity2D child : getEntities()) {
            DrawInstruction di = child.process();
            child.updateEntityList();
            if (visible)
                entityDrawInstruction.childrenInstructions().add(di);
        }
        if (visible)
            return entityDrawInstruction;
        return null;
    }

    @Override
    public void dispose() {
        for (Entity2D entity : getEntities()) {
            entity.dispose();
        }
        for (ComponentBehavior component : components) {
            component.dispose();
        }
        if (parent() != null) {
            parent().getToRemoveEntities().add(this);
            setParent(null);
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
     * Get the component of the specified type at the specified index.
     * The first line of the function is a generic type declaration. This is a way of saying that the function will return
     * a component of the specified type
     *
     * @param componentType The type of component you want to get.
     * @param index         The index of the component you want to get.
     * @return A component of the specified type.
     */
    public <T extends ComponentBehavior> T getComponent(Class<T> componentType, int index) {
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
    public <T extends ComponentBehavior> T getComponent(Class<T> componentType) {
        return getComponent(componentType, 0);
    }

    /**
     * Remove the component from the list of components and dispose of it.
     *
     * @param component The component to remove.
     */
    public void removeComponent(ComponentBehavior component) {
        components.remove(component);
        component.dispose();
    }

    /**
     * Remove the component of the given type at the given index.
     *
     * @param componentType The type of component to remove.
     * @param index         The index of the component to remove.
     */
    public void removeComponent(Class<? extends ComponentBehavior> componentType, int index) {
        removeComponent(getComponent(componentType, index));
    }

    /**
     * Remove the component of the given type from the entity.
     *
     * @param componentType The type of the component to remove.
     */
    public void removeComponent(Class<? extends ComponentBehavior> componentType) {
        removeComponent(getComponent(componentType));
    }

    public Transform2D transform() {
        return transform;
    }

    public Application application() {
        return application;
    }

    public List<ComponentBehavior> components() {
        return components;
    }

    public Entity2D setComponents(List<ComponentBehavior> components) {
        this.components = components;
        return this;
    }

    public Entity2DContainer parent() {
        return parent;
    }

    public Entity2D setParent(Entity2DContainer parent) {
        this.parent = parent;
        return this;
    }

    public int index() {
        return index;
    }

    public Entity2D setIndex(int index) {
        this.index = index;
        return this;
    }

    public List<ContactListener> contactListeners() {
        return contactListeners;
    }

    public Entity2D setContactListeners(List<ContactListener> contactListeners) {
        this.contactListeners = contactListeners;
        return this;
    }

    public String[] tags() {
        return tags;
    }

    public Entity2D setTags(String[] tags) {
        this.tags = tags;
        return this;
    }

    public int frames() {
        return frames;
    }

    public Entity2D setFrames(int frames) {
        this.frames = frames;
        return this;
    }

    public boolean visible() {
        return visible;
    }

    public Entity2D setVisible(boolean visible) {
        this.visible = visible;
        return this;
    }

    public DrawInstruction entityDrawInstruction() {
        return entityDrawInstruction;
    }
}
