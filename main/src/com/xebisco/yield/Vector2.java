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

import java.util.Objects;

/**
 * It's a class that represents a 2D vector
 */
public class Vector2 {
    public float x, y;

    public Vector2() {
    }

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2 vector2 = (Vector2) o;
        return Float.compare(vector2.x, x) == 0 && Float.compare(vector2.y, y) == 0;
    }

    /**
     * Returns a new vector with the same direction as this vector, but with a multiplied value.
     *
     * @param value The value to multiply the vector by.
     * @return A new Vector2 object with the x and y values multiplied by the value passed in.
     */
    public Vector2 mul(float value) {
        return new Vector2(x * value, y * value);
    }

    /**
     * Divide the vector by a scalar value and return the result as a new vector.
     *
     * @param value The value to divide by.
     * @return A new vector2 object with the x and y values divided by the value.
     */
    public Vector2 div(float value) {
        return new Vector2(x / value, y / value);
    }

    /**
     * Returns a new vector with the sum of the current vector and the given value.
     *
     * @param value The value to add to the vector.
     * @return A new Vector2 object with the sum of the x and y values of the current Vector2 object and the value passed
     * in.
     */
    public Vector2 sum(float value) {
        return new Vector2(x + value, y + value);
    }

    /**
     * Returns a new vector with the value subtracted from each component.
     *
     * @param value The value to subtract from the vector.
     * @return A new Vector2 object with the x and y values subtracted by the value.
     */
    public Vector2 subt(float value) {
        return new Vector2(x - value, y - value);
    }

    /**
     * Multiply this vector by another vector and return the result.
     *
     * @param vector The vector to multiply this vector by.
     * @return A new vector with the x and y values multiplied by the x and y values of the vector passed in.
     */
    public Vector2 mul(Vector2 vector) {
        return new Vector2(x * vector.x, y * vector.y);
    }

    /**
     * Divides the current vector by the given vector and returns the result as a new vector.
     *
     * @param vector The vector to divide by.
     * @return A new vector with the x and y values divided by the x and y values of the vector passed in.
     */
    public Vector2 div(Vector2 vector) {
        return new Vector2(x / vector.x, y / vector.y);
    }

    /**
     * This function returns a new Vector2 object that is the sum of the current Vector2 object and the Vector2 object
     * passed in as a parameter.
     *
     * @param vector The vector to add to this vector.
     * @return A new vector with the sum of the two vectors.
     */
    public Vector2 sum(Vector2 vector) {
        return new Vector2(x + vector.x, y + vector.y);
    }

    /**
     * Subtracts the given vector from this vector and returns the result as a new vector.
     *
     * @param vector The vector to subtract from this vector.
     * @return A new vector with the subtracted values of the two vectors.
     */
    public Vector2 subt(Vector2 vector) {
        return new Vector2(x - vector.x, y - vector.y);
    }

    @Deprecated
    public float distance(Vector2 other) {
        return (float) (((x + other.x) / Math.sqrt(2)) + (y + other.y) / Math.sqrt(2));
    }

    /**
     * Returns the distance between this vector and another vector.
     *
     * @param other The other vector to calculate the distance to.
     * @return The distance between the two points.
     */
    public float dist(Vector2 other) {
        return (float) Math.sqrt(Math.pow(Yld.mod(other.x - x), 2) + Math.pow(Yld.mod(other.y - y), 2));
    }

    /**
     * Returns the angle in degrees between this vector and the given vector.
     *
     * @param other The other vector to calculate the angle between
     * @return The angle between the two vectors.
     */
    public float angle(Vector2 other) {
        return (float) Math.toDegrees(Math.atan2(other.y - y, other.x - x));
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    /**
     * Returns a copy of this vector.
     *
     * @return A new Vector2 object with the same x and y values as the original.
     */
    public Vector2 get() {
        return new Vector2(x, y);
    }

    @Override
    public String toString() {
        return "Vector2{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    /**
     * Set the x and y coordinates to 0.
     */
    public void reset() {
        x = 0;
        y = 0;
    }
}
