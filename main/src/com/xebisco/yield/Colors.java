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

/**
 * A collection of some basic colors.
 */
public class Colors {
    public final static Color RED = new ImmutableColor(1, 0, 0),
            CYAN = new ImmutableColor(0, 1, 1),
            BLUE = new ImmutableColor(0, 0, 1),
            WHITE = new ImmutableColor(1, 1, 1),
            BLACK = new ImmutableColor(0, 0, 0),
            MAGENTA = new ImmutableColor(1, 0, 1),
            PURPLE = new ImmutableColor(.5f, 0, 1),
            YELLOW = new ImmutableColor(1, 1, 0),
            GREEN = new ImmutableColor(0, 1, 0),
            PINK = new ImmutableColor(1, .5f, .5f),
            LIME = new ImmutableColor(.5f, 1, .5f),
            GRAY = new ImmutableColor(.5f, .5f, .5f),
            LIGHT_GRAY = new ImmutableColor(.8f, .8f, .8f),
            DARK_GRAY = new ImmutableColor(.2f, .2f, .2f),
            BROWN = new ImmutableColor(.5f, .3f, .1f),
            TRANSPARENT = new ImmutableColor(0, 0, 0, 0);

    /**
     * Returns a random color.
     *
     * @return A new color object with random values for red, green, and blue.
     */
    public static Color random() {
        return new Color(Yld.RAND.nextInt(255) / 255f, Yld.RAND.nextInt(255) / 255f, Yld.RAND.nextInt(255) / 255f, 1);
    }
}
