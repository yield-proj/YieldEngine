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
