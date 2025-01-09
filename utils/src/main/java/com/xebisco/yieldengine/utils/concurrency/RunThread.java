package com.xebisco.yieldengine.utils.concurrency;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class RunThread {
    private final Thread thread;
    private final LockProcess runLock = new LockProcess();
    private final Map<IRunnableWithReturnValue<?>, Object> returnObjects = new HashMap<>();
    private Queue<RunnableLock> runQueue = new LinkedList<>();

    private static final class RunnableLock {
        private final IRunnableWithReturnValue<?> runnable;
        private final LockProcess lock;

        public RunnableLock(IRunnableWithReturnValue<?> runnable, LockProcess lock) {
            this.runnable = runnable;
            this.lock = lock;
        }

        public IRunnableWithReturnValue<?> getRunnable() {
            return runnable;
        }

        public LockProcess getLock() {
            return lock;
        }
    }

    public RunThread() {
        thread = new Thread(() -> {
            while (runQueue != null) {
                runLock.getRunning().set(true);
                RunnableLock runnableLock;
                while ((runnableLock = runQueue.poll()) != null) {
                    Object ret = runnableLock.getRunnable().apply();
                    if (ret != null)
                        returnObjects.put(runnableLock.getRunnable(), ret);
                    if (runnableLock.getLock() != null)
                        runnableLock.getLock().unlock();
                }
                try {
                    runLock.aWait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "YIELD-RunThread");
    }

    public void start() {
        thread.start();
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        runLock.unlock();
    }

    public <R> R run(IRunnableWithReturnValue<R> runnable) {
        LockProcess lock = new LockProcess();
        runQueue.add(new RunnableLock(runnable, lock));
        runLock.unlock();
        try {
            lock.aWait();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //noinspection unchecked
        R ret = (R) returnObjects.get(runnable);
        returnObjects.remove(runnable);
        return ret;
    }

    public void runIgnore(Runnable runnable) {
        runQueue.add(new RunnableLock(() -> {
            runnable.run();
            return null;
        }, null));
        runLock.unlock();
    }

    public void interrupt() {
        runQueue = null;
        runLock.unlock();
    }

    public Thread getThread() {
        return thread;
    }

    public LockProcess getRunLock() {
        return runLock;
    }

    public Map<IRunnableWithReturnValue<?>, Object> getReturnObjects() {
        return returnObjects;
    }

    public Queue<RunnableLock> getRunQueue() {
        return runQueue;
    }

    public RunThread setRunQueue(Queue<RunnableLock> runQueue) {
        this.runQueue = runQueue;
        return this;
    }
}
