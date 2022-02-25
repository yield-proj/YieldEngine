package com.xebisco.yield.test;

import com.xebisco.yield.GameConfiguration;
import com.xebisco.yield.Yld;
import com.xebisco.yield.YldGame;
import com.xebisco.yield.extensions.YieldOverlay;
import com.xebisco.yield.input.Keys;

import java.awt.event.KeyEvent;

public class MyGame extends YldGame
{

    @Override
    public void create()
    {
        input.addShortcut(new Keys(KeyEvent.VK_ALT, KeyEvent.VK_M), () ->
                YieldOverlay.setShow(!YieldOverlay.isShow())
        );
    }

    public static void main(String[] args)
    {
        GameConfiguration config = new GameConfiguration();
        launch(new MyGame(), config);

        Yld.message("Hello, World!");
    }
}