/*
 * Copyright [2022-2023] [Xebisco]
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

package com.xebisco.yield;

import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

public class RenderingThread extends Thread {

    private final AtomicBoolean running = new AtomicBoolean();
    private final GraphicsManager graphicsManager;

    private List<DrawInstruction> drawInstructions;
    private final Object lockObject = new Object(), otherLock = new Object();
    private boolean finishedRendering;

    private final Semaphore semaphore = new Semaphore(0);

    public RenderingThread(GraphicsManager graphicsManager) {
        setName("Yield Rendering Thread");
        this.graphicsManager = graphicsManager;
    }

    @Override
    public void run() {
        running.set(true);
        while (running.get()) {
            finishedRendering = false;
            synchronized (lockObject) {
                try {
                    if (drawInstructions == null)
                        lockObject.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            if (drawInstructions != null) {
                graphicsManager.draw(drawInstructions);
                setDrawInstructions(null);
            }
            finishedRendering = true;
            semaphore.release();
        }
        interrupt();
    }

    public synchronized void renderAsync(List<DrawInstruction> drawInstructions) {
        setDrawInstructions(drawInstructions);
        synchronized (lockObject) {
            lockObject.notify();
        }
    }

    public void aWait() {
        if (finishedRendering)
            return;

        semaphore.acquireUninterruptibly();

    }

    public AtomicBoolean running() {
        return running;
    }

    public GraphicsManager graphicsManager() {
        return graphicsManager;
    }

    public List<DrawInstruction> drawInstructions() {
        return drawInstructions;
    }

    public RenderingThread setDrawInstructions(List<DrawInstruction> drawInstructions) {
        this.drawInstructions = drawInstructions;
        return this;
    }

    public Object lockObject() {
        return lockObject;
    }

    public Object otherLock() {
        return otherLock;
    }

    public boolean finishedRendering() {
        return finishedRendering;
    }

    public RenderingThread setFinishedRendering(boolean finishedRendering) {
        this.finishedRendering = finishedRendering;
        return this;
    }

    public Semaphore semaphore() {
        return semaphore;
    }
}
