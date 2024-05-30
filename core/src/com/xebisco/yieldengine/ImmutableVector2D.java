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
 * {@code ImmutableVector2D} class extends {@link Vector2D} and provides an immutable version of the {@link Vector2D} class.
 * It does not allow modification of its x and y coordinates after initialization.
 */
public class ImmutableVector2D extends Vector2D {
    public ImmutableVector2D(double x, double y) {
        super(x, y);
    }

    @Override
    public Vector2D setX(double x) {
        throw new ImmutableBreakException();
    }

    @Override
    public Vector2D setY(double y) {
        throw new ImmutableBreakException();
    }
}
