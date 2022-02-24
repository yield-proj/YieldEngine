package com.xebisco.yield.engine;

import com.xebisco.yield.YldExtension;
import com.xebisco.yield.YldGame;

public class GameHandler implements Runnable
{

    private final Thread gameThread = new Thread(this);
    private final YldGame game;
    private int fps;
    private boolean running;

    public GameHandler(YldGame game)
    {
        this.game = game;
        fps = game.getConfiguration().fps;
    }

    @Override
    public void run()
    {
        running = true;
        game.create();
        long start, end = System.nanoTime();
        while (running)
        {
            start = System.nanoTime();
            float delta = (start - end) / 1_000_000_000f;
            for (int i = 0; i < game.getExtensions().size(); i++)
            {
                YldExtension extension = game.getExtensions().get(i);
                extension.update(delta);
            }
            game.setFrames(game.getFrames() + 1);
            game.update(delta);
            game.process(delta);
            if (game.getScene() != null)
                game.updateScene(delta);
            game.getWindow().startGraphics();
            game.getWindow().getWindowG().repaint();
            if (game.getInput() != null)
                game.getInput().setClicking(false);
            end = System.nanoTime();

            if (!game.getConfiguration().disableFps)
            {
                try
                {
                    Thread.sleep(1000 / fps);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }
        try
        {
            gameThread.join();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    public boolean isRunning()
    {
        return running;
    }

    public void setRunning(boolean running)
    {
        this.running = running;
    }

    public Thread getGameThread()
    {
        return gameThread;
    }

    public YldGame getGame()
    {
        return game;
    }

    public int getFps()
    {
        return fps;
    }

    public void setFps(int fps)
    {
        this.fps = fps;
    }
}
