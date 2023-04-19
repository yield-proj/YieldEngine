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

import com.xebisco.yield.physics.RayCast;

/**
 * ComponentBehavior is an abstract class that implements the Behavior and Renderable interfaces.
 * This the class is declared as abstract. This means that it cannot be instantiated. It
 * is only meant to be extended by other classes
 */
public abstract class ComponentBehavior implements Behavior, Renderable {
    public static final String HORIZONTAL = Global.HORIZONTAL, VERTICAL = Global.VERTICAL;
    private int frames;
    private Entity2D entity;

    @Override
    public void onStart() {

    }

    @Override
    public void onUpdate() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void render(PlatformGraphics graphics) {

    }

    /**
     * Raycast from point1 to point2 and return the closest hit.
     *
     * @param point1 The starting point of the ray-cast.
     * @param point2 The end point of the ray.
     * @return A RayCast object.
     */
    public final RayCast rayCast(Vector2D point1, Vector2D point2) {
        return getApplication().getScene().getPhysicsSystem().rayCast(getEntity(), point1, point2);
    }

    /**
     * This function returns the number of frames that this component is in a scene.
     *
     * @return The number of frames in the that this component is in a scene.
     */
    public int getFrames() {
        return frames;
    }

    /**
     * This function sets the number of frames.
     *
     * @param frames The number of frames.
     */
    public void setFrames(int frames) {
        this.frames = frames;
    }

    /**
     * This function returns the entity that this component is attached to.
     *
     * @return The entity variable is being returned.
     */
    public Entity2D getEntity() {
        return entity;
    }

    /**
     * This function sets the entity variable to the entity passed in.
     *
     * @param entity The entity that the component is attached to.
     */
    public void setEntity(Entity2D entity) {
        this.entity = entity;
    }

    /**
     * Returns the transform of the entity.
     *
     * @return The transform of the entity.
     */
    public Transform2D getTransform() {
        return getEntity().getTransform();
    }

    /**
     * This function returns the index of the entity.
     *
     * @return The index of the entity.
     */

    public int getIndex() {
        return getEntity().getIndex();
    }

    /**
     * This function sets the index of the entity to the index passed in as a parameter.
     *
     * @param index The index of the entity.
     */

    public void setIndex(int index) {
        getEntity().setIndex(index);
    }

    /**
     * Returns the first component of the specified type, or null if no such component exists.
     *
     * @param componentType The type of the component to retrieve.
     * @return The first component of the given type.
     */

    public <T extends ComponentBehavior> T getComponent(Class<T> componentType) {
        return getEntity().getComponent(componentType);
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
        return getEntity().getComponent(componentType, index);
    }

    /**
     * This function returns the application associated with the entity.
     *
     * @return The method is returning an instance of the `Application` class. It is getting the `Application` object from
     * the `Entity` object and returning it.
     */
    public Application getApplication() {
        return getEntity().getApplication();
    }

    /**
     * This function returns the time context of the application manager.
     *
     * @return The method `getTime()` is returning an object of type `ContextTime`.
     */
    public ContextTime getTime() {
        return getEntity().getApplication().getApplicationManager().getManagerContext().getContextTime();
    }
}
