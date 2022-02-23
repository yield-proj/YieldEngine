package com.xebisco.yield;

import com.xebisco.yield.engine.GameHandler;

public class YldTime {
    private float delta, fps;
    private GameHandler gameHandler;

    public YldTime(GameHandler gameHandler) {
        this.gameHandler = gameHandler;
    }

    public float getDelta() {
        return delta;
    }

    public float getFps() {
        return fps;
    }

    public float getTargetFPS() {
        return gameHandler.getFps();
    }

    public void setTargetFPS(float fps) {
        gameHandler.setFps((int) fps);
    }

    public GameHandler getGameHandler() {
        return gameHandler;
    }

    public void setGameHandler(GameHandler gameHandler) {
        this.gameHandler = gameHandler;
    }

    public void setDelta(float delta)
    {
        this.delta = delta;
    }

    public void setFps(float fps)
    {
        this.fps = fps;
    }
}
