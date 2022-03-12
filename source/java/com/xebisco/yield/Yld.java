/*
 * Copyright [2022] [Xebisco]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xebisco.yield;

import com.xebisco.yield.extensions.YieldOverlay;
import com.xebisco.yield.utils.YldAction;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public final class Yld
{
    public static final String VERSION = "4 1.1";
    public static final ArrayList<String> MESSAGES = new ArrayList<>();
    public static final Random RAND = new Random();
    public static boolean debug;
    public static final long BUILD = 13;
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

    public static void debug(YldAction action)
    {
        if (debug)
            action.onAction();
    }

    public static void release(YldAction action)
    {
        if (!debug)
            action.onAction();
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
