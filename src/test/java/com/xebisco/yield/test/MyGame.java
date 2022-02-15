package com.xebisco.yield.test;

import com.xebisco.yield.*;

class MyGame extends YldGame {
    @Override
    public void create() {
        addScene(new GameplayScene());
        setScene("GameplayScene");
    }

    public static void main(String[] args) {
        new View(427, 240);
        GameConfiguration config = new GameConfiguration();
        launch(new MyGame(), config);
    }
}

class GameplayScene extends YldScene {
    private final Color color = Colors.BLACK;
    Obj rect;

    @Override
    public void start() {
        color.setA(1);
        graphics.setColor(color);
        rect = graphics.rect(0, 0, View.getActView().getWidth(), View.getActView().getHeight());
    }

    @Override
    public void update(float delta) {
        color.setA(color.getA() - delta);
        if (color.getA() < 0)
            color.setA(0);
        if (color.getA() == 0)
            graphics.shapeRends.remove(rect);
    }

    public Color getColor() {
        return color;
    }
}