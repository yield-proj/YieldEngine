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
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.Shape;

/**
 * The CircleCollider class defines a circle collider with a radius and center point that can be set and retrieved.
 */

@ComponentIcon(iconType = ComponentIconType.PHYSICS)
public class CircleCollider extends Collider {
    @VisibleOnEditor
    private double radius = 50;

    @VisibleOnEditor
    private Vector2D center = new Vector2D();

    @Override
    public Shape getShape() {
        CircleShape s = new CircleShape();
        float largerScale = (float) (Math.max(Math.abs(transform().scale().x()), Math.abs(transform().scale().y())));
        if(isIgnoreScaling())
            largerScale = 1;
        s.setRadius((float) (radius * largerScale / application().physicsPpm()));
        s.m_p.set(Global.toVec2(center.divide(new Vector2D(application().physicsPpm(), application().physicsPpm()))));
        return s;
    }

    /**
     * The function returns the value of the radius.
     *
     * @return The method `radius()` is returning the value of the `radius` variable, which is of type `double`.
     */
    public double radius() {
        return radius;
    }

    /**
     * The function sets the value of the radius variable.
     *
     * @param radius The parameter "radius" is a double data type that represents the radius of a circle. The method
     * "setRadius" sets the value of the instance variable "radius" to the value passed as a parameter.
     */
    public CircleCollider setRadius(double radius) {
        this.radius = radius;
        return this;
    }

    /**
     * The function returns a Vector2D representing the center point.
     *
     * @return A Vector2D object representing the center of the circle.
     */
    public Vector2D center() {
        return center;
    }

    public CircleCollider setCenter(Vector2D center) {
        this.center = center;
        return this;
    }
}
