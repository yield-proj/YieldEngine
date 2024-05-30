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

import com.xebisco.yieldengine.editor.annotations.Config;
import com.xebisco.yieldengine.editor.annotations.Visible;
import com.xebisco.yieldengine.platform.ApplicationModule;

import java.io.Serial;
import java.io.Serializable;
/**
 * {@code PlatformInit} is a class that represents the platform initialization settings.
 */

@Config
public class PlatformInit implements Serializable {
    @Serial
    private static final long serialVersionUID = 6160223093067169502L;
    @Visible
    private Vector2D viewportSize = new Vector2D(1280, 720), windowSize = new Vector2D(1280, 720);
    @Visible
    private String title = "Yield Window";
    @Visible
    private boolean fullscreen, undecorated, stretchViewport, invertZIndex, verticalSync;
    @Visible
    private String windowIconPath = "yieldIcon.png";
    private final ApplicationModule[] requiredPlatformModules;

    /**
     * The {@code PC_DEFAULT} static array in the PlatformInit class serves as a predefined set of required platform modules for the PC platform.
     * These platform modules are essential components that are needed to initialize and run the application on a PC.
     */
    public static final ApplicationModule[] PC_DEFAULT = new ApplicationModule[]{
            ApplicationModule.FONT_MANAGER,
            ApplicationModule.TEXTURE_MANAGER,
            ApplicationModule.PC_INPUT_MANAGER,
            ApplicationModule.AUDIO_MANAGER,
            ApplicationModule.GRAPHICS_MANAGER,
            ApplicationModule.FILE_IO_MANAGER
    };

    /**
     * Constructs a new instance of {@link PlatformInit} with the specified required platform modules.
     *
     * @param requiredPlatformModules the required platform modules
     */
    public PlatformInit(ApplicationModule[] requiredPlatformModules) {
        this.requiredPlatformModules = requiredPlatformModules;
    }

    /**
     * Constructs a new instance of {@link PlatformInit} with the PC_DEFAULT platform modules.
     */
    public PlatformInit() {
        this.requiredPlatformModules = PC_DEFAULT;
    }

    /**
     * Gets the initial viewport size.
     *
     * @return the initial viewport size
     */
    public Vector2D viewportSize() {
        return viewportSize;
    }

    /**
     * Sets the initial viewport size.
     *
     * @param viewportSize the new viewport size
     * @return this instance for method chaining
     */
    public PlatformInit setViewportSize(Vector2D viewportSize) {
        this.viewportSize = viewportSize;
        return this;
    }

    /**
     * Gets the initial window size.
     *
     * @return the initial window size
     */
    public Vector2D windowSize() {
        return windowSize;
    }

    /**
     * Sets the initial window size.
     *
     * @param windowSize the new window size
     * @return this instance for method chaining
     */
    public PlatformInit setWindowSize(Vector2D windowSize) {
        this.windowSize = windowSize;
        return this;
    }

    /**
     * Gets the initial window title.
     *
     * @return the initial window title
     */
    public String title() {
        return title;
    }

    /**
     * Sets the initial window title.
     *
     * @param title the new window title
     * @return this instance for method chaining
     */
    public PlatformInit setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * Gets whether the window should be in fullscreen mode.
     *
     * @return whether the window should be in fullscreen mode
     */
    public boolean fullscreen() {
        return fullscreen;
    }

    /**
     * Sets whether the window should be in fullscreen mode.
     *
     * @param fullscreen whether the window should be in fullscreen mode
     * @return this instance for method chaining
     */
    public PlatformInit setFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
        return this;
    }

    /**
     * Gets whether the window should be undecorated.
     *
     * @return whether the window should be undecorated
     */
    public boolean undecorated() {
        return undecorated;
    }

    /**
     * Sets whether the window should be undecorated.
     *
     * @param undecorated whether the window should be undecorated
     * @return this instance for method chaining
     */
    public PlatformInit setUndecorated(boolean undecorated) {
        this.undecorated = undecorated;
        return this;
    }

    /**
     * Gets whether the viewport should be stretched to fit the window.
     *
     * @return whether the viewport should be stretched to fit the window
     */
    public boolean stretchViewport() {
        return stretchViewport;
    }

    /**
     * Sets whether the viewport should be stretched to fit the window.
     *
     * @param stretchViewport whether the viewport should be stretched to fit the window
     * @return this instance for method chaining
     */
    public PlatformInit setStretchViewport(boolean stretchViewport) {
        this.stretchViewport = stretchViewport;
        return this;
    }

    /**
     * Gets whether the Z-index should be inverted.
     *
     * @return whether the Z-index should be inverted
     */
    public boolean invertZIndex() {
        return invertZIndex;
    }

    /**
     * Sets whether the Z-index should be inverted.
     *
     * @param invertZIndex whether the Z-index should be inverted
     * @return this instance for method chaining
     */
    public PlatformInit setInvertZIndex(boolean invertZIndex) {
        this.invertZIndex = invertZIndex;
        return this;
    }

    /**
     * Gets whether vertical synchronization should be enabled.
     *
     * @return whether vertical synchronization should be enabled
     */
    public boolean verticalSync() {
        return verticalSync;
    }

    /**
     * Sets whether vertical synchronization should be enabled.
     *
     * @param verticalSync whether vertical synchronization should be enabled
     * @return this instance for method chaining
     */
    public PlatformInit setVerticalSync(boolean verticalSync) {
        this.verticalSync = verticalSync;
        return this;
    }

    /**
     * Gets the path to the window icon.
     *
     * @return the path to the window icon
     */
    public String windowIconPath() {
        return windowIconPath;
    }

    /**
     * Sets the path to the window icon.
     *
     * @param windowIconPath the new path to the window icon
     * @return this instance for method chaining
     */
    public PlatformInit setWindowIconPath(String windowIconPath) {
        this.windowIconPath = windowIconPath;
        return this;
    }

    /**
     * Gets the required platform modules.
     *
     * @return the required platform modules
     */
    public ApplicationModule[] requiredPlatformModules() {
        return requiredPlatformModules;
    }
}
