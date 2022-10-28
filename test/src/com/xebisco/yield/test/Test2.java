package com.xebisco.yield.test;

import com.xebisco.yield.*;

import java.util.Arrays;

public class Test2 extends YldGame {

    @Override
    public void start() {
        graphics.img(getAssets().getTexture("yield.png")).center();
        addScene(new Test());
        addScene(new Loading());

    }

    @Override
    public void update(float delta) {
        if (getFrames() == 100) {
            setScene(Test.class, Loading.class);
        }
    }

    public static void main(String[] args) {
        GameConfiguration config = new GameConfiguration();
        Ini.file(new RelativeFile("game.ini"), config);
        launch(new Test2(), config);
    }
}

class Loading extends YldProgressScene {

    Rectangle t;
    float w;

    @Override
    public void create() {
        instantiate(e -> {
            e.addComponent(t = new Rectangle());
            t.setSize(new Vector2(0, 64));
            e.center();
        });
    }

    @Override
    public void update(float delta) {
        w = getProgress() * getView().getWidth();
        t.getSize().x += (w - t.getSize().x) / 8f;
    }
}

class Test extends YldScene {

    @Override
    public void start() {
        graphics.img(getAssets().getTexture("img.png")).center();
        graphics.img(getAssets().getTexture("yield.png")).center();
        /*log(Arrays.toString(getAssets().getTextFile("test2.txt").getContents()));
        log(Arrays.toString(getAssets().getTextFile("aa/test.txt").getContents()));
        log(Arrays.toString(getAssets().getTextFile("test3.txt").getContents()));*/
    }
}