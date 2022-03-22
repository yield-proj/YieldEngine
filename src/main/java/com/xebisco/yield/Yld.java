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

/**
 * A class that hold global properties and methods of Yield Game Engine
 * @since 4_alpha1
 * @author Xebisco
 */
public final class Yld
{
    /**
     * The version of the Yield Game Engine.
     */
    public static final String VERSION = "4 1.1.3";
    /**
     * All the Yield Game Engine messages.
     */
    public static final ArrayList<String> MESSAGES = new ArrayList<>();
    /**
     * The standard Random library instance.
     */
    public static final Random RAND = new Random();
    /**
     * If Yield Game Engine is in debug mode ot not.
     */
    public static boolean debug;

    /**
     * The memory in use in the actual Java Virtual Machine.
     * @return The memory in use.
     */
    public static int MEMORY()
    {
        return (int) ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024 / 1024);
    }

    /**
     * The free memory of the actual Java Virtual Machine.
     * @return The free memory.
     */
    public static int MAX_MEMORY()
    {
        return (int) (Runtime.getRuntime().maxMemory() / 1024 / 1024);
    }

    /**
     * Adds a message to the messages list, and logs to the standard output.
     * @param msg The message to be added.
     */
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

    /**
     * Executes an instance of a YldAction if the debug variable is set to TRUE.
     * @param action The action to be performed.
     */
    public static void debug(YldAction action)
    {
        if (debug)
        {
            action.onAction();
        }
    }

    /**
     * Executes an instance of a YldAction if the debug variable is set to FALSE.
     * @param action The action to be performed.
     */
    public static void release(YldAction action)
    {
        if (!debug)
            action.onAction();
    }

    /**
     * Logs a Object to the standard output.
     * @param msg The message to be logged.
     */
    public static void log(Object msg)
    {
        System.out.println(msg);
    }

    /**
     * Closes the Java Virtual Machine.
     */
    public static void exit()
    {
        System.exit(0);
    }
}
