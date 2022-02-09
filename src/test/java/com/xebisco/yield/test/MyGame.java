package com.xebisco.yield.test;

import com.xebisco.yield.Entity;
import com.xebisco.yield.View;
import com.xebisco.yield.YldGame;
import com.xebisco.yield.config.GameConfiguration;
import com.xebisco.yield.extensions.FullscreenDetectorExtension;
import com.xebisco.yield.graphics.Material;
import com.xebisco.yield.physics.components.RectCollider;
import com.xebisco.yield.physics.components.Rigidbody;
import com.xebisco.yield.utils.Vector2;

import java.awt.*;
import java.awt.event.KeyEvent;

class MyGame extends YldGame {
    Rigidbody rigidbody;

    @Override
    public void create() {
        addExtension(new FullscreenDetectorExtension());
        new View(427, 240);
        Entity cube = instantiate("cube", new Material(Color.BLUE));
        cube.getRenderer().getObj().index = 100;
        rigidbody = new Rigidbody();
        rigidbody.gravity = new Vector2(0, -10f);
        cube.getTransform().translate(0, 240);
        cube.addComponent(rigidbody);
        cube.addComponent(new RectCollider());
        Entity cube2 = instantiate("cube2");
        cube2.getTransform().translate(0, 100);
        cube2.addComponent(new RectCollider());
    }

    @Override
    public void update(float delta) {
        if(input.justPressed(KeyEvent.VK_RIGHT)) {
            rigidbody.addForce(new Vector2(1000, 0));
        }
        if(input.justPressed(KeyEvent.VK_LEFT)) {
            rigidbody.addForce(new Vector2(-1000, 0));
        }
    }

    public static void main(String[] args) {
        GameConfiguration config = new GameConfiguration();
        launch(new MyGame(), config);
    }
}