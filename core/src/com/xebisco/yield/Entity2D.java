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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Entity2D implements Renderable, Behavior {
    private List<ComponentBehavior> components = new ArrayList<>();
    private List<Entity2D> children = new ArrayList<>();
    private int frames;

    @Override
    public void onStart() {
        frames = 0;
    }

    @Override
    public void onUpdate() {
        frames++;
        IntStream.range(0, components.size()).parallel().forEach(i -> children.get(0).onUpdate());
    }

    @Override
    public void dispose() {
        IntStream.range(0, components.size()).parallel().forEach(i -> children.get(0).dispose());
        setComponents(null);
        setChildren(null);
    }

    @Override
    public void render(PlatformGraphics graphics) {

    }

    public <T extends ComponentBehavior> T getComponent(Class<T> componentType, int index) {
        int i = 0;
        for(ComponentBehavior c : components) {
            if(c.getClass().hashCode() == componentType.hashCode() && c.getClass().equals(componentType)) {
                if(index == i) {
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

    public void addComponent(ComponentBehavior component) {
        components.add(component);
        component.onAdd();
    }

    public void removeComponent(ComponentBehavior component) {
        components.add(component);
        component.onAdd();
    }

    public void removeComponent(Class<? extends ComponentBehavior> componentType, int index) {
        removeComponent(getComponent(componentType, index));
    }

    public void removeComponent(Class<? extends ComponentBehavior> componentType) {
        removeComponent(getComponent(componentType));
    }

    public List<ComponentBehavior> getComponents() {
        return components;
    }

    public void setComponents(List<ComponentBehavior> components) {
        this.components = components;
    }

    public List<Entity2D> getChildren() {
        return children;
    }

    public void setChildren(List<Entity2D> children) {
        this.children = children;
    }

    public int getFrames() {
        return frames;
    }

    public void setFrames(int frames) {
        this.frames = frames;
    }
}
