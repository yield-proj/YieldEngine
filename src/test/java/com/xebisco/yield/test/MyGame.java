package com.xebisco.yield.test;

import com.xebisco.yield.*;
import com.xebisco.yield.config.GameConfiguration;

public class MyGame extends YldGame {
    public static void main(String[] args) {
        GameConfiguration config = new GameConfiguration();
        launch(new MyGame(), config);
        new Resolution(427, 240);
    }
}