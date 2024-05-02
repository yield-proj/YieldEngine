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

package com.xebisco.yield.manager;

import com.xebisco.yield.Color;
import com.xebisco.yield.PlatformInit;
import com.xebisco.yield.Transform2D;
import com.xebisco.yield.rendering.Renderer;
import com.xebisco.yield.texture.Texture;

import java.io.Closeable;

/**
 * The {@code GraphicsManager} interface is a wrapper around the platform's graphics API. It's a simple class that provides a
 * few functions for drawing to the screen
 */
public interface GraphicsManager extends Closeable, Renderer {

    /**
     * Initializes the platform graphics
     *
     * @param platformInit This is a struct that contains the parameters that will be used to init the graphics.
     */
    void init(PlatformInit platformInit);

    /**
     * This function updates the icon the window using a texture.
     *
     * @param icon The "icon" parameter is a Texture object that represents the image that will be used as the icon for the
     *             window. This function updates the icon of the window to the specified texture.
     */
    void updateWindowIcon(Texture icon);

    /**
     * Returns true if the user has requested that the application close.
     *
     * @return A boolean value.
     */
    boolean shouldClose();

    void start(Color clearColor);
    void finish();

    /**
     * The function sets the position of the camera in a 2D space.
     *
     * @param camera The parameter "camera" is a Vector2D object that represents the position of the camera in a 2D space.
     */
    void setCamera(Transform2D camera);
}
