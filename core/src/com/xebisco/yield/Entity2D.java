/*
 * Copyright [2022-2023] [Xebisco]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.xebisco.yield;

import java.util.*;
import java.util.stream.IntStream;

/**
 * It's a class that can be rendered, disposed, and compared to other entities
 */
public final class Entity2D implements Renderable, Disposable, Comparable<Entity2D> {
    private List<ComponentBehavior> components = new ArrayList<>();
    private TreeSet<Entity2D> children = new TreeSet<>();
    private int index;
    private final Transform2D transform = new Transform2D();
    private FontLoader fontLoader;
    private TextureLoader textureLoader;
    private int frames;

    public Entity2D() {
    }

    public Entity2D(Collection<Entity2D> children) {
        if (children != null)
            this.children.addAll(children);
    }

    public Entity2D(TreeSet<Entity2D> children, ComponentBehavior... components) {
        this(children);
        this.components.addAll(List.of(components));
    }

    /**
     * For each component, set the entity and frames, and call onStart and onUpdate.
     */
    public void process() {
        frames++;
        IntStream.range(0, components.size()).parallel().forEach(i -> {
            ComponentBehavior component = components.get(i);
            component.setEntity(this);
            component.setFrames(component.getFrames() + 1);
            if(component.getFrames() == 1)
                component.onStart();
            component.onUpdate();
        });
        try {
            for (Entity2D entity : children) {
                entity.process();
            }
        } catch (ConcurrentModificationException ignore) {

        }
    }

    @Override
    public void dispose() {
        for (Entity2D entity : children) {
            entity.dispose();
        }
        setComponents(null);
        setChildren(null);
    }

    @Override
    public void render(PlatformGraphics graphics) {
        for (ComponentBehavior component : components)
            component.render(graphics);
    }

    @Override
    public int compareTo(Entity2D o) {
        return Integer.compare(o.index, index);
    }

    /**
     * Get the component of the specified type at the specified index.
     * The first line of the function is a generic type declaration. This is a way of saying that the function will return
     * a component of the specified type
     *
     * @param componentType The type of component you want to get.
     * @param index The index of the component you want to get.
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
     * @param index The index of the component to remove.
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
     * Returns a set of all the children of this entity.
     *
     * @return A TreeSet of Entity2D objects.
     */
    public TreeSet<Entity2D> getChildren() {
        return children;
    }

    /**
     * This function sets the children of the entity to the children passed in.
     *
     * @param children A TreeSet of Entity2D objects.
     */
    public void setChildren(TreeSet<Entity2D> children) {
        this.children = children;
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
     * This function returns the fontLoader of this entity.
     *
     * @return The fontLoader of this entity.
     */
    public FontLoader getFontLoader() {
        return fontLoader;
    }
    /**
     * This function sets the fontLoader of this entity.
     *
     * @param fontLoader The fontLoader of this entity.
     */

    public void setFontLoader(FontLoader fontLoader) {
        this.fontLoader = fontLoader;
    }

    public TextureLoader getTextureLoader() {
        return textureLoader;
    }

    public void setTextureLoader(TextureLoader textureLoader) {
        this.textureLoader = textureLoader;
    }
}
