/*
 * Copyright [2022] [Xebisco]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xebisco.yield;

import com.xebisco.yield.exceptions.ImmutableBreakException;

/**
 * It's a color that can't be changed.
 */
public class ImmutableColor extends Color {
    public ImmutableColor(int rgb) {
        super(rgb);
    }

    public ImmutableColor(int rgb, float alpha) {
        super(rgb, alpha);
    }

    public ImmutableColor(float r, float g, float b) {
        super(r, g, b);
    }

    public ImmutableColor(float r, float g, float b, float a) {
        super(r, g, b, a);
    }

    @Override
    public void setA(float a) {
        Yld.throwException(new ImmutableBreakException("Cannot set Alpha value of a immutable color"));
    }

    @Override
    public void setB(float b) {
        Yld.throwException(new ImmutableBreakException("Cannot set Blue value of a immutable color"));
    }

    @Override
    public void setG(float g) {
        Yld.throwException(new ImmutableBreakException("Cannot set Green value of a immutable color"));
    }

    @Override
    public void setR(float r) {
        Yld.throwException(new ImmutableBreakException("Cannot set Red value of a immutable color"));
    }
}
