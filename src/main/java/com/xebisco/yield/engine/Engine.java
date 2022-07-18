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

public class Engine implements Runnable {
    private final Thread thread = new Thread(this);
    private final EngineController controller;
    private final ArrayList<YldEngineAction> todoList = new ArrayList<>();
    private int targetTime = 33, oneSecCount, oneSecFrameCount, fpsCount;
    private EngineStop stop = EngineStop.JOIN_ON_END;
    private boolean running, ignoreTodo, lock = true, stopOnNext;

    private AtomicBoolean paused = new AtomicBoolean(false);

    private long last, actual;

    public Engine(EngineController controller) {
        this.controller = controller;
    }

    @Override
    public void run() {
        running = true;
        if (controller != null)
            controller.start();
        while (running) {
            try {
                if (!paused.get()) {


                    actual = System.currentTimeMillis();
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
                    last = System.currentTimeMillis();
                    if (stopOnNext) {
                        running = false;
                        break;
                    }
                    if (lock)
                        try {
                            Thread.sleep(targetTime);
                        } catch (InterruptedException e) {
                            Yld.throwException(e);
                        }

                }
            } catch (Exception e) {
                Yld.throwException(e);
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

    public void update(long last, long actual) {

    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public Thread getThread() {
        return thread;
    }

    public EngineController getController() {
        return controller;
    }

    public ArrayList<YldEngineAction> getTodoList() {
        return todoList;
    }

    public int getTargetTime() {
        return targetTime;
    }

    public void setTargetTime(int targetTime) {
        this.targetTime = targetTime;
    }

    public boolean isIgnoreTodo() {
        return ignoreTodo;
    }

    public void setIgnoreTodo(boolean ignoreTodo) {
        this.ignoreTodo = ignoreTodo;
    }

    public EngineStop getStop() {
        return stop;
    }

    public void setStop(EngineStop stop) {
        this.stop = stop;
    }

    public int getOneSecCount() {
        return oneSecCount;
    }

    public void setOneSecCount(int oneSecCount) {
        this.oneSecCount = oneSecCount;
    }

    public long getLast() {
        return last;
    }

    public void setLast(long last) {
        this.last = last;
    }

    public long getActual() {
        return actual;
    }

    public void setActual(long actual) {
        this.actual = actual;
    }

    public int getFpsCount() {
        return fpsCount;
    }

    public void setFpsCount(int fpsCount) {
        this.fpsCount = fpsCount;
    }

    public int getOneSecFrameCount() {
        return oneSecFrameCount;
    }

    public void setOneSecFrameCount(int oneSecFrameCount) {
        this.oneSecFrameCount = oneSecFrameCount;
    }

    public boolean isLock() {
        return lock;
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }

    public boolean isStopOnNext() {
        return stopOnNext;
    }

    public void setStopOnNext(boolean stopOnNext) {
        this.stopOnNext = stopOnNext;
    }

    public AtomicBoolean getPaused() {
        return paused;
    }

    public void setPaused(AtomicBoolean paused) {
        this.paused = paused;
    }

    public void setPaused(boolean paused) {
        this.paused.set(paused);
    }
}
