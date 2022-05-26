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

import com.xebisco.yield.*;
import com.xebisco.yield.exceptions.YieldEngineException;
import com.xebisco.yield.render.RenderMaster;

import java.lang.reflect.InvocationTargetException;

public class GameHandler extends Engine {
    private final YldGame game;
    private SampleGraphics sampleGraphics;
    private RenderMaster renderMaster;
    private static RenderMaster sampleRenderMaster;
    private int fps;
    private Engine defaultConcurrentEngine;

    private int framesToGarbageCollectionCount;

    private boolean canRenderNext;

    public GameHandler(YldGame game) {
        super(null);
        this.game = game;
        if (game.getConfiguration().renderMasterName != null) {
            try {
                renderMaster = (RenderMaster) Class.forName(game.getConfiguration().renderMasterName).getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else if (sampleRenderMaster != null) {
            renderMaster = sampleRenderMaster;
        } else {
            throw new YieldEngineException("FATAL: Yield Engine needs a path to a RenderMaster implementation.");
        }
        sampleGraphics = renderMaster.initGraphics();
        game.setWindow(renderMaster.initWindow(game.getConfiguration()));
        fps = game.getConfiguration().fps;
        defaultConcurrentEngine = new Engine(null);
        defaultConcurrentEngine.getThread().start();
    }

    @Override
    public void run() {
        setRunning(true);
        long start, end = System.nanoTime();
        boolean zeroDelta = true;
        while (isRunning()) {
            start = System.nanoTime();
            float delta = (start - end) / 1_000_000_000f;
            if (zeroDelta) delta = 0f;
            zeroDelta = false;
            if (!isIgnoreTodo()) {
                for (int i = 0; i < getTodoList().size(); i++) {
                    YldEngineAction engineAction = getTodoList().get(i);
                    if (engineAction.getToExec() <= 0) {
                        engineAction.getAction().onAction();
                        if (!engineAction.isRepeat())
                            getTodoList().remove(engineAction);
                        engineAction.setToExec(engineAction.getInitialToExec());
                    } else {
                        engineAction.setToExec(engineAction.getToExec() - (int) ((start - end) / 1_000_000));
                    }

                }
            }
            renderMaster.frameStart();
            if (renderMaster.canStart())
                game.updateScene(delta, sampleGraphics);
            renderMaster.frameEnd(game.getScene().getView());
            if(game.getConfiguration().framesToGarbageCollection >= 0) {
                framesToGarbageCollectionCount++;
                if(framesToGarbageCollectionCount - game.getConfiguration().framesToGarbageCollection >= 0) {
                    System.gc();
                    framesToGarbageCollectionCount = 0;
                }
            }
            end = System.nanoTime();
            if (game.getConfiguration().fpsLock) {
                try {
                    Thread.sleep(1000 / fps);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            getThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Texture loadTexture(String path) {
        Texture texture = new Texture(path);
        renderMaster.loadTexture(texture);
        return texture;
    }

    public YldGame getGame() {
        return game;
    }

    public int getFps() {
        return fps;
    }

    public void setFps(int fps) {
        this.fps = fps;
    }

    public Engine getDefaultConcurrentEngine() {
        return defaultConcurrentEngine;
    }

    public void setDefaultConcurrentEngine(Engine defaultConcurrentEngine) {
        this.defaultConcurrentEngine = defaultConcurrentEngine;
    }

    public RenderMaster getRenderMaster() {
        return renderMaster;
    }

    public void setRenderMaster(RenderMaster renderMaster) {
        this.renderMaster = renderMaster;
    }

    public SampleGraphics getSampleGraphics() {
        return sampleGraphics;
    }

    public void setSampleGraphics(SampleGraphics sampleGraphics) {
        this.sampleGraphics = sampleGraphics;
    }

    public static RenderMaster getSampleRenderMaster() {
        return sampleRenderMaster;
    }

    public static void setSampleRenderMaster(RenderMaster sampleRenderMaster) {
        GameHandler.sampleRenderMaster = sampleRenderMaster;
    }

    public boolean isCanRenderNext() {
        return canRenderNext;
    }

    public void setCanRenderNext(boolean canRenderNext) {
        this.canRenderNext = canRenderNext;
    }
}
