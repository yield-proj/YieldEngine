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

import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Set;

/**
 * An instance of this class is made to contain the time information of a YldGame.
 *
 * @author Xebisco
 * @since 4_beta1
 */
public class YldTime {
    private float delta, fps, targetFPS;
    private YldGame game;
    private Set<YldPair<YldTask, Float>> tasks = new HashSet<>();

    /**
     * Creates a YldTime instance that will pick the time information of the passed YldGame
     *
     * @param game the game instance
     */
    public YldTime(YldGame game) {
        setGame(game);
    }

    /**
     * @return The delta time of the current frame. This variable is updated in the YldTimeSystem.
     * @since 4_beta1
     */
    public float getDelta() {
        return delta;
    }

    /**
     * @return The fps since the last frame. This variable is updated in the YldTimeSystem.
     * @since 4_beta1
     */
    public float getFps() {
        return fps;
    }

    /**
     * @return The target fps of the YldGame GameConfiguration instance.
     * @since 4_1.1
     */
    public float getTargetFPS() {
        return targetFPS;
    }

    public void setTargetFPS(float fps) {
        if (fps <= 0) throw new IllegalArgumentException("TargetFPS needs to be mor than 0");
        targetFPS = fps;
        game.getHandler().setTargetTime((int) (1000 / fps));
    }

    /**
     * @return This YldTime YldGame instance.
     */
    public YldGame getGame() {
        return game;
    }

    /**
     * Setter for the YldGame instance
     */
    public void setGame(YldGame game) {
        this.game = game;
        targetFPS = game.getConfiguration().fps;
    }

    /**
     * "For each task in the list, subtract the delta from the task's time, and if the time is less than or equal to zero,
     * execute the task."
     *
     * The first thing we do is set the delta to the delta passed in. Then we loop through the tasks list. For each task,
     * we subtract the delta from the task's time. If the time is less than or equal to zero, we execute the task
     *
     * @param delta The time in seconds since the last update.
     */
    public void setDelta(float delta) {
        this.delta = delta;
        try {
            for (YldPair<YldTask, Float> task : tasks) {
                task.setSecond(task.getSecond() - delta);
                if(task.getSecond() <= 0f) task.getFirst().execute();
            }
        } catch (ConcurrentModificationException ignore) {
        }
    }

    /**
     * Setter for the fps variable
     */
    public void setFps(float fps) {
        this.fps = fps;
    }

    /**
     * Returns the fps of the RenderMaster of this game.
     *
     * @return The RenderMaster fps.
     */
    public float getRenderFps() {
        return game.getHandler().getRenderMaster().fpsCount();
    }

    /**
     * This function returns a set of YldPair objects, where each YldPair object contains a YldTask object and a Float
     * object.
     *
     * @return A set of YldPair objects.
     */
    public Set<YldPair<YldTask, Float>> getTasks() {
        return tasks;
    }

    /**
     * This function sets the tasks variable to the tasks variable.
     *
     * @param tasks A set of YldPair objects. Each YldPair object contains a YldTask object and a float value. The float
     * value is the time in seconds that the task will be executed
     */
    public void setTasks(Set<YldPair<YldTask, Float>> tasks) {
        this.tasks = tasks;
    }

    /**
     * Add a task to the list of tasks to be executed.
     *
     * @param task The task to be executed.
     * @param time The time in seconds that the task will be executed.
     */
    public void addTask(YldTask task, float time) {
        getTasks().add(new YldPair<>(task, time));
    }
}
