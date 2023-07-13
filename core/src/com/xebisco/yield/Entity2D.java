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

import com.xebisco.yield.physics.ContactAdapter;
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
        try {
            for (Entity2D child : getEntities()) {
                child.processPhysics();
            }
        } catch (ConcurrentModificationException ignore) {

        }
        try {
            for (int i = 0; i < components.size(); i++) {
                ComponentBehavior component = null;

                component = components.get(i);
                if (component != null)
                    component.onPhysicsUpdate();
            }
        } catch (IndexOutOfBoundsException ignore) {

        }
    }

    /**
     * For each component, set the entity and frames, and call onStart and onUpdate.
     */
    public DrawInstruction process() {
        frames++;
        if (visible) {
            entityDrawInstruction.setX(getTransform().getPosition().getX());
            entityDrawInstruction.setY(getTransform().getPosition().getY());
            entityDrawInstruction.setScaleX(getTransform().getScale().getX());
            entityDrawInstruction.setScaleY(getTransform().getScale().getY());
            entityDrawInstruction.setRotation(getTransform().getzRotation());
            entityDrawInstruction.getChildrenInstructions().clear();
            entityDrawInstruction.setCenterOffsetX(getTransform().getCenterOffset().getX());
            entityDrawInstruction.setCenterOffsetY(getTransform().getCenterOffset().getY());
        }
        for (Entity2D child : getEntities()) {
            DrawInstruction di = child.process();
            child.updateEntityList();
            if (visible)
                entityDrawInstruction.getChildrenInstructions().add(di);
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
                            entityDrawInstruction.getChildrenInstructions().add(di);
                    }
                }
            }
        } catch (IndexOutOfBoundsException ignore) {

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
        if (getParent() != null) {
            getParent().getToRemoveEntities().add(this);
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

    /**
     * Returns the transform of the entity.
     *
     * @return The transform of the entity.
     */
    public Transform2D getTransform() {
        return transform;
    }

    /**
     * This function returns the index of the entity.
     *
     * @return The index of the entity.
     */
    public int getIndex() {
        return index;
    }

    /**
     * This function sets the index of the entity to the index passed in as a parameter.
     *
     * @param index The index of the entity.
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * This function returns a list of ComponentBehavior objects.
     *
     * @return A list of ComponentBehavior objects.
     */
    public List<ComponentBehavior> getComponents() {
        return components;
    }

    /**
     * This function sets the components of the current object to the components of the parameter.
     *
     * @param components A list of ComponentBehavior objects.
     */
    public void setComponents(List<ComponentBehavior> components) {
        this.components = components;
    }

    /**
     * This function returns the number of frames in the entity.
     *
     * @return The number of frames in the entity.
     */
    public int getFrames() {
        return frames;
    }

    /**
     * This function sets the number of frames in the entity.
     *
     * @param frames The number of frames in the entity.
     */
    public void setFrames(int frames) {
        this.frames = frames;
    }

    /**
     * The function returns an instance of the Application class.
     *
     * @return The method is returning an object of type `Application`.
     */
    public Application getApplication() {
        return application;
    }

    public void addContactListener(ContactListener contactListener) {
        getContactListeners().add(contactListener);
    }

    public List<ContactListener> getContactListeners() {
        return contactListeners;
    }

    public void setContactListeners(List<ContactListener> contactListeners) {
        this.contactListeners = contactListeners;
    }

    /**
     * The function returns a boolean value indicating whether an entity is visible or not.
     *
     * @return The method `isVisible()` is returning a boolean value, which indicates whether the entity is visible or not.
     * The value returned depends on the value of the `visible` variable.
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * This function sets the visibility of this entity.
     *
     * @param visible The "visible" parameter is a boolean variable that determines whether this entity should
     *                be visible or not. If the value of "visible" is true, the object will be visible on the screen. If the
     *                value is false, the object or component will be hidden from view.
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * This function returns the parent container of a 2D entity in Java.
     *
     * @return The method is returning an object of type `Entity2DContainer`, which is the parent of the current entity.
     */
    public Entity2DContainer getParent() {
        return parent;
    }

    /**
     * This function sets the parent of a 2D entity container.
     *
     * @param parent The parameter "parent" is an object of type Entity2DContainer, which represents the parent container
     *               of the current object. This method sets the parent container of the current object to the specified parent object.
     */
    public void setParent(Entity2DContainer parent) {
        this.parent = parent;
    }

    /**
     * The function returns an array of strings representing tags.
     *
     * @return The tags array.
     */
    public String[] getTags() {
        return tags;
    }

    /**
     * This function sets the value of the tags array of this entity.
     *
     * @param tags The tags array to set.
     */
    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public DrawInstruction getEntityDrawInstruction() {
        return entityDrawInstruction;
    }
}
