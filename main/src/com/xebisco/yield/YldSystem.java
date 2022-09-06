/*
 * Copyright [2022] [Xebisco]
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

/**
 * This is the primitive class that is used in the YldScene to store all the different types of Systems. Example: ProcessSystem, UpdateSystem
 * @since 4_beta1
 * @author Xebisco
 */
public abstract class YldSystem {
    /**
     * The YldScene that have this system stored.
     */
    protected YldScene scene;

    /**
     *
     * @param e The entity.
     * @param delta The time in seconds since the last frame.
     */
    public abstract void receive(Entity e, float delta);

    /**
     * This function is called when the system is destroyed.
     */
    public abstract void destroy();

    /**
     * Getter for the scene variable.
     */
    public YldScene getScene() {
        return scene;
    }

    /**
     * Setter for the scene variable.
     */
    public void setScene(YldScene scene) {
        this.scene = scene;
    }
}
