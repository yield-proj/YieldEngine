package com.xebisco.yield;

import com.xebisco.yield.engine.Engine;
import com.xebisco.yield.engine.EngineStop;
import com.xebisco.yield.engine.YldEngineAction;
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
            game.getHandler().getDefaultConcurrentEngine().getTodoList().add(new YldEngineAction(action, 0, false));
        }
        else if (multiThread == MultiThread.ON_GAME_THREAD)
        {
            game.getHandler().getTodoList().add(new YldEngineAction(action, 0, false));
        }
        else if (multiThread == MultiThread.EXCLUSIVE)
        {
            Engine engine = new Engine(null);
            engine.getThread().start();
            engine.getTodoList().add(new YldEngineAction(action, 0, false));
            YldEngineAction action1 = new YldEngineAction(() ->
            {
                engine.setStop(EngineStop.INTERRUPT_ON_END);
                engine.setRunning(false);
            }, 0, false);
            engine.getTodoList().add(action1);
        }
    }

    public final void timer(YldAction action, float time, boolean repeat, MultiThread multiThread)
    {
        if (multiThread == MultiThread.DEFAULT)
        {
            game.getHandler().getDefaultConcurrentEngine().getTodoList().add(new YldEngineAction(action, (int) (time * 1000), repeat));
        }
        else if (multiThread == MultiThread.ON_GAME_THREAD)
        {
            game.getHandler().getTodoList().add(new YldEngineAction(action, (int) (time * 1000), repeat));
        }
        else if (multiThread == MultiThread.EXCLUSIVE)
        {
            Engine engine = new Engine(null);
            engine.getThread().start();
            engine.getTodoList().add(new YldEngineAction(action, (int) (time * 1000), repeat));
            YldEngineAction action1 = new YldEngineAction(() ->
            {
                engine.setStop(EngineStop.INTERRUPT_ON_END);
                engine.setRunning(false);
            }, 0, false);
            engine.getTodoList().add(action1);
        }
    }

    public final void timer(YldAction action, float time, boolean repeat) {
        timer(action, time, repeat, MultiThread.ON_GAME_THREAD);
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
