package com.xebisco.yield.test;

import com.xebisco.yield.Obj;
import com.xebisco.yield.YldCamera;
import com.xebisco.yield.YldGame;
import com.xebisco.yield.config.GameConfiguration;
import com.xebisco.yield.extensions.FullscreenDetectorExtension;

public class MyGame extends YldGame {

    Obj image;
    YldCamera camera = new YldCamera(1280, 720);
    float xSpeed = 140, ySpeed = 140;

    @Override
    public void create() {
        image = graphics.img("/com/xebisco/yield/assets/yieldlogo.png", 0, 0);
    }

    @Override
    public void update(float delta) {
        image.x += xSpeed * delta;
        image.y += ySpeed * delta;
        if (image.x < 0 || image.x + image.x2 > camera.getWidth()) {
            xSpeed = -xSpeed;
        }
        if (image.y < 0 || image.y + image.y2 > camera.getHeight()) {
            ySpeed = -ySpeed;
        }
    }

    public static void main(String[] args) {
        GameConfiguration config = new GameConfiguration();
        YldGame game = new MyGame();
        game.addExtension(new FullscreenDetectorExtension());
        launch(game, config);
    }

}
