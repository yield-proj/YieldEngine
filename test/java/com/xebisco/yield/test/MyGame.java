package com.xebisco.yield.test;

import com.xebisco.yield.*;

public class MyGame extends YldGame
{


    public static void main(String[] args)
    {
        final GameConfiguration config = new GameConfiguration();
        launch(new MyGame(), config);
    }
}