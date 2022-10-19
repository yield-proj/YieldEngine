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
import com.xebisco.yield.render.WindowPrint;

import java.lang.reflect.InvocationTargetException;

/**
 * It's the class that handles the game loop
 */
public class GameHandler extends Engine {
    private final YldGame game;
    private SampleGraphics sampleGraphics;
    private RenderMaster renderMaster;
    private static RenderMaster sampleRenderMaster;
    private WindowPrint windowPrint;
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
        if(renderMaster instanceof WindowPrint)
            windowPrint = (WindowPrint) renderMaster;
        sampleGraphics = renderMaster.initGraphics();
        if(renderMaster instanceof ExceptionThrower) {
            if(Yld.getExceptionThrower() == null)
                Yld.setExceptionThrower((ExceptionThrower) renderMaster);
        }
        setTargetTime(1000 / game.getConfiguration().fps);
        setLock(game.getConfiguration().fpsLock);
        renderMaster.before(game);
        defaultConcurrentEngine = new Engine(null);
        defaultConcurrentEngine.setTargetTime(1);
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
        renderMaster.frameStart(sampleGraphics);
        if (renderMaster.canStart())
            game.updateScene(delta, sampleGraphics);
        renderMaster.frameEnd(game.getScene().getView());
        game.afterRender(delta);
        if(game.getConfiguration().framesToGarbageCollection >= 0) {
            framesToGarbageCollectionCount++;
            if(framesToGarbageCollectionCount - game.getConfiguration().framesToGarbageCollection >= 0) {
                System.gc();
                framesToGarbageCollectionCount = 0;
            }
        }
    }

    /**
     * This function returns the game object.
     *
     * @return The game object.
     */
    public YldGame getGame() {
        return game;
    }

    /**
     * This function returns the default concurrent engine.
     *
     * @return The default concurrent engine.
     */
    public Engine getDefaultConcurrentEngine() {
        return defaultConcurrentEngine;
    }

    /**
     * Sets the default concurrent engine
     *
     * @param defaultConcurrentEngine The default engine to use for concurrent requests.
     */
    public void setDefaultConcurrentEngine(Engine defaultConcurrentEngine) {
        this.defaultConcurrentEngine = defaultConcurrentEngine;
    }

    /**
     * This function returns the renderMaster object.
     *
     * @return The renderMaster object.
     */
    public RenderMaster getRenderMaster() {
        return renderMaster;
    }

    /**
     * This function sets the renderMaster variable to the renderMaster parameter.
     *
     * @param renderMaster The RenderMaster object that will be used to render the game.
     */
    public void setRenderMaster(RenderMaster renderMaster) {
        this.renderMaster = renderMaster;
    }

    /**
     * This function returns the sampleGraphics object.
     *
     * @return The sampleGraphics object.
     */
    public SampleGraphics getSampleGraphics() {
        return sampleGraphics;
    }

    /**
     * This function sets the sampleGraphics variable to the sampleGraphics parameter.
     *
     * @param sampleGraphics The SampleGraphics object that will be used to draw the graphics.
     */
    public void setSampleGraphics(SampleGraphics sampleGraphics) {
        this.sampleGraphics = sampleGraphics;
    }

    /**
     * This function returns the sampleRenderMaster variable.
     *
     * @return The sampleRenderMaster object.
     */
    public static RenderMaster getSampleRenderMaster() {
        return sampleRenderMaster;
    }

    /**
     * It sets the sampleRenderMaster to the sampleRenderMaster that is passed in
     *
     * @param sampleRenderMaster The sample render master.
     */
    public static void setSampleRenderMaster(RenderMaster sampleRenderMaster) {
        GameHandler.sampleRenderMaster = sampleRenderMaster;
    }

    /**
     * > Returns whether the next frame can be rendered
     *
     * @return The value of the canRenderNext variable.
     */
    public boolean isCanRenderNext() {
        return canRenderNext;
    }

    /**
     * This function sets the value of the canRenderNext variable to the value of the canRenderNext parameter.
     *
     * @param canRenderNext This is a boolean that tells the renderer whether it can render the next frame.
     */
    public void setCanRenderNext(boolean canRenderNext) {
        this.canRenderNext = canRenderNext;
    }

    /**
     * This function returns the windowPrint object.
     *
     * @return The windowPrint object.
     */
    public WindowPrint getWindowPrint() {
        return windowPrint;
    }

    /**
     * This function sets the windowPrint variable to the windowPrint parameter.
     *
     * @param windowPrint The WindowPrint object that will be used to print the window.
     */
    public void setWindowPrint(WindowPrint windowPrint) {
        this.windowPrint = windowPrint;
    }

    /**
     * Returns the number of frames to garbage collection count.
     *
     * @return The number of frames until garbage collection.
     */
    public int getFramesToGarbageCollectionCount() {
        return framesToGarbageCollectionCount;
    }

    /**
     * This function sets the number of frames to garbage collection count
     *
     * @param framesToGarbageCollectionCount The number of frames to wait before garbage collection.
     */
    public void setFramesToGarbageCollectionCount(int framesToGarbageCollectionCount) {
        this.framesToGarbageCollectionCount = framesToGarbageCollectionCount;
    }

    /**
     * Returns true if the delta is zero
     *
     * @return The boolean value of the variable zeroDelta.
     */
    public boolean isZeroDelta() {
        return zeroDelta;
    }

    public void setZeroDelta(boolean zeroDelta) {
        this.zeroDelta = zeroDelta;
    }
}
