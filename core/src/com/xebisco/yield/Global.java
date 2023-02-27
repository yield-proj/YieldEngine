/*
 * Copyright [2022-2023] [Xebisco]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.xebisco.yield;

public class Global {
    public static int clamp(int value, int min, int max) {
        return value > max ? max : Math.max(value, min);
    }
    public static long clamp(long value, long min, long max) {
        return value > max ? max : Math.max(value, min);
    }
    public static double clamp(double value, double min, double max) {
        return value > max ? max : Math.max(value, min);
    }
    public static float clamp(float value, float min, float max) {
        return value > max ? max : Math.max(value, min);
    }
}