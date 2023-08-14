/*
 * Copyright [2022-2023] [Xebisco]
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

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * It's a class that runs a runnable at a specified frame-rate
 */
public class Context implements Runnable {

    private final Thread thread;
    private final ContextTime contextTime;
    private final AtomicBoolean running = new AtomicBoolean();
    private final Object lockObject = new Object();
    private final Runnable runnable;
    private final Disposable disposable;
    private boolean lightweight;

    public Context(ContextTime contextTime, Runnable runnable, Disposable disposable, String name) {
        this.contextTime = contextTime;
        this.runnable = runnable;
        this.disposable = disposable;
        thread = new Thread(this, "Yield Context: " + name);
    }

    @Override
    public void run() {
        running.set(true);
        long last = System.nanoTime(), actual;
        long value = 0;
        while (running.get()) {
            if (!lightweight && value > 0) {
                do {
                    actual = System.nanoTime();
                } while (actual - last < contextTime.targetSleepTime() * 1_000);
            } else {
                actual = System.nanoTime();
            }
            contextTime.setDeltaTime((actual - last) / 1_000_000_000.0);
            if (runnable != null)
                runnable.run();
            last = actual;
            actual = System.nanoTime();

            value = contextTime.targetSleepTime() - 1000 - ((actual - last) / 1_000);
            if (lightweight) value++;
            if (value > 0)
                synchronized (lockObject) {
                    try {
                        lockObject.wait(value / 1000, (int) (value - value / 1000));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
        }
        if (disposable != null)
            disposable.dispose();
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
     * This function returns the lock object.
     *
     * @return The lock object.
     */
    public Object lockObject() {
        return lockObject;
    }

    /**
     * This function returns a Runnable object.
     *
     * @return The runnable object.
     */
    public Runnable runnable() {
        return runnable;
    }

    /**
     * Returns true if this component is lightweight.
     *
     * @return The boolean value of the variable lightweight.
     */
    public boolean lightweight() {
        return lightweight;
    }

    /**
     * Sets the lightweight property of the component.
     *
     * @param lightweight If true, the component will behavior as a lightweight context.
     */
    public Context setLightweight(boolean lightweight) {
        this.lightweight = lightweight;
        return this;
    }

    /**
     * This function returns the disposable object that was passed in the constructor.
     *
     * @return The disposable object.
     */
    public Disposable disposable() {
        return disposable;
    }
}
