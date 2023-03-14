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

    public static Vector2D vector2D(int degrees, double intensity) {
        double radians = Math.toRadians(degrees);
        return new Vector2D(Math.cos(radians) * intensity, Math.sin(radians) * intensity);
    }

    public double calculateDegrees() {
        return Math.toDegrees(Math.atan2(getY(), getX()));
    }

    public double calculateIntensity() {
        return Math.sqrt(Math.pow(getX(), 2) + Math.pow(getY(), 2));
    }
}
