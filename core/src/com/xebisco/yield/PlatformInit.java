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

import java.io.Serializable;

/**
 * It's a class that holds the initial configuration of the specific platform.
 */
public class PlatformInit implements Serializable {
    private Vector2D viewportSize = new Vector2D(1280, 720), windowSize = new Vector2D(1280, 720);
    private String title = "Yield Window";
    private int startPhysicsPpm = 16;
    private boolean fullscreen, undecorated, stretchViewport, invertZIndex, verticalSync, physicsOnMainContext;
    private String windowIconPath = "com/xebisco/yield/yieldIcon.png";

    private ContextTime physicsContextTime = new ContextTime(40);
    private final Class<?>[] requiredPlatformModules;

    public static final Class<?>[] INPUT_DEFAULT = new Class<?>[]{
            FontManager.class,
            TextureManager.class,
            SpritesheetTextureManager.class,
            InputManager.class,
            MouseCheck.class,
            AudioManager.class,
            ViewportZoomScale.class,
            GraphicsManager.class
    }, CHECKS_DEFAULT = new Class<?>[]{
            FontManager.class,
            TextureManager.class,
            SpritesheetTextureManager.class,
            KeyCheck.class,
            MouseCheck.class,
            AudioManager.class,
            ViewportZoomScale.class,
            GraphicsManager.class
    };

    public PlatformInit(Class<?>[] requiredPlatformModules) {
        this.requiredPlatformModules = requiredPlatformModules;
    }

    public Vector2D viewportSize() {
        return viewportSize;
    }

    public PlatformInit setViewportSize(Vector2D viewportSize) {
        this.viewportSize = viewportSize;
        return this;
    }

    public Vector2D windowSize() {
        return windowSize;
    }

    public PlatformInit setWindowSize(Vector2D windowSize) {
        this.windowSize = windowSize;
        return this;
    }

    public String title() {
        return title;
    }

    public PlatformInit setTitle(String title) {
        this.title = title;
        return this;
    }

    public int startPhysicsPpm() {
        return startPhysicsPpm;
    }

    public PlatformInit setStartPhysicsPpm(int startPhysicsPpm) {
        this.startPhysicsPpm = startPhysicsPpm;
        return this;
    }

    public boolean fullscreen() {
        return fullscreen;
    }

    public PlatformInit setFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
        return this;
    }

    public boolean undecorated() {
        return undecorated;
    }

    public PlatformInit setUndecorated(boolean undecorated) {
        this.undecorated = undecorated;
        return this;
    }

    public boolean stretchViewport() {
        return stretchViewport;
    }

    public PlatformInit setStretchViewport(boolean stretchViewport) {
        this.stretchViewport = stretchViewport;
        return this;
    }

    public boolean invertZIndex() {
        return invertZIndex;
    }

    public PlatformInit setInvertZIndex(boolean invertZIndex) {
        this.invertZIndex = invertZIndex;
        return this;
    }

    public boolean verticalSync() {
        return verticalSync;
    }

    public PlatformInit setVerticalSync(boolean verticalSync) {
        this.verticalSync = verticalSync;
        return this;
    }

    public boolean physicsOnMainContext() {
        return physicsOnMainContext;
    }

    public PlatformInit setPhysicsOnMainContext(boolean physicsOnMainContext) {
        this.physicsOnMainContext = physicsOnMainContext;
        return this;
    }

    public String windowIconPath() {
        return windowIconPath;
    }

    public PlatformInit setWindowIconPath(String windowIconPath) {
        this.windowIconPath = windowIconPath;
        return this;
    }

    public ContextTime physicsContextTime() {
        return physicsContextTime;
    }

    public PlatformInit setPhysicsContextTime(ContextTime physicsContextTime) {
        this.physicsContextTime = physicsContextTime;
        return this;
    }

    public Class<?>[] requiredPlatformModules() {
        return requiredPlatformModules;
    }
}
