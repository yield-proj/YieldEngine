package com.xebisco.yield.test;

import com.xebisco.yield.*;
import com.xebisco.yield.components.Rectangle;
import com.xebisco.yield.components.Sprite;
import com.xebisco.yield.components.Square;

public class MyGame extends YldGame
{
    @Override
    public void create()
    {
        instantiate((e) -> {
            e.addComponent(new Rectangle());
            e.addComponent(new C());
        });
        instantiate((e) -> {
            e.addComponent(new Rectangle());
            e.getComponent(Rectangle.class).setColor(Colors.WHITE);
            e.getSelfTransform().translate(200, 200);
        });
    }

    public static void main(String[] args)
    {
        final GameConfiguration config = new GameConfiguration();
        launch(new MyGame(), config);
    }
}

class C extends Component {

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