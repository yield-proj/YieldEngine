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

/**
 * {@code ImmutableColor} class extends the {@link Color} class and provides an immutable version of it.
 * It does not allow any of its properties to be changed once they are set.
 */
public class ImmutableColor extends Color {
    public ImmutableColor(long rgba) {
        super(rgba);
    }

    public ImmutableColor(Color toCopy) {
        super(toCopy);
    }

    public ImmutableColor(int rgb, double alpha) {
        super(rgb, alpha);
    }

    public ImmutableColor(double red, double green, double blue) {
        super(red, green, blue);
    }

    public ImmutableColor(double red, double green, double blue, double alpha) {
        super(red, green, blue, alpha);
    }

    @Override
    public Color setRed(double red) {
        throw new ImmutableBreakException();
    }

    @Override
    public Color setGreen(double green) {
        throw new ImmutableBreakException();
    }

    @Override
    public Color setBlue(double blue) {
        throw new ImmutableBreakException();
    }

    @Override
    public Color setAlpha(double alpha) {
        throw new ImmutableBreakException();
    }
}
