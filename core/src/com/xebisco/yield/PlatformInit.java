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
 * It's a class that holds the initial configuration of the specific platform.
 */
public class PlatformInit {
    private Size2D gameResolution = new Size2D(1280, 720), uiResolution = new Size2D(1280, 720), windowSize = new Size2D(1280, 720);
    private String title = "Yield Window";
    private boolean fullscreen, undecorated, stretchViewport;
    private Texture windowIcon;

    /**
     * Returns the size of the window.
     *
     * @return The windowSize variable is being returned.
     */
    public Size2D getWindowSize() {
        return windowSize;
    }

    /**
     * This function sets the window size to the given size.
     *
     * @param windowSize The size of the window in pixels.
     */
    public void setWindowSize(Size2D windowSize) {
        this.windowSize = windowSize;
    }

    /**
     * This function returns the title of the window.
     *
     * @return The title of the window.
     */
    public String getTitle() {
        return title;
    }

    /**
     * This function sets the title of the window.
     *
     * @param title The title of the window.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns true if the game is in fullscreen mode, false otherwise.
     *
     * @return The value of the variable fullscreen.
     */
    public boolean isFullscreen() {
        return fullscreen;
    }

    /**
     * This function sets the fullscreen variable to the value of the fullscreen parameter.
     *
     * @param fullscreen Whether the game should be fullscreen.
     */
    public void setFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
    }

    /**
     * Returns true if the window is undecorated, false otherwise.
     *
     * @return The boolean value of the variable undecorated.
     */
    public boolean isUndecorated() {
        return undecorated;
    }

    /**
     * This function sets the undecorated variable to the value of the parameter undecorated.
     *
     * @param undecorated If true, the window will not have a title bar, close button, or window border.
     */
    public void setUndecorated(boolean undecorated) {
        this.undecorated = undecorated;
    }


    /**
     * Return true if application viewport will be stretched.
     *
     * @return The boolean value of the variable stretchViewport.
     */
    public boolean isStretchViewport() {
        return stretchViewport;
    }

    /**
     * This function sets the undecorated variable to the value of the parameter undecorated.
     *
     * @param stretchViewport If true, the application viewport will be stretched.
     */
    public void setStretchViewport(boolean stretchViewport) {
        this.stretchViewport = stretchViewport;
    }

    public Texture getWindowIcon() {
        return windowIcon;
    }

    public void setWindowIcon(Texture windowIcon) {
        this.windowIcon = windowIcon;
    }

    public Size2D getUiResolution() {
        return uiResolution;
    }

    public void setUiResolution(Size2D uiResolution) {
        this.uiResolution = uiResolution;
    }

    public Size2D getGameResolution() {
        return gameResolution;
    }

    public void setGameResolution(Size2D gameResolution) {
        this.gameResolution = gameResolution;
    }
}
