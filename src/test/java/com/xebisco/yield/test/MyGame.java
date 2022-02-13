package com.xebisco.yield.test;

import com.xebisco.yield.YldGame;
import com.xebisco.yield.config.GameConfiguration;

class MyGame extends YldGame {
    public static void main(String[] args) {
        GameConfiguration config = new GameConfiguration();
        launch(new MyGame(), config);
    }
}