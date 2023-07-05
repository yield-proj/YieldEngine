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
 * The class ApplicationPlatform contains various managers and utilities for a Yield application.
 */
public class ApplicationPlatform {
    private final FontManager fontManager;
    private final TextureManager textureManager;
    private final InputManager inputManager;
    private final KeyCheck keyCheck;
    private final MouseCheck mouseCheck;
    private final AudioManager audioManager;
    private final ViewportZoomScale viewportZoomScale;
    private final ToggleFullScreen toggleFullScreen;
    private final GraphicsManager graphicsManager;

    private final SpritesheetTextureManager spritesheetTextureManager;

    public ApplicationPlatform(FontManager fontManager, TextureManager textureManager, InputManager inputManager, KeyCheck keyCheck, MouseCheck mouseCheck, AudioManager audioManager, ViewportZoomScale viewportZoomScale, ToggleFullScreen toggleFullScreen, GraphicsManager graphicsManager, SpritesheetTextureManager spritesheetTextureManager) {
        this.fontManager = fontManager;
        this.textureManager = textureManager;
        this.inputManager = inputManager;
        this.keyCheck = keyCheck;
        this.mouseCheck = mouseCheck;
        this.audioManager = audioManager;
        this.viewportZoomScale = viewportZoomScale;
        this.toggleFullScreen = toggleFullScreen;
        this.graphicsManager = graphicsManager;
        this.spritesheetTextureManager = spritesheetTextureManager;
    }

    public FontManager getFontManager() {
        return fontManager;
    }

    public TextureManager getTextureManager() {
        return textureManager;
    }

    public InputManager getInputManager() {
        return inputManager;
    }

    public KeyCheck getKeyCheck() {
        return keyCheck;
    }

    public MouseCheck getMouseCheck() {
        return mouseCheck;
    }

    public AudioManager getAudioManager() {
        return audioManager;
    }

    public ViewportZoomScale getViewportZoomScale() {
        return viewportZoomScale;
    }

    public ToggleFullScreen getToggleFullScreen() {
        return toggleFullScreen;
    }

    public GraphicsManager getGraphicsManager() {
        return graphicsManager;
    }

    public SpritesheetTextureManager getSpritesheetTextureManager() {
        return spritesheetTextureManager;
    }
}
