/*
 * Copyright [2022] [Xebisco]
 *
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

package com.xebisco.yield.engine;

import com.xebisco.yield.Yld;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class is used to create a game loop.
 */
public class Engine implements Runnable {
    private final Thread thread = new Thread(this);
    private final EngineController controller;
    private final ArrayList<YldEngineAction> todoList = new ArrayList<>();
    private int targetTime = 33, oneSecCount, oneSecFrameCount, fpsCount;
    private EngineStop stop = EngineStop.JOIN_ON_END;
    private boolean running, ignoreTodo, lock = true, stopOnNext;

    private AtomicBoolean paused = new AtomicBoolean(false);

    private long last, actual, skipped;

    public Engine(EngineController controller) {
        this.controller = controller;
    }

    @Override
    public void run() {
        running = true;
        if (controller != null)
            controller.start();
        while (running) {
            if (!paused.get()) {
                actual = System.currentTimeMillis();
                skipped = System.currentTimeMillis();
                if (!ignoreTodo) {
                    for (int i = 0; i < todoList.size(); i++) {
                        YldEngineAction engineAction = todoList.get(i);
                        if (engineAction != null) {
                            if (engineAction.getToExec() <= 0) {
                                engineAction.getAction().onAction();
                                engineAction.setToExec(engineAction.getInitialToExec());
                            } else {
                                engineAction.setToExec(engineAction.getToExec() - targetTime);
                            }
                        }

                    }
                    for (int i = 0; i < todoList.size(); i++) {
                        YldEngineAction engineAction = todoList.get(i);
                        if (engineAction != null) {
                            if (!engineAction.isRepeat())
                                todoList.remove(engineAction);
                        }
                    }
                }
                if (controller != null)
                    controller.tick();
                update(last, actual);
                oneSecCount += actual - last;
                oneSecFrameCount++;
                if (oneSecCount > 1000) {
                    fpsCount = oneSecFrameCount;
                    oneSecCount = 0;
                    oneSecFrameCount = 0;
                }
                if (stopOnNext) {
                    running = false;
                    break;
                }
                skipped = System.currentTimeMillis() - skipped;
                last = System.currentTimeMillis() - skipped;
                if (lock)
                    try {
                        Thread.sleep(Yld.clamp(targetTime - skipped, 0, targetTime));
                    } catch (InterruptedException e) {
                        Yld.throwException(e);
                    }

            }
        }
        if (stop == EngineStop.INTERRUPT_ON_END) {
            thread.interrupt();
        } else if (stop == EngineStop.JOIN_ON_END) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Yld.throwException(e);
            }
        }
    }
    /**
     * "This function is called every time the game updates."
     * The first parameter, `last`, is the time in milliseconds since the last update. The second parameter, `actual`, is
     * the time in milliseconds the actual frame process began.
     *
     * @param last The last time the update method was called.
     * @param actual The current time in milliseconds.
     */
    public void update(long last, long actual) {

    }

    /**
     * Returns true if the thread is running, false otherwise.
     *
     * @return The boolean value of the variable running.
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * This function sets the value of the running variable to the value of the running parameter.
     */
    public void setRunning(boolean running) {
        this.running = running;
    }

    /**
     * Returns the thread that is running this function.
     *
     * @return The thread object.
     */
    public Thread getThread() {
        return thread;
    }

    /**
     * This function returns the controller object.
     *
     * @return The controller object.
     */
    public EngineController getController() {
        return controller;
    }

    /**
     * This function returns the todoList.
     *
     * @return An ArrayList of YldEngineAction objects.
     */
    public ArrayList<YldEngineAction> getTodoList() {
        return todoList;
    }

    /**
     * This function returns the target time of the game loop.
     *
     * @return The targetTime variable is being returned.
     */
    public int getTargetTime() {
        return targetTime;
    }


    /**
     * This function sets the target time to the value of the game loop.
     *
     * @param targetTime The targetTime variable
     */
    public void setTargetTime(int targetTime) {
        this.targetTime = targetTime;
    }

    /**
     * `isIgnoreTodo()` returns a boolean value that indicates whether the `ignoreTodo` variable is set to true
     *
     * @return The ignoreTodo variable
     */
    public boolean isIgnoreTodo() {
        return ignoreTodo;
    }

    /**
     * > Sets the ignoreTodo flag
     *
     * @param ignoreTodo If true, the plugin will ignore the elements in the todoList.
     */
    public void setIgnoreTodo(boolean ignoreTodo) {
        this.ignoreTodo = ignoreTodo;
    }

    /**
     * This function returns the stop variable.
     *
     * @return The stop variable is being returned.
     */
    public EngineStop getStop() {
        return stop;
    }

    /**
     * This function sets the stop variable to the stop variable passed in.
     *
     * @param stop The stop variable to be set.
     */
    public void setStop(EngineStop stop) {
        this.stop = stop;
    }

    /**
     * This function returns the value of the oneSecCount variable.
     *
     * @return The value of the value oneSecCount.
     */
    public int getOneSecCount() {
        return oneSecCount;
    }

    /**
     * This function sets the value of the oneSecCount variable to the value of the oneSecCount parameter
     *
     * @param oneSecCount The stop value to be set.
     */
    public void setOneSecCount(int oneSecCount) {
        this.oneSecCount = oneSecCount;
    }

    /**
     * This function returns the value of the last variable.
     *
     * @return The last variable value.
     */
    public long getLast() {
        return last;
    }

    /**
     * This function sets the last variable to the value of the parameter last.
     *
     * @param last The 'last' value to be set.
     */
    public void setLast(long last) {
        this.last = last;
    }

    /**
     * This function returns the actual value of the `actual` variable.
     *
     * @return The actual variable value.
     */
    public long getActual() {
        return actual;
    }

    /**
     * This function sets the value of the `actual` variable.
     *
     * @param actual The actual variable value to be set.
     */
    public void setActual(long actual) {
        this.actual = actual;
    }

    /**
     * This function returns the number of frames per second.
     *
     * @return The fpsCount variable is being returned.
     */
    public int getFpsCount() {
        return fpsCount;
    }

    public void setFpsCount(int fpsCount) {
        this.fpsCount = fpsCount;
    }

    /**
     * This function returns the number of frames that have been rendered in the last second
     *
     * @return The number of frames that have been rendered in the last second.
     */
    public int getOneSecFrameCount() {
        return oneSecFrameCount;
    }

    public void setOneSecFrameCount(int oneSecFrameCount) {
        this.oneSecFrameCount = oneSecFrameCount;
    }

    /**
     * This function returns the value of the lock variable.
     *
     * @return The value of the lock variable.
     */
    public boolean isLock() {
        return lock;
    }

    /**
     * This function sets the lock variable to the value of the lock parameter.
     *
     * @param lock The lock value to be set.
     */
    public void setLock(boolean lock) {
        this.lock = lock;
    }

    /**
     * Returns to value of the stopOnNext variable.
     *
     * @return The value of the stopOnNext variable.
     */
    public boolean isStopOnNext() {
        return stopOnNext;
    }

    /**
     * This function sets the value of the stopOnNext variable to the value of the stopOnNext parameter.
     * The stopOnNext variable tells if the game loop will end in the next frame update.
     *
     * @param stopOnNext The stopOnNext value to be set.
     */
    public void setStopOnNext(boolean stopOnNext) {
        this.stopOnNext = stopOnNext;
    }

    /**
     * This function returns the paused variable.
     *
     * @return A reference to the paused object.
     */
    public AtomicBoolean getPaused() {
        return paused;
    }

    /**
     * This function sets the paused variable to the value of the paused variable passed in as a parameter.
     *
     * @param paused This is an AtomicBoolean object that is used to pause and resume the thread.
     */
    public void setPaused(AtomicBoolean paused) {
        this.paused = paused;
    }

    /**
     * This function sets the paused variable to the value of the paused variable passed in as a parameter.
     *
     * @param paused Will set the AtomicBoolean object that is used to pause and resume the thread.
     */
    public void setPaused(boolean paused) {
        this.paused.set(paused);
    }

    public long getSkipped() {
        return skipped;
    }

    public void setSkipped(long skipped) {
        this.skipped = skipped;
    }
}
