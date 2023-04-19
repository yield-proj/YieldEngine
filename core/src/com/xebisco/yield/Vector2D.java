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

    public static final Vector2D RIGHT = new Vector2D(1, 0), LEFT = new Vector2D(-1, 0), UP = new Vector2D(0, 1), DOWN = new Vector2D(0, -1), ZERO = new Vector2D(0, 0);

    public Vector2D() {
        super(0, 0);
    }

    public Vector2D(TwoAnchorRepresentation a) {
        super(a);
    }

    public Vector2D(double x, double y) {
        super(x, y);
    }

    /**
     * The function returns a 2D vector with a given angle in degrees and intensity.
     *
     * @param degrees The angle in degrees that the vector makes with the positive x-axis.
     * @param intensity The intensity parameter represents the magnitude or length of the vector. It determines how long
     * the vector will be.
     * @return A new instance of the Vector2D class with x and y components calculated based on the input degrees and
     * intensity values.
     */
    public static Vector2D vector2D(int degrees, double intensity) {
        double radians = Math.toRadians(degrees);
        return new Vector2D(Math.cos(radians) * intensity, Math.sin(radians) * intensity);
    }

    /**
     * This Java function calculates the angle in degrees between the positive x-axis and a point represented by its x and
     * y coordinates using the Math.atan2() and Math.toDegrees() methods.
     *
     * @return The method `calculateDegrees()` returns the angle in degrees between the positive x-axis and the vector
     * represented by the point (x,y) in the Cartesian coordinate system. It uses the `Math.atan2()` method to calculate
     * the angle in radians and then converts it to degrees using the `Math.toDegrees()` method.
     */
    public double calculateDegrees() {
        return Math.toDegrees(Math.atan2(getY(), getX()));
    }

    /**
     * This Java function calculates the intensity of a vector using the Pythagorean theorem.
     *
     * @return The method `calculateIntensity` is returning the intensity of a vector, which is calculated as the square
     * root of the sum of the squares of its components (x and y).
     */
    public double calculateIntensity() {
        return Math.sqrt(Math.pow(getX(), 2) + Math.pow(getY(), 2));
    }
}
