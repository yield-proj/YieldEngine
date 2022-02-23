package com.xebisco.yield.test;

import com.xebisco.yield.*;
import com.xebisco.yield.components.Animator;
import com.xebisco.yield.components.Sprite;

class MyGame extends YldGame
{

    @Override
    public void create()
    {
        Entity e = instantiate();
        e.addComponent(new Sprite());
        e.addComponent(new Animator());
        e.getSelfTransform().translate(100, 100);
        Animator animator = e.getComponent(Animator.class);
        Texture mainTexture = new Texture("/com/xebisco/yield/test/assets/ship.png");
        Animation animation = new Animation(new Texture(mainTexture, 0, 0, 96 / 2, 48), new Texture(mainTexture, 96 / 2, 0, 96 / 2, 48));
        animation.setMicrosecondDelay(100);
        animator.setAnimation(animation);
    }

    public static void main(String[] args)
    {
        new View(427, 240);
        GameConfiguration config = new GameConfiguration();
        launch(new MyGame(), config);
    }
}