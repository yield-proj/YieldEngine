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
    private Size2D viewportSize = new Size2D(1280, 720), windowSize = new Size2D(1280, 720);
    private String title = "Yield Window";
    private int startPhysicsPpm = 16;
    private boolean fullscreen, undecorated, stretchViewport, verticalSync;
    private Texture windowIcon;
    private String windowIconPath = "com/xebisco/yield/yieldIcon.png";

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

    /**
     * The function returns the path of the window icon.
     *
     * @return The method `getWindowIconPath()` is returning a `String` value which is the path of the window icon.
     */
    public String getWindowIconPath() {
        return windowIconPath;
    }

    /**
     * This function sets the path of the window icon in a Yield application.
     *
     * @param windowIconPath The parameter "windowIconPath" is a string that represents the file path of the icon that will
     * be displayed in the window.
     */
    public void setWindowIconPath(String windowIconPath) {
        this.windowIconPath = windowIconPath;
    }

    /**
     * The function returns the size of the viewport as a Size2D object.
     *
     * @return The method is returning an object of type `Size2D`.
     */
    public Size2D getViewportSize() {
        return viewportSize;
    }

    /**
     * This function sets the viewport size of the application.
     *
     * @param viewportSize The parameter `viewportSize` is of type `Size2D`, which represents the size of a two-dimensional
     * area. It is being used to set the size of a viewport, which is a visible area on a screen/window.
     */
    public void setViewportSize(Size2D viewportSize) {
        this.viewportSize = viewportSize;
    }

    /**
     * This function returns the window icon as a Texture object in Yield.
     *
     * @return The method is returning a Texture object named "windowIcon".
     */
    public Texture getWindowIcon() {
        return windowIcon;
    }

    /**
     * This function sets the window icon of a Yield application.
     *
     * @param windowIcon The parameter "windowIcon" is a Texture object that represents the icon of a window in a Yield application. The method "setWindowIcon" sets the value of the windowIcon instance variable to the value passed as
     * a parameter.
     */
    public void setWindowIcon(Texture windowIcon) {
        this.windowIcon = windowIcon;
    }

    /**
     * The function returns a boolean value indicating whether vertical synchronization is enabled or not.
     *
     * @return The method `isVerticalSync()` is returning the value of the variable
     * `verticalSync`.
     */
    public boolean isVerticalSync() {
        return verticalSync;
    }

    /**
     * This function sets the value of the boolean variable "verticalSync".
     *
     * @param verticalSync A boolean parameter that determines whether vertical synchronization is enabled or disabled.
     * Vertical synchronization (or VSync) is a technique used in computer graphics to synchronize the frame rate of a game
     * or application with the refresh rate of the monitor, in order to prevent screen tearing and other visual artifacts.
     */
    public void setVerticalSync(boolean verticalSync) {
        this.verticalSync = verticalSync;
    }

    /**
     * The function returns the value of the variable startPhysicsPpm.
     *
     * @return The method is returning the value of the variable `startPhysicsPpm`.
     */
    public int getStartPhysicsPpm() {
        return startPhysicsPpm;
    }

    /**
     * This is a method that sets the value of a variable called "startPhysicsPpm".
     *
     * @param startPhysicsPpm startPhysicsPpm is a variable of type integer that represents the starting value of the
     * physics pixels per meter (ppm) for the physics simulation.
     */
    public void setStartPhysicsPpm(int startPhysicsPpm) {
        this.startPhysicsPpm = startPhysicsPpm;
    }
}
