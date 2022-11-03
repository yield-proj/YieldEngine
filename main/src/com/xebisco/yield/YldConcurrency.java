package com.xebisco.yield;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public final class YldConcurrency {

    private static boolean interruptOnEnd = true;

    public static void runTask(YldTask task) {
        final YldTaskExecutor taskExecutor = new YldTaskExecutor(task, interruptOnEnd, null);
        final Thread thread = new Thread(taskExecutor);
        taskExecutor.setThread(thread);
        final String taskName = task.toString();
        Yld.getDebugLogger().log("YldConcurrency: Started '" + taskName + "' task.");
        thread.start();
    }

    public static void timedTask(YldTask task, float time, long doTimes) {
        AtomicLong atomicRepeat = new AtomicLong(doTimes);
        final YldTask task1 = () -> {
            while (atomicRepeat.get() > 0) {
                atomicRepeat.set(atomicRepeat.get() - 1);
                try {
                    Thread.sleep((long) (time * 1000f));
                } catch (InterruptedException e) {
                    Yld.throwException(e);
                }
                task.execute();
            }
        };
        final YldTaskExecutor taskExecutor = new YldTaskExecutor(task1, interruptOnEnd, null);
        final Thread thread = new Thread(taskExecutor);
        taskExecutor.setThread(thread);
        final String taskName = task.toString();
        Yld.getDebugLogger().log("YldConcurrency: Started '" + taskName + "' timed task.");
        thread.start();
    }

    public static void timedTask(YldTask task, float time) {
        timedTask(task, time, false);
    }

    public static void timedTask(YldTask task, float time, boolean repeat) {
        if (repeat)
            timedTask(task, time, Long.MAX_VALUE);
        else timedTask(task, time, 1);
    }

    public static boolean isInterruptOnEnd() {
        return interruptOnEnd;
    }

    public static void setInterruptOnEnd(boolean interruptOnEnd) {
        YldConcurrency.interruptOnEnd = interruptOnEnd;
    }
}
