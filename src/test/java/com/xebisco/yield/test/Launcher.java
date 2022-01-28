package com.xebisco.yield.test;

import com.xebisco.yield.GameConfiguration;
import com.xebisco.yield.GameManager;

public class Launcher {

    public static void main(String[] args) {
        GameConfiguration config = new GameConfiguration();
        config.fps = 60;
        config.width = 1280;
        config.height = 720;
        config.fullscreen = false;
        GameManager.launch(new MyGame(), config);
    }

}
