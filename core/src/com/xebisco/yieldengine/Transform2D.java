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

import com.xebisco.yieldengine.editor.annotations.Visible;

/**
 * A class representing the transformation of a 2D object.
 * This class encapsulates the position, scale, and rotation of a 2D object. It provides methods to translate, scale, and rotate the object.
 */

public class Transform2D {
    @Visible
    private final Vector2D position = new Vector2D(), scale = new Vector2D(1, 1);
    @Visible
    private ObjectAnchor anchor = ObjectAnchor.CENTER;
    @Visible
    private double zRotation;

    /**
     * Constructs a new {@link Transform2D} instance.
     */
    public Transform2D() {
    }

    /**
     * Constructs a new {@link Transform2D} instance from another Transform2D instance.
     *
     * @param t The {@link Transform2D} instance to copy.
     */
    public Transform2D(Transform2D t) {
        position.set(t.position);
        scale.set(t.scale);
        anchor = t.anchor;
        zRotation = t.zRotation;
    }


    /**
     * Translates the position of the object by adding a {@link Vector2D} value to it.
     *
     * @param value The {@link Vector2D} object to translate this {@link Transform2D} instance position.
     */
    public void translate(Vector2D value) {
        position.sumLocal(value);
    }

    /**
     * Translates the {@link Transform2D} position by a given x and y value.
     *
     * @param x The x-coordinate by which the object will be translated.
     * @param y The y-coordinate by which the object will be translated.
     */
    public void translate(double x, double y) {
        translate(new Vector2D(x, y));
    }

    /**
     * Scales the {@link Transform2D} object by a given x and y value.
     *
     * @param x The value to scale the object horizontally.
     * @param y The value to scale the object vertically.
     */
    public void scale(double x, double y) {
        scale(new Vector2D(x, y));
    }

    /**
     * Scales the {@link Transform2D} object using a {@link Vector2D}.
     *
     * @param value The {@link Vector2D} object to scale this {@link Transform2D} instance.
     */
    public void scale(Vector2D value) {
        scale.sumLocal(scale.multiply(value));
    }

    /**
     * Rotates the object by a specified angle around the z-axis.
     *
     * @param value A double type variable that represents the degrees of rotation.
     */
    public void rotate(double value) {
        zRotation += value;
    }

    /**
     * Returns the position of the object.
     *
     * @return The position of the object.
     */
    public Vector2D position() {
        return position;
    }

    /**
     * Returns the scale of the object.
     *
     * @return The scale of the object.
     */
    public Vector2D scale() {
        return scale;
    }

    /**
     * Returns the z-rotation of the object.
     *
     * @return The z-rotation of the object.
     */
    public double zRotation() {
        return zRotation;
    }

    /**
     * Sets the z-rotation of the object.
     *
     * @param zRotation The new z-rotation value.
     * @return This {@link Transform2D} instance for method chaining.
     */
    public Transform2D setzRotation(double zRotation) {
        this.zRotation = zRotation;
        return this;
    }

    /**
     * Returns the anchor of the object.
     *
     * @return The anchor of the object.
     */
    public ObjectAnchor anchor() {
        return anchor;
    }

    /**
     * Sets the object's anchor.
     *
     * @param anchor The new object anchor.
     * @return This {@link Transform2D} instance for method chaining.
     */
    public Transform2D setAnchor(ObjectAnchor anchor) {
        this.anchor = anchor;
        return this;
    }
}
