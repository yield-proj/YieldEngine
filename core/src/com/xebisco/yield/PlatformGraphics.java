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

import java.lang.reflect.InvocationTargetException;

/**
 * The `PlatformGraphics` interface is a wrapper around the platform's graphics API. It's a simple class that provides a
 * few functions for drawing to the screen
 */
public interface PlatformGraphics extends Disposable {

    static PlatformGraphics swingGraphics() throws ClassNotFoundException {
        //noinspection unchecked
        Class<? extends PlatformGraphics> swingGraphicsImplClass = (Class<? extends PlatformGraphics>) Class.forName("com.xebisco.yield.swingimpl.SwingPlatformGraphics");
        try {
            return swingGraphicsImplClass.getConstructor().newInstance();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Initializes the platform graphics
     *
     * @param platformInit This is a struct that contains the parameters that will be used to init the graphics.
     */
    void init(PlatformInit platformInit);
    /**
     * This function is called once per frame.
     * This is the first function called in render, it's used to prepare the graphics for the incoming draw calls.
     */
    void frame();
    /**
     * Draws the given draw instruction.
     *
     * @param drawInstruction A DrawInstruction object that contains all the information needed to draw the object.
     */
    void draw(DrawInstruction drawInstruction);
    /**
     * Resets the graphics rotation.
     */
    void resetRotation();
    /**
     * Returns true if the user has requested that the application close.
     *
     * @return A boolean value.
     */
    boolean shouldClose();
    /**
     * This function is called once per frame.
     * This is the last function called in render.
     */
    void conclude();
}
