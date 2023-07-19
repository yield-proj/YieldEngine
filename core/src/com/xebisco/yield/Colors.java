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

import java.util.Random;

/**
 * It's a collection of static final Color objects that represent the default colors used by GrADS
 */
public class Colors {
    /**
     * From GrADS Default colors.
     */
    public static final Color
            BLACK = new ImmutableColor(0, 0, 0),
            WHITE = new ImmutableColor(1, 1, 1),
            RED = new ImmutableColor(1, 0.24, 0.24),
            GREEN = new ImmutableColor(0, 0.86, 0),
            DARK_BLUE = new ImmutableColor(0.18, 0.24, 1),
            LIGHT_BLUE = new ImmutableColor(0, 0.78, 0.78),
            MAGENTA = new ImmutableColor(0.94, 0, 0.5),
            YELLOW = new ImmutableColor(0.9, 0.86, 0.2),
            ORANGE = new ImmutableColor(0.94, 0.5, 0.16),
            PURPLE = new ImmutableColor(0.63, 0, 0.78),
            YELLOW_GREEN = new ImmutableColor(0.63, 0.9, 0.2),
            MEDIUM_BLUE = new ImmutableColor(0, 0.63, 1),
            DARK_YELLOW = new ImmutableColor(0.9, 0.69, 0.18),
            AQUA = new ImmutableColor(0, 0.82, 0.55),
            DARK_PURPLE = new ImmutableColor(0.5, 0, 0.86),
            GRAY = new ImmutableColor(0.67, 0.67, 0.67);

    public static Color random(Random random) {
         return new Color(random.nextDouble(), random.nextDouble(), random.nextDouble());
    }

    public static Color random() {
        return random(Global.RANDOM);
    }
}
