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

import java.io.IOException;

/**
 * ComponentBehavior is an abstract class that implements the Behavior and Renderable interfaces.
 * This the class is declared as abstract. This means that it cannot be instantiated. It
 * is only meant to be extended by other classes
 */
public abstract class ComponentBehavior extends AbstractBehavior implements Renderable {
    private Entity2D entity;

    public void onCreate() {

    }

    public void onLateUpdate(ContextTime time) {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onUpdate(ContextTime time) {

    }

    @Override
    public void close() throws IOException {

    }

    @Override
    public void render(Renderer renderer) {

    }

    /**
     * This function returns the entity that this component is attached to.
     *
     * @return The entity variable is being returned.
     */
    public Entity2D entity() {
        return entity;
    }

    public Application application() {
        return entity().application();
    }

    /**
     * This function sets the entity variable to the entity passed in.
     *
     * @param entity The entity that the component is attached to.
     */
    public ComponentBehavior setEntity(Entity2D entity) {
        this.entity = entity;
        return this;
    }

    /**
     * Returns the transform of the entity.
     *
     * @return The transform of the entity.
     */
    public Transform2D transform() {
        return entity().transform();
    }

    /**
     * This function returns the index of the entity.
     *
     * @return The index of the entity.
     */

    public int renderIndex() {
        return entity().renderIndex();
    }

    /**
     * This function sets the index of the entity to the index passed in as a parameter.
     *
     * @param index The index of the entity.
     */

    public ComponentBehavior setRenderIndex(int index) {
        entity().setRenderIndex(index);
        return this;
    }

    /**
     * Returns the first component of the specified type, or null if no such component exists.
     *
     * @param componentType The type of the component to retrieve.
     * @return The first component of the given type.
     */

    public <T extends ComponentBehavior> T component(Class<T> componentType) {
        return entity().component(componentType);
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

    public <T extends ComponentBehavior> T component(Class<T> componentType, int index) {
        return entity().component(componentType, index);
    }
}
