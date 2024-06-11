/*
 * Copyright [2022-2024] [Xebisco]
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

package com.xebisco.yieldengine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class represents a context that runs a specified behavior at a specified frame-rate.
 */
public class Context implements Runnable {

    private final Thread thread;
    private final ContextTime contextTime;
    private final AtomicBoolean running = new AtomicBoolean(), paused = new AtomicBoolean();
    private final AbstractBehavior abstractBehavior;
    private final Object pauseLock = new Object();
    private final List<Runnable> shutdownHooks = new ArrayList<>();

    /**
     * Constructs a new Context object.
     *
     * @param contextTime      The context time object.
     * @param abstractBehavior The behavior to be run.
     * @param name             The name of the thread.
     */
    public Context(ContextTime contextTime, AbstractBehavior abstractBehavior, String name) {
        this.contextTime = contextTime;
        this.abstractBehavior = abstractBehavior;
        thread = new Thread(this, "Yield Context: " + name);
    }

    @Override
    public void run() {
        running.set(true);
        long last = System.nanoTime(), actual;
        long start = last;
        long value = 0;
        while (running.get()) {
            if (value > 0) {
                do {
                    actual = System.nanoTime();
                } while (actual - last < contextTime.targetSleepTime());
            } else {
                actual = System.nanoTime();
            }

            if (paused.get()) {
                paused.set(false);
                synchronized (pauseLock) {
                    try {
                        pauseLock.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            contextTime.setTimeSinceStart(actual - start);
            contextTime.setDeltaTime((actual - last) / 1_000_000_000f);
            abstractBehavior.onUpdate(contextTime);
            last = actual;
            actual = System.nanoTime();

            value = contextTime.targetSleepTime() - 1_000_000 - ((actual - last) / 1_000_000);
            if (value > 0) {
                try {
                    //noinspection BusyWait
                    Thread.sleep(value / 1_000_000, (int) (value - (value / 1_000_000 * 1_000_000)));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        for (Runnable shutdownHook : shutdownHooks) {
            shutdownHook.run();
        }
        try {
            abstractBehavior.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void pause() {
        paused.set(true);
    }

    public void resume() {
        paused.set(false);
        synchronized (pauseLock) {
            pauseLock.notify();
        }
    }

    public List<Runnable> shutdownHooks() {
        return shutdownHooks;
    }

    /**
     * Returns the thread that is running this context.
     *
     * @return The thread object.
     */
    public Thread thread() {
        return thread;
    }

    /**
     * This function returns the context time.
     *
     * @return The contextTime object.
     */
    public ContextTime contextTime() {
        return contextTime;
    }

    /**
     * This function returns the value of the running variable.
     *
     * @return The value of the running variable.
     */
    public AtomicBoolean running() {
        return running;
    }

    /**
     * Returns the abstract behavior associated with this context.
     *
     * @return The abstract behavior.
     */
    public AbstractBehavior abstractBehavior() {
        return abstractBehavior;
    }
}
