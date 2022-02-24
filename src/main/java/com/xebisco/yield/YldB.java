package com.xebisco.yield;

import com.xebisco.yield.engine.Engine;
import com.xebisco.yield.engine.EngineStop;
import com.xebisco.yield.utils.MultiThread;
import com.xebisco.yield.utils.YldAction;

public abstract class YldB
{

    protected YldGame game;

    public abstract void create();

    public abstract void update(float delta);

    public abstract void onDestroy();

    public final void concurrent(YldAction action, MultiThread multiThread)
    {
        if (multiThread == MultiThread.DEFAULT)
        {
            game.getHandler().getDefaultConcurrentEngine().getTodoList().add(action);
        }
        else if (multiThread == MultiThread.ON_GAME_THREAD)
        {
            game.getHandler().getTodoList().add(action);
        }
        else if (multiThread == MultiThread.EXCLUSIVE)
        {
            Engine engine = new Engine(null);
            engine.getThread().start();
            engine.getTodoList().add(action);
            engine.getTodoList().add(() ->
            {
                engine.setStop(EngineStop.INTERRUPT_ON_END);
                engine.setRunning(false);
            });
        }
    }

    public final void concurrent(YldAction action)
    {
        concurrent(action, MultiThread.DEFAULT);
    }

    public YldGame getGame()
    {
        return game;
    }

    public void setGame(YldGame game)
    {
        this.game = game;
    }
}
