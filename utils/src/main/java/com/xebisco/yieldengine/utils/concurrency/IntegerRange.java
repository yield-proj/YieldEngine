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

package com.xebisco.yieldengine.utils.concurrency;

public class IntegerRange {
    private final int min, max, step;

    private IntegerRange(int min, int max, int step) {
        this.min = min;
        this.max = max;
        this.step = step;

        if(max - min < 0) {
            throw new IllegalArgumentException("'max - min' must be greater than zero");
        }

        if(((max - min) / (double) step) % 1 != 0) {
            throw new IllegalArgumentException("step: " + step);
        }
    }

    public static IntegerRange range(int min, int max, int step) {
        return new IntegerRange(min, max, step);
    }

    public static IntegerRange range(int min, int max) {
        return range(min, max, 1);
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public int getStep() {
        return step;
    }
}
