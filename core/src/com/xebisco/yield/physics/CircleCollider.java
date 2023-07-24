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

import com.xebisco.yield.Global;
import com.xebisco.yield.TwoAnchorRepresentation;
import com.xebisco.yield.Vector2D;
import com.xebisco.yield.VisibleOnEditor;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.Shape;

/**
 * The CircleCollider class defines a circle collider with a radius and center point that can be set and retrieved.
 */
public class CircleCollider extends Collider {
    @VisibleOnEditor
    private double radius = 50;

    @VisibleOnEditor
    private Vector2D center = new Vector2D();

    @Override
    public Shape getShape() {
        CircleShape s = new CircleShape();
        float largerScale = (float) (Math.max(Math.abs(getTransform().getScale().getX()), Math.abs(getTransform().getScale().getY())));
        if(isIgnoreScaling())
            largerScale = 1;
        s.setRadius((float) (radius * largerScale / getApplication().getPhysicsPpm()));
        s.m_p.set(Global.toVec2(center.divide(new TwoAnchorRepresentation(getApplication().getPhysicsPpm(), getApplication().getPhysicsPpm()))));
        return s;
    }

    /**
     * The function returns the value of the radius.
     *
     * @return The method `getRadius()` is returning the value of the `radius` variable, which is of type `double`.
     */
    public double getRadius() {
        return radius;
    }

    /**
     * The function sets the value of the radius variable.
     *
     * @param radius The parameter "radius" is a double data type that represents the radius of a circle. The method
     * "setRadius" sets the value of the instance variable "radius" to the value passed as a parameter.
     */
    public void setRadius(double radius) {
        this.radius = radius;
    }

    /**
     * The function returns a Vector2D representing the center point.
     *
     * @return A Vector2D object representing the center of something (e.g. a shape or object).
     */
    public Vector2D getCenter() {
        return center;
    }

    public void setCenter(Vector2D center) {
        this.center = center;
    }
}
