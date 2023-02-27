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

public final class Entity2D implements Renderable, Disposable, Comparable<Entity2D> {
    private List<ComponentBehavior> components = new ArrayList<>();
    private TreeSet<Entity2D> children = new TreeSet<>();
    private int index;
    private final Transform2D transform = new Transform2D();
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

    public <T extends ComponentBehavior> T getComponent(Class<T> componentType) {
        return getComponent(componentType, 0);
    }

    public void removeComponent(ComponentBehavior component) {
        components.remove(component);
        component.dispose();
    }

    public void removeComponent(Class<? extends ComponentBehavior> componentType, int index) {
        removeComponent(getComponent(componentType, index));
    }

    public void removeComponent(Class<? extends ComponentBehavior> componentType) {
        removeComponent(getComponent(componentType));
    }

    public Transform2D getTransform() {
        return transform;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public List<ComponentBehavior> getComponents() {
        return components;
    }

    public void setComponents(List<ComponentBehavior> components) {
        this.components = components;
    }

    public TreeSet<Entity2D> getChildren() {
        return children;
    }

    public void setChildren(TreeSet<Entity2D> children) {
        this.children = children;
    }

    public int getFrames() {
        return frames;
    }

    public void setFrames(int frames) {
        this.frames = frames;
    }
}
