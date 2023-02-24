package com.xebisco.yield;

import java.util.concurrent.atomic.AtomicBoolean;

public class Context implements Runnable {

    private final Thread thread = new Thread(this);
    private final ContextTime contextTime = new ContextTime();
    private final AtomicBoolean running = new AtomicBoolean();
    private final Object lockObject = new Object();
    private final Runnable runnable;

    public Context(Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public void run() {
        running.set(true);
        long last = System.currentTimeMillis(), actual;
        while (running.get()) {
            actual = System.currentTimeMillis();
            //TODO delta calculation
            last = actual;
            runnable.run();

            synchronized (lockObject) {
                try {
                    lockObject.wait(contextTime.getTargetSleepTime());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
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
