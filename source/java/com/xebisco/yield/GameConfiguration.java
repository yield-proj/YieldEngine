package com.xebisco.yield;

import com.xebisco.yield.config.WindowConfiguration;

public class GameConfiguration extends WindowConfiguration
{
    public int fps = 60;
    public boolean fpsLock = true, hardwareAcceleration = false, vSync = false, alwaysRender = false, showFPS;
    public String appName;
}
