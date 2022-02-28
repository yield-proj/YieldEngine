package com.xebisco.yield.engine;

import com.xebisco.yield.utils.YldAction;

public class YldEngineAction
{
    private final YldAction action;
    private int toExec;
    private final int initialToExec;
    private final boolean repeat;

    public YldEngineAction(YldAction action, int toExec, boolean repeat)
    {
        this.initialToExec = toExec;
        this.toExec = initialToExec;
        this.action = action;
        this.repeat = repeat;
    }

    public YldAction getAction()
    {
        return action;
    }

    public int getToExec()
    {
        return toExec;
    }

    public void setToExec(int toExec)
    {
        this.toExec = toExec;
    }

    public int getInitialToExec()
    {
        return initialToExec;
    }

    public boolean isRepeat()
    {
        return repeat;
    }
}
