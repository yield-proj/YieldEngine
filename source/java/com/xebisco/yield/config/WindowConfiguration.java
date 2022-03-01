package com.xebisco.yield.config;

public class WindowConfiguration
{
    public int width = 1280, height = 745;
    public WindowPos position = WindowPos.CENTER;
    public String title = null, internalIconPath = "/com/xebisco/yield/assets/icon.png";
    public boolean doubleBuffered = true, resizable = true, undecorated = false, fullscreen = false, alwaysOnTop = false, hideMouse, sync = true;

    public enum WindowPos
    {
        CENTER, CUSTOM
    }
}
