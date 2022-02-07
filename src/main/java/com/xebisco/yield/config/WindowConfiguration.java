package com.xebisco.yield.config;

public class WindowConfiguration {
    public int width = 1280, height = 745, fps = 60;
    public WindowPos position = WindowPos.CENTER;
    public String title = null, internalIconPath = "/com/xebisco/yield/assets/yieldlogo.png";
    public boolean doubleBuffered = true, resizable = true, undecorated = false, fullscreen = false, alwaysOnTop = false, hideMouse;

    public enum WindowPos {
        CENTER, CUSTOM
    }
}
