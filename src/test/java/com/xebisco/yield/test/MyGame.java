package com.xebisco.yield.test;

import com.xebisco.yield.GameConfiguration;
import com.xebisco.yield.YldGame;
import com.xebisco.yield.extensions.FullscreenDetectorExtension;
import com.xebisco.yield.extensions.YieldOverlay;
import com.xebisco.yield.input.Keys;

import java.awt.event.KeyEvent;

class MyGame extends YldGame
{

    @Override
    public void create()
    {
        addExtension(new FullscreenDetectorExtension());
        input.addShortcut(new Keys(KeyEvent.VK_ALT, KeyEvent.VK_M), () ->
                YieldOverlay.setShow(!YieldOverlay.isShow())
        );
    }

    public static void main(String[] args)
    {
        GameConfiguration config = new GameConfiguration();
        launch(new MyGame(), config);
    }
}