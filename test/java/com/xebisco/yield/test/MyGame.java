package com.xebisco.yield.test;

import com.xebisco.yield.*;
import com.xebisco.yield.components.Text;

import java.awt.*;

public class MyGame extends YldGame
{
    @Override
    public void create()
    {

    }

    public static void main(String[] args)
    {
        final GameConfiguration config = new GameConfiguration();
        launch(new MyGame(), config);
    }
}