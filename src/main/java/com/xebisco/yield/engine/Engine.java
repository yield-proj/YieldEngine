package com.xebisco.yield.engine;

import com.xebisco.yield.utils.YldAction;

import java.util.ArrayList;
import java.util.HashSet;

public class Engine implements Runnable
{
    private final Thread thread = new Thread(this);
    private final EngineController controller;
    private final ArrayList<YldAction> todoList = new ArrayList<>();
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
                    YldAction action = todoList.get(i);
                    action.onAction();
                    todoList.remove(action);
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

    public ArrayList<YldAction> getTodoList()
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
