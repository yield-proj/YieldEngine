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

/**
 * Vector2D is a TwoAnchorRepresentation that represents a vector in 2D space.
 */
public class Vector2D extends TwoAnchorRepresentation {

    public Vector2D() {
        super(0, 0);
    }

    public Vector2D(double x, double y) {
        super(x, y);
    }
}
