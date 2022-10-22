package com.xebisco.yield;

public class YldTaskExecutor implements Runnable {

    private final YldTask task;
    private final boolean interruptOnEnd;
    private boolean finished;
    private Thread thread;

    public YldTaskExecutor(final YldTask task, final boolean interruptOnEnd, final Thread thread) {
        this.task = task;
        this.interruptOnEnd = interruptOnEnd;
        this.thread = thread;
    }

    @Override
    public void run() {
        task.execute();
        finished = true;
        if (thread != null)
            if (interruptOnEnd)
                thread.interrupt();
            else
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
    }

    public YldTask getTask() {
        return task;
    }

    public boolean isInterruptOnEnd() {
        return interruptOnEnd;
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public boolean isFinished() {
        return finished;
    }
}
