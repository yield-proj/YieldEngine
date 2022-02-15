package com.xebisco.yield.test;

import com.xebisco.yield.*;
import com.xebisco.yield.components.Circle;

class MyGame extends YldGame {
    @Override
    public void create() {



        Entity entity = instantiate();
        entity.addComponent(new Circle());
        entity.setIndex(0);
        Entity entity2 = instantiate();
        entity2.setIndex(1);
        entity.addComponent(new Circle());




    }

    public static void main(String[] args) {
        new View(427, 240);
        GameConfiguration config = new GameConfiguration();
        launch(new MyGame(), config);
    }
}