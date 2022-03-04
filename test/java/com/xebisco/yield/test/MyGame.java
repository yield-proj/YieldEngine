package com.xebisco.yield.test;

import com.xebisco.yield.*;
import com.xebisco.yield.components.Rectangle;
import com.xebisco.yield.components.Sprite;
import com.xebisco.yield.components.Square;
import com.xebisco.yield.extensions.FullscreenDetectorExtension;
import com.xebisco.yield.slick.SlickTexture;
import org.newdawn.slick.Input;

import java.awt.event.KeyEvent;

public class MyGame extends YldGame
{
    Entity e;

    @Override
    public void create()
    {
        Yld.message("Hello, World!");
        addExtension(new FullscreenDetectorExtension());
        e = instantiate((e) ->
        {
            e.addComponent(new Sprite());
            e.getMaterial().setTexture(new SlickTexture("/com/xebisco/yield/assets/yieldlogo.png"));
            e.addComponent(new C());
        });
        instantiate((e) ->
        {
            e.addComponent(new Rectangle());
            e.addComponent(new C());
            e.getComponent(Rectangle.class).setColor(Colors.WHITE);
            e.getSelfTransform().translate(200, 200);
        });
    }

    @Override
    public void update(float delta)
    {
        if (input.isPressing(Key.NUMBER_1))
        {
            Yld.log("test");
        }
    }

    public static void main(String[] args)
    {
        final GameConfiguration config = new GameConfiguration();
        config.hardwareAcceleration = true;
        config.resizable = true;
        launch(new MyGame(), config);
    }
}

class C extends Component
{

    @Override
    public void start()
    {
        new View(1280, 720);
    }

    @Override
    public void update(float delta)
    {
        transform.translate(.1f, .1f);
        transform.rotate(1);
    }
}