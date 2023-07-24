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
 * It's a representation of a point in 2D space
 */
public class TwoAnchorRepresentation implements Serializable {
    private double x, y;
    public TwoAnchorRepresentation(double x, double y) {
        set(x, y);
    }

    public TwoAnchorRepresentation(TwoAnchorRepresentation a) {
        set(a.getX(), a.getY());
    }

    public TwoAnchorRepresentation set(double x, double y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public TwoAnchorRepresentation reset() {
        return set(0, 0);
    }

    public TwoAnchorRepresentation set(TwoAnchorRepresentation a) {
        return set(a.getX(), a.getY());
    }

    /**
     * Add the x and y values of the given TwoAnchorRepresentation to the x and y values of this TwoAnchorRepresentation.
     *
     * @param a The TwoAnchorRepresentation object to add to this one.
     */
    public TwoAnchorRepresentation sumLocal(TwoAnchorRepresentation a) {
        x += a.x;
        y += a.y;
        return this;
    }

    /**
     * This method returns the sum of two anchor representations.
     *
     * @param a The parameter `a` is an object of the class `TwoAnchorRepresentation` that is being passed as an argument
     * to the `sum` method.
     * @return A new instance of the `TwoAnchorRepresentation` class is being returned, which is the result of adding the
     * current instance's coordinates (`x` and `y`) to the coordinates of the `TwoAnchorRepresentation` object passed as
     * the argument `a`. The `sumLocal` method is called on the new instance to perform the addition operation.
     */
    public TwoAnchorRepresentation sum(TwoAnchorRepresentation a) {
        return new TwoAnchorRepresentation(x, y).sumLocal(a);
    }

    /**
     * Subtracts the given representation from this one.
     *
     * @param a The TwoAnchorRepresentation to subtract from this one.
     */
    public TwoAnchorRepresentation subtractLocal(TwoAnchorRepresentation a) {
        x -= a.x;
        y -= a.y;
        return this;
    }

    /**
     * This method subtracts a TwoAnchorRepresentation object from another and returns the result as a new
     * TwoAnchorRepresentation object.
     *
     * @param a The parameter "a" is a TwoAnchorRepresentation object that is being subtracted from the current object.
     * @return A new instance of the `TwoAnchorRepresentation` class is being returned. The `subtractLocal` method is being
     * called on the new instance with the `a` parameter passed in, which modifies the instance's `x` and `y` values.
     */
    public TwoAnchorRepresentation subtract(TwoAnchorRepresentation a) {
        return new TwoAnchorRepresentation(x, y).subtractLocal(a);
    }

    /**
     * Multiply the x and y values of this object by the x and y values of the given object.
     *
     * @param a The TwoAnchorRepresentation to multiply this TwoAnchorRepresentation by.
     */
    public TwoAnchorRepresentation multiplyLocal(TwoAnchorRepresentation a) {
        x *= a.x;
        y *= a.y;
        return this;
    }

    /**
     * This method multiplies the x and y values of a TwoAnchorRepresentation object by a given value and returns the
     * object.
     *
     * @param value The parameter "value" is a double type variable that represents the value by which the x and y
     * coordinates of the TwoAnchorRepresentation object are multiplied.
     * @return The method is returning this instance of the `TwoAnchorRepresentation`.
     */
    public TwoAnchorRepresentation multiplyLocal(double value) {
        x *= value;
        y *= value;
        return this;
    }

    /**
     * This method returns the result of multiplying two instances of the TwoAnchorRepresentation class.
     *
     * @param a The parameter "a" is a TwoAnchorRepresentation object that is being passed as an argument to the "multiply"
     * method.
     * @return A new instance of the `TwoAnchorRepresentation` class is being returned. The new instance is created by
     * calling the `multiplyLocal` method on the current instance of `TwoAnchorRepresentation` with the argument `a`.
     */
    public TwoAnchorRepresentation multiply(TwoAnchorRepresentation a) {
        return new TwoAnchorRepresentation(x, y).multiplyLocal(a);
    }

    /**
     * Divide the x and y values of this object by the x and y values of the given object.
     *
     * @param a The TwoAnchorRepresentation to divide by.
     */
    public TwoAnchorRepresentation divideLocal(TwoAnchorRepresentation a) {
        x /= a.x;
        y /= a.y;
        return this;
    }

    /**
     * This Java function returns a new TwoAnchorRepresentation object that is the result of dividing the current object by
     * the input object.
     *
     * @param a The parameter "a" is a TwoAnchorRepresentation object that is being passed as an argument to the "divide"
     * method.
     * @return A new instance of the `TwoAnchorRepresentation` class is being returned, which is the result of dividing the
     * current instance by the `a` parameter using the `divideLocal` method.
     */
    public TwoAnchorRepresentation divide(TwoAnchorRepresentation a) {
        return new TwoAnchorRepresentation(x, y).divideLocal(a);
    }

    /**
     * The function returns the absolute value of the x and y coordinates of a TwoAnchorRepresentation object.
     *
     * @return The method is returning this instance of the `TwoAnchorRepresentation` class.
     */
    public TwoAnchorRepresentation absoluteLocal() {
        x = Math.abs(x);
        y = Math.abs(y);
        return this;
    }

    /**
     * This method returns a new TwoAnchorRepresentation object with absolute values of x and y coordinates.
     *
     * @return A new instance of the `TwoAnchorRepresentation` class is being returned, which is created using the `x` and
     * `y` values of the current instance. The `absoluteLocal()` method is then called on the new instance to modify its
     * values, but the modified instance is not returned.
     */
    public TwoAnchorRepresentation absolute() {
        return new TwoAnchorRepresentation(x, y).absoluteLocal();
    }

    /**
     * This function inverts a TwoAnchorRepresentation object locally by multiplying it with -1.
     *
     * @return The method is returning this `TwoAnchorRepresentation` object.
     */
    public TwoAnchorRepresentation invertLocal() {
        return multiplyLocal(-1);
    }

    /**
     * The function inverts a TwoAnchorRepresentation object by multiplying it with a new object with negative values.
     *
     * @return The method is returning a new instance of the `TwoAnchorRepresentation` class that is the result of
     * multiplying the current instance with a new instance of `TwoAnchorRepresentation` that has the values `-1` for both
     * anchors. This new instance is effectively the inverse of the current instance.
     */
    public TwoAnchorRepresentation invert() {
        return multiply(new TwoAnchorRepresentation(-1, -1));
    }

    /**
     * This function returns the value of the x variable.
     *
     * @return The value of the x variable.
     */
    public double getX() {
        return x;
    }

    /**
     * This function sets the value of the x variable to the value of the x parameter.
     *
     * @param x The x coordinate of the point.
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * This function returns the y coordinate of the point.
     *
     * @return The y coordinate of the point.
     */
    public double getY() {
        return y;
    }

    /**
     * This function sets the y coordinate of the point to the value of the parameter y.
     *
     * @param y The y coordinate of the point.
     */
    public void setY(double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
