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

package com.xebisco.yieldengine;

import com.xebisco.yieldengine.rendering.Renderer;
import com.xebisco.yieldengine.texture.TextureFilter;

import java.io.IOException;

/**
 * Abstract class representing a behavior that can be attached to an {@link Entity2D}.
 * It provides methods for updating, rendering, and accessing entity-related information.
 */
public abstract class ComponentBehavior extends AbstractBehavior implements Renderable {
    private Entity2D entity;

    /**
     * Called when the behavior is created.
     */
    public void onCreate() {

    }

    /**
     * Called after all {@link #onUpdate(ContextTime)} calls.
     *
     * @param time The context time.
     */
    public void onLateUpdate(ContextTime time) {

    }

    /**
     * Called when the entity starts.
     */
    @Override
    public void onStart() {

    }

    /**
     * Called every frame.
     *
     * @param time The context time.
     */
    @Override
    public void onUpdate(ContextTime time) {

    }

    /**
     * Called when the behavior is closed.
     *
     * @throws IOException If an I/O error occurs.
     */
    @Override
    public void close() throws IOException {

    }

    /**
     * Renders the behavior.
     *
     * @param renderer The renderer.
     */
    @Override
    public void render(Renderer renderer) {

    }

    /**
     * Returns the entity this behavior is attached to.
     *
     * @return The entity.
     */
    public Entity2D entity() {
        return entity;
    }

    /**
     * Returns the application this behavior belongs to.
     *
     * @return The application.
     */
    public Application application() {
        return entity().application();
    }

    /**
     * Sets the entity this behavior is attached to.
     *
     * @param entity The entity.
     * @return This behavior.
     */
    public ComponentBehavior setEntity(Entity2D entity) {
        this.entity = entity;
        return this;
    }

    /**
     * Returns the transform of the entity this behavior is attached to.
     *
     * @return The transform.
     */
    public Transform2D transform() {
        return entity().transform();
    }

    /**
     * Returns the render index of the entity this behavior is attached to.
     *
     * @return The render index.
     */
    public int renderIndex() {
        return entity().renderIndex();
    }

    /**
     * Sets the render index of the entity this behavior is attached to.
     *
     * @param index The render index.
     * @return This behavior.
     */
    public ComponentBehavior setRenderIndex(int index) {
        entity().setRenderIndex(index);
        return this;
    }

    /**
     * Returns a component of the specified type attached to the entity this behavior is attached to.
     *
     * @param componentType The type of the component.
     * @return The component.
     */
    public <T extends ComponentBehavior> T component(Class<T> componentType) {
        return entity().component(componentType);
    }

    /**
     * Returns a component of the specified type and index attached to the entity this behavior is attached to.
     *
     * @param componentType The type of the component.
     * @param index The index of the component.
     * @return The component.
     */
    public <T extends ComponentBehavior> T component(Class<T> componentType, int index) {
        return entity().component(componentType, index);
    }

    /**
     * Returns a texture loaded on demand from the specified path and filter.
     *
     * @param path The path of the texture.
     * @param filter The texture filter.
     * @return The texture.
     */
    public final AbstractTexture texture(String path, TextureFilter filter) {
        return new OnDemandTexture(path, filter, () -> application().applicationPlatform().textureManager(), () -> application().applicationPlatform().ioManager());
    }

    /**
     * Returns a texture loaded on demand from the specified path with a default linear filter.
     *
     * @param path The path of the texture.
     * @return The texture.
     */
    public final AbstractTexture texture(String path) {
        return texture(path, TextureFilter.LINEAR);
    }
}