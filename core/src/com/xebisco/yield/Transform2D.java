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
 * It represents the position, size, scale, and rotation of a 2D object
 */

@ComponentIcon(iconType = ComponentIconType.TRANSFORM)
public class Transform2D {
    @VisibleOnEditor
    private final Vector2D position = new Vector2D(), scale = new Vector2D(1, 1), centerOffset = new Vector2D();
    @VisibleOnEditor
    private double zRotation;

    /**
     * This function translates the position by adding a TwoAnchorRepresentation value to it.
     *
     * @param value The `TwoAnchorRepresentation` object to translate this `Transform2D` instance position.
     */
    public void translate(Vector2D value) {
        position.sumLocal(value);
    }

    /**
     * This method translates an object's position by a given x and y value using a TwoAnchorRepresentation.
     *
     * @param x The x-coordinate by which the object will be translated.
     * @param y The y-coordinate by which the object will be translated.
     */
    public void translate(double x, double y) {
        translate(new Vector2D(x, y));
    }

    /**
     * The function scales the `Transform2D` object using a two-anchor representation.
     *
     * @param x The value to scale the object horizontally.
     * @param y The value to scale the object vertically.
     */
    public void scale(double x, double y) {
        scale(new Vector2D(x, y));
    }

    /**
     * The function scales the `Transform2D` object using a two-anchor representation.
     *
     * @param value The `TwoAnchorRepresentation` object to scale this `Transform2D` instance.
     */
    public void scale(Vector2D value) {
        scale.sumLocal(scale.multiply(value));
    }

    /**
     * The function rotates an object by a specified angle around the z-axis.
     *
     * @param value The parameter "value" is a double type variable that represents the amount of rotation to be added to
     * the current zRotation value.
     */
    public void rotate(double value) {
        zRotation += value;
    }

    /**
     * This function returns the position of the object.
     *
     * @return The position of the object.
     */
    public Vector2D position() {
        return position;
    }

    /**
     *
     */
    public Vector2D scale() {
        return scale;
    }

    public Vector2D centerOffset() {
        return centerOffset;
    }

    public double zRotation() {
        return zRotation;
    }

    public Transform2D setzRotation(double zRotation) {
        this.zRotation = zRotation;
        return this;
    }
}
