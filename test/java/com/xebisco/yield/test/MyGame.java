package com.xebisco.yield.test;

import com.xebisco.yield.*;
import com.xebisco.yield.components.Sprite;
import com.xebisco.yield.components.Square;

public class MyGame extends YldGame
{
    public static void main(String[] args)
    {
        final GameConfiguration config = new GameConfiguration();
        config.hardwareAcceleration = true;
        launch(new MyGame(), config);
    }
}