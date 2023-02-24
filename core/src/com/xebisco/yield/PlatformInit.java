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

public class PlatformInit {
    private Size2D resolution = new Size2D(1280, 720), windowSize = new Size2D(1280, 720);
    private String title = "Yield Window";
    private boolean fullscreen, undecorated;

    public Size2D getResolution() {
        return resolution;
    }

    public void setResolution(Size2D resolution) {
        this.resolution = resolution;
    }

    public Size2D getWindowSize() {
        return windowSize;
    }

    public void setWindowSize(Size2D windowSize) {
        this.windowSize = windowSize;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isFullscreen() {
        return fullscreen;
    }

    public void setFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
    }

    public boolean isUndecorated() {
        return undecorated;
    }

    public void setUndecorated(boolean undecorated) {
        this.undecorated = undecorated;
    }
}
