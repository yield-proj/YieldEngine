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

import java.io.Serializable;

/**
 * Vector2D is a Vector2D that represents a vector in 2D space.
 */
public class Vector2D implements Serializable {
    private double x, y;
    public Vector2D(double x, double y) {
        set(x, y);
    }
    public Vector2D() {
    }

    public Vector2D(Vector2D a) {
        set(a.x(), a.y());
    }

    public Vector2D set(double x, double y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public Vector2D reset() {
        return set(0, 0);
    }

    public Vector2D set(Vector2D a) {
        return set(a.x(), a.y());
    }

    /**
     * Add the x and y values of the given Vector2D to the x and y values of this Vector2D.
     *
     * @param a The Vector2D object to add to this one.
     */
    public Vector2D sumLocal(Vector2D a) {
        x += a.x;
        y += a.y;
        return this;
    }

    /**
     * This method returns the sum of two anchor representations.
     *
     * @param a The parameter `a` is an object of the class `Vector2D` that is being passed as an argument
     * to the `sum` method.
     * @return A new instance of the `Vector2D` class is being returned, which is the result of adding the
     * current instance's coordinates (`x` and `y`) to the coordinates of the `Vector2D` object passed as
     * the argument `a`. The `sumLocal` method is called on the new instance to perform the addition operation.
     */
    public Vector2D sum(Vector2D a) {
        return new Vector2D(x, y).sumLocal(a);
    }

    /**
     * Subtracts the given representation from this one.
     *
     * @param a The Vector2D to subtract from this one.
     */
    public Vector2D subtractLocal(Vector2D a) {
        x -= a.x;
        y -= a.y;
        return this;
    }

    /**
     * This method subtracts a Vector2D object from another and returns the result as a new
     * Vector2D object.
     *
     * @param a The parameter "a" is a Vector2D object that is being subtracted from the current object.
     * @return A new instance of the `Vector2D` class is being returned. The `subtractLocal` method is being
     * called on the new instance with the `a` parameter passed in, which modifies the instance's `x` and `y` values.
     */
    public Vector2D subtract(Vector2D a) {
        return new Vector2D(x, y).subtractLocal(a);
    }

    /**
     * Multiply the x and y values of this object by the x and y values of the given object.
     *
     * @param a The Vector2D to multiply this Vector2D by.
     */
    public Vector2D multiplyLocal(Vector2D a) {
        x *= a.x;
        y *= a.y;
        return this;
    }

    /**
     * This method multiplies the x and y values of a Vector2D object by a given value and returns the
     * object.
     *
     * @param value The parameter "value" is a double type variable that represents the value by which the x and y
     * coordinates of the Vector2D object are multiplied.
     * @return The method is returning this instance of the `Vector2D`.
     */
    public Vector2D multiplyLocal(double value) {
        x *= value;
        y *= value;
        return this;
    }

    /**
     * This method returns the result of multiplying two instances of the Vector2D class.
     *
     * @param a The parameter "a" is a Vector2D object that is being passed as an argument to the "multiply"
     * method.
     * @return A new instance of the `Vector2D` class is being returned. The new instance is created by
     * calling the `multiplyLocal` method on the current instance of `Vector2D` with the argument `a`.
     */
    public Vector2D multiply(Vector2D a) {
        return new Vector2D(x, y).multiplyLocal(a);
    }

    public Vector2D multiply(double value) {
        return new Vector2D(x, y).multiplyLocal(value);
    }

    /**
     * Divide the x and y values of this object by the x and y values of the given object.
     *
     * @param a The Vector2D to divide by.
     */
    public Vector2D divideLocal(Vector2D a) {
        x /= a.x;
        y /= a.y;
        return this;
    }

    public Vector2D divideLocal(double value) {
        x /= value;
        y /= value;
        return this;
    }

    /**
     * This Java function returns a new Vector2D object that is the result of dividing the current object by
     * the input object.
     *
     * @param a The parameter "a" is a Vector2D object that is being passed as an argument to the "divide"
     * method.
     * @return A new instance of the `Vector2D` class is being returned, which is the result of dividing the
     * current instance by the `a` parameter using the `divideLocal` method.
     */
    public Vector2D divide(Vector2D a) {
        return new Vector2D(x, y).divideLocal(a);
    }

    public Vector2D divide(double value) {
        return new Vector2D(x, y).divideLocal(value);
    }

    /**
     * The function returns the absolute value of the x and y coordinates of a Vector2D object.
     *
     * @return The method is returning this instance of the `Vector2D` class.
     */
    public Vector2D absoluteLocal() {
        x = Math.abs(x);
        y = Math.abs(y);
        return this;
    }

    /**
     * This method returns a new Vector2D object with absolute values of x and y coordinates.
     *
     * @return A new instance of the `Vector2D` class is being returned, which is created using the `x` and
     * `y` values of the current instance. The `absoluteLocal()` method is then called on the new instance to modify its
     * values, but the modified instance is not returned.
     */
    public Vector2D absolute() {
        return new Vector2D(x, y).absoluteLocal();
    }

    /**
     * This function inverts a Vector2D object locally by multiplying it with -1.
     *
     * @return The method is returning this `Vector2D` object.
     */
    public Vector2D invertLocal() {
        return multiplyLocal(-1);
    }

    /**
     * The function inverts a Vector2D object by multiplying it with a new object with negative values.
     *
     * @return The method is returning a new instance of the `Vector2D` class that is the result of
     * multiplying the current instance with a new instance of `Vector2D` that has the values `-1` for both
     * anchors. This new instance is effectively the inverse of the current instance.
     */
    public Vector2D invert() {
        return multiply(new Vector2D(-1, -1));
    }

    /**
     * This function returns the value of the x variable.
     *
     * @return The value of the x variable.
     */
    public double x() {
        return x;
    }

    /**
     * This function sets the value of the x variable to the value of the x parameter.
     *
     * @param x The x coordinate of the point.
     */
    public Vector2D setX(double x) {
        this.x = x;
        return this;
    }

    /**
     * This function returns the y coordinate of the point.
     *
     * @return The y coordinate of the point.
     */
    public double y() {
        return y;
    }

    /**
     * This function sets the y coordinate of the point to the value of the parameter y.
     *
     * @param y The y coordinate of the point.
     */
    public Vector2D setY(double y) {
        this.y = y;
        return this;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
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
    public double angle() {
        return Math.toDegrees(Math.atan2(y(), x()));
    }

    /**
     * This Java function calculates the intensity of a vector using the Pythagorean theorem.
     *
     * @return The method `calculateIntensity` is returning the intensity of a vector, which is calculated as the square
     * root of the sum of the squares of its components (x and y).
     */
    public double intensity() {
        return Math.sqrt(Math.pow(x(), 2) + Math.pow(y(), 2));
    }

    public double width() {
        return x();
    }

    public double height() {
        return y();
    }
}
