package com.xebisco.yield.test;

import com.xebisco.yield.*;

class MyGame extends YldGame
{
    public static void main(String[] args)
    {
        new View(427, 240);
        GameConfiguration config = new GameConfiguration();
        launch(new MyGame(), config);
    }
}