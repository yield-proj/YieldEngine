package com.xebisco.yield.test;

import com.xebisco.yield.*;
import com.xebisco.yield.components.Text;

public class MyGame extends YldGame
{
    @Override
    public void create()
    {
        instantiate((e) -> {
            e.addComponent(new Text("))"));
            e.setIndex(10);
        });
    }

    public static void main(String[] args)
    {
        final GameConfiguration config = new GameConfiguration();
        launch(new MyGame(), config);
    }
}