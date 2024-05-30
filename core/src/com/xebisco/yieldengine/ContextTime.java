/*
 * Copyright [2022-2024] [Xebisco]
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

package com.xebisco.yieldengine;

import java.io.Serializable;

/**
 * Represents the timing context for an application, encapsulating time-related properties such as
 * delta time, timescale, and target sleep time. This class allows for the adjustment of the perceived speed of time
 * (via timescale) and facilitates frame rate control by setting a target frames per second (FPS). It also tracks
 * the total time since the start of the context, enabling various time-based operations within the application.
 */

public class ContextTime implements Serializable {
    private double timeScale = 1, deltaTime;
    private long targetSleepTime = 16_666_666, timeSinceStart;

    /**
     * Constructs a {@link ContextTime} object with a specified target frames per second (FPS).
     * This constructor calculates the target sleep time based on the provided FPS.
     *
     * @param targetFPS The desired target FPS for the context time.
     */
    public ContextTime(double targetFPS) {
        setTargetFPS(targetFPS);
    }

    /**
     * Default constructor for ContextTime. Initializes the object with default values.
     */
    public ContextTime() {
    }

    /**
     * Sets the target frames per second (FPS) for this context.
     * This method calculates and sets the target sleep time based on the provided FPS.
     *
     * @param targetFPS The desired target FPS.
     * @return The current ContextTime instance for chaining method calls.
     * @throws IllegalArgumentException If the targetFPS is less than or equal to zero.
     */
    public ContextTime setTargetFPS(double targetFPS) {
        if (targetFPS <= 0) throw new IllegalArgumentException("targetFPS can't be less or equal to zero");
        return setTargetSleepTime((long) (1 / targetFPS * 1_000_000_000));
    }

    /**
     * Gets the current timescale.
     *
     * @return The current timescale.
     */
    public double timeScale() {
        return timeScale;
    }

    /**
     * Sets the timescale. This scale is applied to deltaTime to simulate faster or slower motion.
     *
     * @param timeScale The new timescale to set.
     */
    public void timeScale(double timeScale) {
        this.timeScale = timeScale;
    }

    /**
     * Gets the delta time adjusted by the current timescale.
     * This value represents the scaled time elapsed between the current and the last update.
     *
     * @return The scaled delta time.
     */
    public double deltaTime() {
        return deltaTime * timeScale;
    }

    /**
     * Sets the delta time. This is the raw time elapsed between the current and the last update.
     *
     * @param deltaTime The delta time to set.
     * @return The current ContextTime instance for chaining method calls.
     */
    public ContextTime setDeltaTime(double deltaTime) {
        this.deltaTime = deltaTime;
        return this;
    }

    /**
     * Gets the target sleep time. This is the intended duration to sleep between updates to achieve the target FPS.
     *
     * @return The target sleep time in nanoseconds.
     */
    public long targetSleepTime() {
        return targetSleepTime;
    }

    /**
     * Sets the target sleep time. This is used to adjust the update frequency to achieve the target FPS.
     *
     * @param targetSleepTime The target sleep time in nanoseconds.
     * @return The current ContextTime instance for chaining method calls.
     */
    public ContextTime setTargetSleepTime(long targetSleepTime) {
        this.targetSleepTime = targetSleepTime;
        return this;
    }

    /**
     * Sets the timescale. This scale is applied to deltaTime to simulate faster or slower motion.
     * This is an alternative method to the void timeScale(double timeScale) method, allowing method chaining.
     *
     * @param timeScale The new timescale to set.
     * @return The current ContextTime instance for chaining method calls.
     */
    public ContextTime setTimeScale(double timeScale) {
        this.timeScale = timeScale;
        return this;
    }

    /**
     * Gets the time since the start of the context in nanoseconds.
     *
     * @return The time since start in nanoseconds.
     */
    public long timeSinceStart() {
        return timeSinceStart;
    }

    /**
     * Sets the time since the start of the context. This is typically updated to track the total elapsed time.
     *
     * @param timeSinceStart The time since start in nanoseconds.
     * @return The current ContextTime instance for chaining method calls.
     */
    public ContextTime setTimeSinceStart(long timeSinceStart) {
        this.timeSinceStart = timeSinceStart;
        return this;
    }
}