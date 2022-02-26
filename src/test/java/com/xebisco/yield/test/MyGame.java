package com.xebisco.yield.test;

import com.xebisco.yield.GameConfiguration;
import com.xebisco.yield.Yld;
import com.xebisco.yield.YldGame;

public class MyGame extends YldGame
{
    @Override
    public void create()
    {
        Yld.message("test");
    }

    public static void main(String[] args)
    {
        final GameConfiguration config = new GameConfiguration();
        launch(new MyGame(), config);
    }
}