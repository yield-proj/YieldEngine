/*
 * Copyright [2022] [Xebisco]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xebisco.yield;

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
