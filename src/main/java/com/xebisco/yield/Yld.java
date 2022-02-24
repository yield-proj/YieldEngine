package com.xebisco.yield;

import com.xebisco.yield.extensions.YieldOverlay;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public final class Yld
{
    public static final String VERSION = "4 beta6";
    public static final ArrayList<String> MESSAGES = new ArrayList<>();
    public static final Random RAND = new Random();
    public static final long BUILD = 3;
    public static final int MEMORY = (int) ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024 / 1024), MAX_MEMORY = (int) (Runtime.getRuntime().maxMemory() / 1024 / 1024);

    public static void message(Object msg)
    {
        YieldOverlay.setShow(true);
        msg = "(" + new Date() + ") " + msg;
        MESSAGES.add(0, msg.toString());
        if (Yld.MESSAGES.size() > 9)
        {
            Yld.MESSAGES.remove(Yld.MESSAGES.size() - 1);
        }
        System.out.println(msg);
    }

    public static void log(Object msg)
    {
        System.out.println(msg);
    }

    public static void exit()
    {
        System.exit(0);
    }
}
