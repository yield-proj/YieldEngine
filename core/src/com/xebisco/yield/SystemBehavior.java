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

/**
 * This is an abstract class that implements the Behavior interface and contains methods for managing scenes.
 */
public abstract class SystemBehavior implements Behavior {

    private int frames;
    private Scene scene;

    @Override
    public void onStart() {

    }

    @Override
    public void onUpdate() {

    }

    @Override
    public void dispose() {

    }

    /**
     * The function returns a Scene object.
     *
     * @return The `scene()` method is returning a `Scene` object.
     */
    public Scene scene() {
        return scene;
    }

    /**
     * This function sets the scene for a Java application.
     *
     * @param scene The method "setScene" sets the value of the
     *              instance variable "scene" to the value passed as the parameter.
     */
    public SystemBehavior setScene(Scene scene) {
        this.scene = scene;
        return this;
    }

    /**
     * The function returns the value of the frames variable.
     *
     * @return The method is returning an integer value of the variable "frames".
     */
    public int frames() {
        return frames;
    }

    /**
     * The function sets the number of frames for a given object.
     *
     * @param frames The "frames" parameter is an integer value that represents the number of frames.
     */
    public SystemBehavior setFrames(int frames) {
        this.frames = frames;
        return this;
    }
}
