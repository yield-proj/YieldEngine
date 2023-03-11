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
 * It's a representation of a point in 2D space
 */
public class TwoAnchorRepresentation {
    private double x, y;
    public TwoAnchorRepresentation(double x, double y) {
        set(x, y);
    }

    public TwoAnchorRepresentation set(double x, double y) {
        this.x = x;
        this.y = y;
        return this;
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

    public TwoAnchorRepresentation sum(TwoAnchorRepresentation a) {
        TwoAnchorRepresentation result = new TwoAnchorRepresentation(x, y);
        result.sumLocal(a);
        return result;
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

    public TwoAnchorRepresentation subtract(TwoAnchorRepresentation a) {
        TwoAnchorRepresentation result = new TwoAnchorRepresentation(x, y);
        result.subtractLocal(a);
        return result;
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

    public TwoAnchorRepresentation multiplyLocal(double value) {
        x *= value;
        y *= value;
        return this;
    }

    public TwoAnchorRepresentation multiply(TwoAnchorRepresentation a) {
        TwoAnchorRepresentation result = new TwoAnchorRepresentation(x, y);
        result.multiplyLocal(a);
        return result;
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

    public TwoAnchorRepresentation divide(TwoAnchorRepresentation a) {
        TwoAnchorRepresentation result = new TwoAnchorRepresentation(x, y);
        result.divideLocal(a);
        return result;
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
