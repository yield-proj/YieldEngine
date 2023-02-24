package com.xebisco.yield;

import java.util.concurrent.atomic.AtomicBoolean;

public class Context implements Runnable {

    private final Thread thread = new Thread(this);
    private final ContextTime contextTime;
    private final AtomicBoolean running = new AtomicBoolean();
    private final Object lockObject = new Object();
    private final Runnable runnable;

    public Context(ContextTime contextTime, Runnable runnable) {
        this.contextTime = contextTime;
        this.runnable = runnable;
    }

    @Override
    public void run() {
        running.set(true);
        long last = System.nanoTime(), actual = 0;
        while (running.get()) {
            synchronized (lockObject) {
                try {
                    lockObject.wait(Math.abs(contextTime.getTargetSleepTime() - 1));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            while (actual - last < contextTime.getTargetSleepTime() * 1_000_000) {
                actual = System.nanoTime();
            }

            contextTime.setDeltaTime((actual - last) / 1_000_000_000.0 * contextTime.getTimeScale());
            last = actual;
            runnable.run();
        }
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
}
