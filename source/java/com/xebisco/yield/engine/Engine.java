package com.xebisco.yield.engine;

import com.xebisco.yield.utils.YldAction;

import java.util.ArrayList;
import java.util.HashSet;

public class Engine implements Runnable
{
    private final Thread thread = new Thread(this);
    private final EngineController controller;
    private final ArrayList<YldEngineAction> todoList = new ArrayList<>();
    private int targetTime = 33;
    private EngineStop stop = EngineStop.NONE;
    private boolean running, ignoreTodo;

    public Engine(EngineController controller)
    {
        this.controller = controller;
    }

    @Override
    public void run()
    {
        running = true;
        if (controller != null)
            controller.start();
        while (running)
        {
            if (!ignoreTodo)
            {
                for (int i = 0; i < todoList.size(); i++)
                {
                    YldEngineAction engineAction = todoList.get(i);
                    if (engineAction.getToExec() <= 0)
                    {
                        engineAction.getAction().onAction();
                        if (!engineAction.isRepeat())
                            todoList.remove(engineAction);
                        engineAction.setToExec(engineAction.getInitialToExec());
                    }
                    else
                    {
                        engineAction.setToExec(engineAction.getToExec() - targetTime);
                    }

                }
            }
            if (controller != null)
                controller.tick();
            try
            {
                Thread.sleep(targetTime);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        if (stop == EngineStop.INTERRUPT_ON_END)
        {
            thread.interrupt();
        }
        else if (stop == EngineStop.JOIN_ON_END)
        {
            try
            {
                thread.join();
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
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

    public Thread getThread()
    {
        return thread;
    }

    public EngineController getController()
    {
        return controller;
    }

    public ArrayList<YldEngineAction> getTodoList()
    {
        return todoList;
    }

    public int getTargetTime()
    {
        return targetTime;
    }

    public void setTargetTime(int targetTime)
    {
        this.targetTime = targetTime;
    }

    public boolean isIgnoreTodo()
    {
        return ignoreTodo;
    }

    public void setIgnoreTodo(boolean ignoreTodo)
    {
        this.ignoreTodo = ignoreTodo;
    }

    public EngineStop getStop()
    {
        return stop;
    }

    public void setStop(EngineStop stop)
    {
        this.stop = stop;
    }
}
