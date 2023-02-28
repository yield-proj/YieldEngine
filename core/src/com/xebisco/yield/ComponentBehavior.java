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

/**
 * ComponentBehavior is an abstract class that implements the Behavior and Renderable interfaces.
 * This the class is declared as abstract. This means that it cannot be instantiated. It
 * is only meant to be extended by other classes
 */
public abstract class ComponentBehavior implements Behavior, Renderable {
    private int frames;
    private Entity2D entity;

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
}
