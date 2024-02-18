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

import java.io.Serializable;

/**
 * It's a container for time-related variables
 */
public class ContextTime implements Serializable {
    private double timeScale = 1, deltaTime;
    private long targetSleepTime = 16_666_666, timeSinceStart;

    public ContextTime(double targetFPS) {
        setTargetFPS(targetFPS);
    }

    public ContextTime() {
    }

    /**
     * This function sets the target sleep time based on the desired frames per second.
     *
     * @param targetFPS The target frames per second (FPS) that the program should aim to achieve.
     */
    public ContextTime setTargetFPS(double targetFPS) {
        if (targetFPS <= 0) throw new IllegalArgumentException("targetFPS can't be less or equal to zero");
        return setTargetSleepTime((long) (1 / targetFPS * 1_000_000_000));
    }

    /**
     * Returns the scale of the context time.
     *
     * @return The timeScale variable is being returned.
     */
    public double timeScale() {
        return timeScale;
    }

    /**
     * This function sets the timescale of the context.
     *
     * @param timeScale This is the timescale of the context. The default value is 1.0.
     */
    public void timeScale(double timeScale) {
        this.timeScale = timeScale;
    }

    /**
     * Returns the time in seconds since the last frame.
     *
     * @return The time in seconds since the last update multiplied by the timescale.
     */
    public double deltaTime() {
        return deltaTime * timeScale;
    }

    /**
     * This function sets the deltaTime variable to the value of the deltaTime parameter.
     *
     * @param deltaTime The time in seconds since the last update.
     */
    public ContextTime setDeltaTime(double deltaTime) {
        this.deltaTime = deltaTime;
        return this;
    }

    /**
     * Returns the target sleep time in microseconds
     *
     * @return The target sleep time.
     */
    public long targetSleepTime() {
        return targetSleepTime;
    }

    /**
     * This function sets the target sleep time to the value passed in.
     *
     * @param targetSleepTime The amount of time the thread should sleep for in microseconds.
     */
    public ContextTime setTargetSleepTime(long targetSleepTime) {
        this.targetSleepTime = targetSleepTime;
        return this;
    }

    public ContextTime setTimeScale(double timeScale) {
        this.timeScale = timeScale;
        return this;
    }

    public long timeSinceStart() {
        return timeSinceStart;
    }

    public ContextTime setTimeSinceStart(long timeSinceStart) {
        this.timeSinceStart = timeSinceStart;
        return this;
    }
}
