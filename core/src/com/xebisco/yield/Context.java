/*
 * Copyright [2022-2023] [Xebisco]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.xebisco.yield;

import java.util.concurrent.atomic.AtomicBoolean;

public class Context implements Runnable {

    private final Thread thread = new Thread(this);
    private final ContextTime contextTime;
    private final AtomicBoolean running = new AtomicBoolean();
    private final Object lockObject = new Object();
    private final Runnable runnable;
    private final Disposable disposable;
    private boolean lightweight;

    public Context(ContextTime contextTime, Runnable runnable, Disposable disposable) {
        this.contextTime = contextTime;
        this.runnable = runnable;
        this.disposable = disposable;
    }

    @Override
    public void run() {
        running.set(true);
        long last = System.nanoTime(), actual;
        while (running.get()) {
            if (!lightweight) {
                do {
                    actual = System.nanoTime();
                } while (actual - last < contextTime.getTargetSleepTime() * 1_000);
            } else {
                actual = System.nanoTime();
            }
            contextTime.setDeltaTime((actual - last) / 1_000_000_000.0);
            if (runnable != null)
                runnable.run();
            last = actual;
            actual = System.nanoTime();

            long value = contextTime.getTargetSleepTime() - 1000 - ((actual - last) / 1_000);
            if (lightweight) value++;
            value = Global.clamp(value, 0, value);
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

    public Thread getThread() {
        return thread;
    }

    public ContextTime getContextTime() {
        return contextTime;
    }

    public AtomicBoolean getRunning() {
        return running;
    }

    public Object getLockObject() {
        return lockObject;
    }

    public Runnable getRunnable() {
        return runnable;
    }

    public boolean isLightweight() {
        return lightweight;
    }

    public void setLightweight(boolean lightweight) {
        this.lightweight = lightweight;
    }

    public Disposable getDisposable() {
        return disposable;
    }
}
