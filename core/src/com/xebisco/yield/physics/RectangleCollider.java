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

package com.xebisco.yield.physics;

import com.xebisco.yield.*;
import org.jbox2d.collision.shapes.*;

/**
 * This is a Java class for a rectangle collider that extends a Collider class and includes properties for size, centroid,
 * center, and angle.
 */
public class RectangleCollider extends Collider {
    @VisibleOnInspector
    private Size2D size = new Size2D(100, 100);

    @VisibleOnInspector
    private Vector2D centroid = new Vector2D(), center = new Vector2D();

    @VisibleOnInspector
    private double angle;

    @Override
    public Shape getShape() {
        PolygonShape s = new PolygonShape();
        s.setAsBox((float) (size.getWidth() * Math.abs(getTransform().getScale().getX()) / getApplication().getPhysicsPpm() / 2.0), (float) (size.getHeight() * Math.abs(getTransform().getScale().getY()) / getApplication().getPhysicsPpm() / 2.0), Global.toVec2(centroid.divide(new TwoAnchorRepresentation(getApplication().getPhysicsPpm(), getApplication().getPhysicsPpm()))), (float) Math.toRadians(angle));
        s.m_centroid.set(Global.toVec2(centroid.divide(new TwoAnchorRepresentation(getApplication().getPhysicsPpm(), getApplication().getPhysicsPpm()))));
        return s;
    }

    /**
     * The function returns a Size2D object.
     *
     * @return The method `getSize()` is returning an object of type `Size2D`.
     */
    public Size2D getSize() {
        return size;
    }

    /**
     * This Java function sets the size of an object using a Size2D parameter.
     *
     * @param size The size value to set.
     */
    public void setSize(Size2D size) {
        this.size = size;
    }

    /**
     * The function returns the centroid of the rectangle collider.
     *
     * @return A Vector2D object representing the centroid.
     */
    public Vector2D getCentroid() {
        return centroid;
    }

    /**
     * This function sets the centroid of the rectangle collider.
     *
     * @param centroid The parameter "centroid" is a Vector2D object that represents the center point of the rectangle collider. The method "setCentroid" sets the value of the centroid for
     *                 the current object to the specified value.
     */
    public void setCentroid(Vector2D centroid) {
        this.centroid = centroid;
    }

    /**
     * The function returns a Vector2D representing the center point.
     *
     * @return A Vector2D object representing the center of the rectangle collider.
     */
    public Vector2D getCenter() {
        return center;
    }

    /**
     * This function sets the center of a Vector2D object.
     *
     * @param center The parameter "center" is a Vector2D object that represents the center point of the rectangle collider. The method "setCenter" sets the value of the center point to the specified Vector2D object.
     */
    public void setCenter(Vector2D center) {
        this.center = center;
    }

    /**
     * The function returns the value of the angle.
     *
     * @return The method `getAngle()` is returning a `double` value which represents the angle.
     */
    public double getAngle() {
        return angle;
    }

    /**
     * This function sets the value of the "angle" variable to the input "angle" value.
     *
     * @param angle The parameter "angle" is a double data type that represents the angle value to be set for an object or
     * variable. The method "setAngle" is used to set the value of the angle variable to the specified value passed as an
     * argument to the method.
     */
    public void setAngle(double angle) {
        this.angle = angle;
    }
}
