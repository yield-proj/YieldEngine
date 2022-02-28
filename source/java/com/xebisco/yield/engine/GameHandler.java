package com.xebisco.yield.engine;

import com.xebisco.yield.YldExtension;
import com.xebisco.yield.YldGame;
import com.xebisco.yield.utils.YldAction;

public class GameHandler extends Engine
{
    private final YldGame game;
    private int fps;
    private Engine defaultConcurrentEngine;

    public GameHandler(YldGame game)
    {
        super(null);
        this.game = game;
        fps = game.getConfiguration().fps;
        defaultConcurrentEngine = new Engine(null);
        defaultConcurrentEngine.getThread().start();
    }

    @Override
    public void run()
    {
        setRunning(true);
        game.create();
        long start, end = System.nanoTime();
        while (isRunning())
        {
            start = System.nanoTime();
            float delta = (start - end) / 1_000_000_000f;
            if (!isIgnoreTodo())
            {
                for (int i = 0; i < getTodoList().size(); i++)
                {
                    YldEngineAction engineAction = getTodoList().get(i);
                    if (engineAction.getToExec() <= 0)
                    {
                        engineAction.getAction().onAction();
                        if (!engineAction.isRepeat())
                            getTodoList().remove(engineAction);
                        engineAction.setToExec(engineAction.getInitialToExec());
                    }
                    else
                    {
                        engineAction.setToExec(engineAction.getToExec() - (int) ((start - end) / 1_000_000));
                    }

                }
            }
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

            if (game.getConfiguration().fpsLock)
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
            getThread().join();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
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

    public Engine getDefaultConcurrentEngine()
    {
        return defaultConcurrentEngine;
    }

    public void setDefaultConcurrentEngine(Engine defaultConcurrentEngine)
    {
        this.defaultConcurrentEngine = defaultConcurrentEngine;
    }
}
