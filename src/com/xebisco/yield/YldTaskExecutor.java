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
