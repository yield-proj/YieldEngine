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
import com.xebisco.yield.exceptions.CannotLoadException;
import com.xebisco.yield.exceptions.MissingRenderMasterException;
import com.xebisco.yield.exceptions.YieldEngineException;
import com.xebisco.yield.render.ExceptionThrower;
import com.xebisco.yield.render.RenderMaster;

import java.lang.reflect.InvocationTargetException;

public class GameHandler extends Engine {
    private final YldGame game;
    private SampleGraphics sampleGraphics;
    private RenderMaster renderMaster;
    private static RenderMaster sampleRenderMaster;
    private Engine defaultConcurrentEngine;

    private int framesToGarbageCollectionCount;

    private boolean canRenderNext;

    public GameHandler(YldGame game) {
        super(null);
        game.setHandler(this);
        this.game = game;
        if(game.getConfiguration().renderMaster == null) {
            if (game.getConfiguration().renderMasterName != null) {
                try {
                    renderMaster = (RenderMaster) Class.forName(game.getConfiguration().renderMasterName).getDeclaredConstructor().newInstance();
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                         NoSuchMethodException e) {
                    Yld.throwException(e);
                }
                catch (ClassNotFoundException e) {
                    Yld.throwException(new MissingRenderMasterException("Could not find render master: '" + game.getConfiguration().renderMasterName + "'."));
                }
            } else if (sampleRenderMaster != null) {
                renderMaster = sampleRenderMaster;
            } else {
                Yld.throwException(new MissingRenderMasterException("Yield Engine needs a path to a RenderMaster implementation."));
            }
        } else {
            renderMaster = game.getConfiguration().renderMaster;
        }
        sampleGraphics = renderMaster.initGraphics();
        if(renderMaster instanceof ExceptionThrower) {
            if(Yld.getExceptionThrower() == null)
                Yld.setExceptionThrower((ExceptionThrower) renderMaster);
        }
        setTargetTime(1000 / game.getConfiguration().fps);
        setLock(game.getConfiguration().fpsLock);
        renderMaster.before(game);
        defaultConcurrentEngine = new Engine(null);
        defaultConcurrentEngine.getThread().start();
    }

   private boolean zeroDelta = true;

    @Override
    public void update(long last, long actual) {
        float delta = (actual - last) / 1_000f;
        if (zeroDelta) {
            delta = 0f;
            System.gc();
        }
        zeroDelta = false;
        renderMaster.frameStart(sampleGraphics, game.getScene().getView());
        if (renderMaster.canStart())
            game.updateScene(delta, sampleGraphics);
        renderMaster.frameEnd();
        if(game.getConfiguration().framesToGarbageCollection >= 0) {
            framesToGarbageCollectionCount++;
            if(framesToGarbageCollectionCount - game.getConfiguration().framesToGarbageCollection >= 0) {
                System.gc();
                framesToGarbageCollectionCount = 0;
            }
        }
    }

    public YldGame getGame() {
        return game;
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
