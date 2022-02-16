package com.xebisco.yield.test;

import com.xebisco.yield.*;
import com.xebisco.yield.components.Circle;
import com.xebisco.yield.components.RectCollider;
import com.xebisco.yield.components.Rectangle;
import com.xebisco.yield.components.Rigidbody;

class MyGame extends YldGame {
    @Override
    public void create() {
        Entity e = instantiate();
        e.addComponent(new Circle());
        e.getSelfTransform().scale(-.8f, -.8f);
        e.getSelfTransform().translate(120, 0);
        e.addComponent(new Rigidbody());
        e.addComponent(new RectCollider());
        e.setIndex(1);
        Entity e2 = instantiate();
        e2.addComponent(new RectCollider());
        Rectangle rectangle = new Rectangle();
        e2.addComponent(rectangle);
        e2.setIndex(0);
        rectangle.setColor(Colors.RED);
        e2.getSelfTransform().translate(100, 100);
    }

    public static void main(String[] args) {
        new View(427, 240);
        GameConfiguration config = new GameConfiguration();
        launch(new MyGame(), config);
    }
}