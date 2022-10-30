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
import com.xebisco.yield.exceptions.IncompatibleException;
import com.xebisco.yield.exceptions.MissingRenderMasterException;
import com.xebisco.yield.exceptions.YieldEngineException;
import com.xebisco.yield.render.ExceptionThrower;
import com.xebisco.yield.render.RenderMaster;
import com.xebisco.yield.render.Renderable;
import com.xebisco.yield.render.WindowPrint;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * It's the class that handles the game loop
 */
public class GameHandler extends Engine {
    private final YldGame game;
    private Set<Renderable> renderables, updateRenderables;
    private RenderMaster renderMaster;
    private static RenderMaster sampleRenderMaster;
    private WindowPrint windowPrint;

    private int framesToGarbageCollectionCount;

    private boolean canRenderNext;
    private int lastMemory, lastLastMemory, actualMemory;
    private FinalObjectWrapper threadObjectWrapper;

    public GameHandler(YldGame game) {
        super(null);
        game.setHandler(this);
        this.game = game;
        if (game.getConfiguration().renderMaster == null) {
            if (game.getConfiguration().renderMasterName != null) {
                try {
                    renderMaster = (RenderMaster) Class.forName(game.getConfiguration().renderMasterName).getDeclaredConstructor().newInstance();
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                         NoSuchMethodException e) {
                    Yld.throwException(e);
                } catch (ClassNotFoundException e) {
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
        if (renderMaster instanceof WindowPrint)
            windowPrint = (WindowPrint) renderMaster;
        if (renderMaster instanceof ExceptionThrower) {
            if (Yld.getExceptionThrower() == null)
                Yld.setExceptionThrower((ExceptionThrower) renderMaster);
        }
        try {
            renderMaster.setThreadTask(() -> {
                if (threadObjectWrapper != null) {
                    synchronized (threadObjectWrapper.getObject()) {
                        threadObjectWrapper.getObject().notify();
                    }
                }
            });
        } catch (AbstractMethodError e) {
            Yld.throwException(new IncompatibleException(e.getMessage()));
        }
        setTargetTime(1000 / game.getConfiguration().fps);
        setLock(game.getConfiguration().fpsLock);
        renderables = new LinkedHashSet<>();
        updateRenderables = new LinkedHashSet<>();
        try {
            renderMaster.start(renderables);
        } catch (AbstractMethodError e) {
            Yld.throwException(new IncompatibleException(e.getMessage()));
        }
    }

    private boolean zeroDelta = true;

    @Override
    public void update(long last, long actual) {
        if (threadObjectWrapper == null) threadObjectWrapper = new FinalObjectWrapper(new Object());
        float delta = (actual - last) / 1_000f;
        if (zeroDelta) {
            delta = 0f;
        }
        zeroDelta = false;
        updateRenderables.clear();
        try {
            if (renderMaster.canStart())
                game.updateScene(delta, updateRenderables);
        } catch (AbstractMethodError e) {
            Yld.throwException(new IncompatibleException(e.getMessage()));
        }
        renderables.clear();
        renderables.addAll(updateRenderables);
        try {
            renderMaster.frameEnd(game.getScene().getView().getBgColor(), getGame().getScene().getView().getWidth(), getGame().getScene().getView().getHeight(), (int) getGame().getScene().getView().getPosition().x, (int) getGame().getScene().getView().getPosition().y, getGame().getScene().getView().getTransform().scale.x, getGame().getScene().getView().getTransform().scale.y);
        } catch (AbstractMethodError e) {
            Yld.throwException(new IncompatibleException(e.getMessage()));
        }
        try {
            synchronized (threadObjectWrapper.getObject()) {
                threadObjectWrapper.getObject().wait();
            }
        } catch (InterruptedException e) {
            Yld.throwException(e);
        }
        game.afterRender(delta);
        if (game.getConfiguration().framesToGarbageCollection >= 0) {
            framesToGarbageCollectionCount++;
            if (framesToGarbageCollectionCount - game.getConfiguration().framesToGarbageCollection >= 0) {
                lastLastMemory = lastMemory;
                lastMemory = actualMemory;
                actualMemory = Yld.MEMORY();
                framesToGarbageCollectionCount = 0;
                if (lastLastMemory < lastMemory && lastMemory < actualMemory) {
                    System.gc();
                    Yld.getDebugLogger().log("GameHandler: Called garbage collector.");
                }
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

    public Set<Renderable> getRenderables() {
        return renderables;
    }

    public void setRenderables(Set<Renderable> renderables) {
        this.renderables = renderables;
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

    public Set<Renderable> getUpdateRenderables() {
        return updateRenderables;
    }

    public void setUpdateRenderables(Set<Renderable> updateRenderables) {
        this.updateRenderables = updateRenderables;
    }

    public int getLastMemory() {
        return lastMemory;
    }

    public void setLastMemory(int lastMemory) {
        this.lastMemory = lastMemory;
    }

    public int getLastLastMemory() {
        return lastLastMemory;
    }

    public void setLastLastMemory(int lastLastMemory) {
        this.lastLastMemory = lastLastMemory;
    }

    public int getActualMemory() {
        return actualMemory;
    }

    public void setActualMemory(int actualMemory) {
        this.actualMemory = actualMemory;
    }

    public FinalObjectWrapper getThreadObjectWrapper() {
        return threadObjectWrapper;
    }

    public void setThreadObjectWrapper(FinalObjectWrapper threadObjectWrapper) {
        this.threadObjectWrapper = threadObjectWrapper;
    }
}
