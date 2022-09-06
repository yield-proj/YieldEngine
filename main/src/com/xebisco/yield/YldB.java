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

package com.xebisco.yield;

import com.xebisco.yield.engine.Engine;
import com.xebisco.yield.engine.EngineStop;
import com.xebisco.yield.engine.YldEngineAction;

/**
 * Sample methods for the majority of the Yield Game Engine classes.
 *
 * @author Xebisco
 * @since 4_alpha1
 */
public abstract class YldB {

    protected YldGame game;

    /**
     * When the object is created.
     */
    public abstract void create();

    /**
     * This function is called once per frame.
     *
     * @param delta The time in seconds since the last frame.
     */
    public abstract void update(float delta);

    /**
     * This function is called when this object is destroyed.
     */
    public abstract void onDestroy();

    /**
     * Executes a YldAction instance independently of others.
     *
     * @param action      The action to be executed.
     * @param multiThread The thread configuration.
     */
    public final long concurrent(YldAction action, MultiThread multiThread) {
        long id = Yld.RAND.nextLong();
        if (multiThread == MultiThread.DEFAULT) {
            game.getHandler().getDefaultConcurrentEngine().getTodoList().add(new YldEngineAction(action, 0, false, id));
        } else if (multiThread == MultiThread.ON_GAME_THREAD) {
            game.getHandler().getTodoList().add(new YldEngineAction(action, 0, false, id));
        } else if (multiThread == MultiThread.EXCLUSIVE) {
            Engine engine = new Engine(null);
            engine.getThread().start();
            YldEngineAction action1 = new YldEngineAction(() ->
            {
                action.onAction();
                engine.setStop(EngineStop.INTERRUPT_ON_END);
                engine.setRunning(false);
            }, 0, false, id);
            engine.getTodoList().add(action1);
        }
        return id;
    }

    /**
     * When the condition is true, execute the action.
     * The first thing to notice is that the function takes in a `YldAction` and a `Condition`. The `YldAction` is the
     * action that will be executed when the condition is true. The `Condition` is the condition that must be true for the
     * action to be executed
     *
     * @param action         The action to be executed when the condition is met.
     * @param condition      The condition to check.
     * @param execAction     The thread that the action will be executed when the condition is met.
     * @param checkCondition The thread that will check if the action should be executed.
     */
    public final void doWhen(YldAction action, Condition condition, MultiThread execAction, MultiThread checkCondition) {
        Engine check = game.getEngine(checkCondition, EngineStop.INTERRUPT_ON_END), exec = game.getEngine(execAction, EngineStop.INTERRUPT_ON_END);
        assert check != null;
        assert exec != null;
        long checkId = Yld.RAND.nextLong();
        final boolean[] executed = {false};
        check.getTodoList().add(new YldEngineAction(() -> {
            if (condition.is() && !executed[0]) {
                exec.getTodoList().add(new YldEngineAction(() -> {
                    executed[0] = true;
                    check.getTodoList().forEach(a -> {
                        if (a.getId() == checkId) a.setRepeat(false);
                    });
                    action.onAction();
                }, 0, false, Yld.RAND.nextLong()));
            }
        }, 0, true, checkId));
    }

    public final void doWhen(YldAction action, Condition condition) {
        doWhen(action, condition, MultiThread.ON_GAME_THREAD, MultiThread.DEFAULT);
    }

    public void log(Object x) {
        Yld.log(x);
    }

    /**
     * Creates a timer.
     *
     * @param action      The action to be performed on the end of the timer.
     * @param time        The time, in seconds to end the timer.
     * @param repeat      If the timer repeat after it ends.
     * @param multiThread The thread configuration.
     */
    public final long timer(YldAction action, float time, boolean repeat, MultiThread multiThread) {
        long id = Yld.RAND.nextLong();
        if (multiThread == MultiThread.DEFAULT) {
            game.getHandler().getDefaultConcurrentEngine().getTodoList().add(new YldEngineAction(action, (int) (time * 1000), repeat, id));
        } else if (multiThread == MultiThread.ON_GAME_THREAD) {
            game.getHandler().getTodoList().add(new YldEngineAction(action, (int) (time * 1000), repeat, id));
        } else if (multiThread == MultiThread.EXCLUSIVE) {
            Engine engine = new Engine(null);
            engine.getThread().start();
            engine.getTodoList().add(new YldEngineAction(action, (int) (time * 1000), repeat, id));
            engine.setStop(EngineStop.JOIN_ON_END);
            YldEngineAction action1 = new YldEngineAction(action, 0, repeat, id);
            engine.getTodoList().add(action1);
        }
        return id;
    }

    /**
     * Creates a timer, on the on game thread.
     *
     * @param action The action to be performed on the end of the timer.
     * @param time   The time, in seconds to end the timer.
     * @param repeat If the timer repeat after it ends.
     */
    public final long timer(YldAction action, float time, boolean repeat) {
        return timer(action, time, repeat, MultiThread.DEFAULT);
    }

    /**
     * Executes a YldAction instance independently of others, on the default multi thread.
     *
     * @param action The action to be executed.
     */
    public final long concurrent(YldAction action) {
        return concurrent(action, MultiThread.DEFAULT);
    }

    public final void remove(final long id, final MultiThread thread) {
        if (thread == MultiThread.EXCLUSIVE)
            throw new IllegalArgumentException("MultiThread cannot be EXCLUSIVE");
        if (thread == MultiThread.ON_GAME_THREAD)
            game.getHandler().getTodoList().removeIf((a) -> a.getId() == id);
        else if (thread == MultiThread.DEFAULT)
            game.getHandler().getDefaultConcurrentEngine().getTodoList().removeIf((a) -> a.getId() == id);
    }

    /**
     * It returns the file path of the file relative to the game's root directory
     *
     * @param relativeFile The relative file to be converted to an absolute file.
     * @return A relative file.
     */
    public final RelativeFile relativeFile(RelativeFile relativeFile) {
        return game.getHandler().getRenderMaster().relativeFile(relativeFile);
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
     * This function sets the game variable to the game variable passed in.
     *
     * @param game The game object value to set.
     */
    public void setGame(YldGame game) {
        this.game = game;
    }
}
