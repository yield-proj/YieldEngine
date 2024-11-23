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

package com.xebisco.yieldengine.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class represents a context that runs a specified behavior at a specified frame-rate.
 */
public class LoopContext implements Runnable {

    private final Thread thread;
    private final AtomicBoolean running = new AtomicBoolean(), paused = new AtomicBoolean();
    private final Object pauseLock = new Object();
    private Object deviceObject;
    private final List<Runnable> shutdownHooks = new ArrayList<>();

    /**
     * Constructs a new LoopContext object.
     *
     * @param name             The name of the thread.
     */
    public LoopContext(String name) {
        thread = new Thread(this, "Yield LoopContext: " + name);
    }

    @Override
    public void run() {
        running.set(true);
        long last = System.nanoTime(), actual;
        long value = 0;
        Global.getCurrentScene().getSceneController().onStart();
        Time.setTimeSinceStart(last);
        while (running.get()) {
            if (value > 0) {
                do {
                    actual = System.nanoTime();
                } while (actual - last < Time.getTargetSleepTime());
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

            Time.setDeltaTime((actual - last) / 1_000_000_000f);
            Global.getCurrentScene().getSceneController().onUpdate();
            Global.getCurrentScene().getSceneController().onLateUpdate();
            last = actual;
            actual = System.nanoTime();

            value = Time.getTargetSleepTime() - 1_000_000 - ((actual - last) / 1_000_000);
            if (value > 0) {
                try {
                    //noinspection BusyWait
                    Thread.sleep(value / 1_000_000, (int) (value - (value / 1_000_000 * 1_000_000)));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        Global.setCurrentScene(null);

        for (Runnable shutdownHook : shutdownHooks) {
            shutdownHook.run();
        }
    }

    public void startThread() {
        getThread().start();
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

    public Thread getThread() {
        return thread;
    }

    public AtomicBoolean getRunning() {
        return running;
    }

    public AtomicBoolean getPaused() {
        return paused;
    }

    public Object getPauseLock() {
        return pauseLock;
    }

    public List<Runnable> getShutdownHooks() {
        return shutdownHooks;
    }

    public Object getDeviceObject() {
        return deviceObject;
    }

    public LoopContext setDeviceObject(Object deviceObject) {
        this.deviceObject = deviceObject;
        return this;
    }
}