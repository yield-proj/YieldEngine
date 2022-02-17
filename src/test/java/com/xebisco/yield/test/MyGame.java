package com.xebisco.yield.test;

import com.xebisco.yield.*;

class MyGame extends YldGame {
    @Override
    public void create() {
        load("/com/xebisco/yield/test/TestScript");
    }

    public static void main(String[] args) {
        GameConfiguration config = new GameConfiguration();
        launch(new MyGame(), config);
        new View(427, 240);
    }
}