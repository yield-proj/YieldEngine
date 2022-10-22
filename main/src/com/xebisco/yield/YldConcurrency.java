package com.xebisco.yield;

public final class YldConcurrency {

    private static boolean interruptOnEnd = true;

    public static void runTask(YldTask task) {
        final YldTaskExecutor taskExecutor = new YldTaskExecutor(task, interruptOnEnd, null);
        final Thread thread = new Thread(taskExecutor);
        taskExecutor.setThread(thread);
        String taskName = task.toString();
        Yld.getDebugLogger().log("YldConcurrency: Started '" + taskName + "' task.");
        thread.start();
    }

    public static boolean isInterruptOnEnd() {
        return interruptOnEnd;
    }

    public static void setInterruptOnEnd(boolean interruptOnEnd) {
        YldConcurrency.interruptOnEnd = interruptOnEnd;
    }
}
