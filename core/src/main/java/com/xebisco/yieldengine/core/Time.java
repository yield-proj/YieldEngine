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

package com.xebisco.yieldengine.core;

public final class Time {
    private static double timeScale = 1, deltaTime;
    private static long targetSleepTime = 16_666_666, timeSinceStart;

    public static void setTargetFPS(double targetFPS) {
        if (targetFPS <= 0) throw new IllegalArgumentException("targetFPS can't be less or equal to zero");
        setTargetSleepTime((long) (1 / targetFPS * 1_000_000_000));
    }

    public static double getTimeScale() {
        return timeScale;
    }

    public static void setTimeScale(double timeScale) {
        Time.timeScale = timeScale;
    }

    public static double getDeltaTime() {
        return deltaTime;
    }

    static void setDeltaTime(double deltaTime) {
        Time.deltaTime = deltaTime;
    }

    public static long getTargetSleepTime() {
        return targetSleepTime;
    }

    public static void setTargetSleepTime(long targetSleepTime) {
        Time.targetSleepTime = targetSleepTime;
    }

    public static long getTimeSinceStart() {
        return timeSinceStart;
    }

    public static void setTimeSinceStart(long timeSinceStart) {
        Time.timeSinceStart = timeSinceStart;
    }
}