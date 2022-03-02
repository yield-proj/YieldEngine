package com.xebisco.yield;

import com.xebisco.yield.engine.GameHandler;

public class YldTime
{
    private float delta, fps;
    private YldGame game;

    public YldTime(YldGame game)
    {
        this.game = game;
    }

    public float getDelta()
    {
        return delta;
    }

    public float getFps()
    {
        return fps;
    }

    public float getTargetFPS()
    {
        return game.getHandler().getFps();
    }

    public void setTargetFPS(float fps)
    {
        game.getHandler().setFps((int) fps);
    }

    public void setTargetDrawFPS(float fps)
    {
        if (game.getSlickApp() != null)
            game.getSlickApp().setTargetFrameRate((int) fps);
        else
            game.getHandler().setFps((int) fps);
    }

    public YldGame getGame()
    {
        return game;
    }

    public void setGame(YldGame game)
    {
        this.game = game;
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
