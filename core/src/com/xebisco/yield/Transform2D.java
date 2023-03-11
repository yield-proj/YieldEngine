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
public class Transform2D {
    private final Point2D position = new Point2D();
    private final Vector2D scale = new Vector2D(1, 1);
    private double zRotation;

    public void translate(TwoAnchorRepresentation value) {
        position.sumLocal(value);
    }

    public void translate(double x, double y) {
        translate(new TwoAnchorRepresentation(x, y));
    }

    public void scale(double x, double y) {
        scale(new TwoAnchorRepresentation(x, y));
    }

    public void scale(TwoAnchorRepresentation value) {
        scale.sumLocal(value);
    }

    public void rotate(double value) {
        zRotation += value;
    }

    /**
     * This function returns the position of the object.
     *
     * @return The position of the object.
     */
    public Point2D getPosition() {
        return position;
    }

    /**
     * This function returns the zRotation of the object
     *
     * @return The zRotation variable is being returned.
     */
    public double getzRotation() {
        return zRotation;
    }

    /**
     * This function sets the zRotation variable to the value of the parameter zRotation.
     *
     * @param zRotation The rotation of the object around the z axis.
     */
    public void setzRotation(double zRotation) {
        this.zRotation = zRotation;
    }

    /**
     * Returns the scale of the object.
     *
     * @return The scale of the object.
     */
    public Vector2D getScale() {
        return scale;
    }
}
