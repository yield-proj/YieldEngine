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

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.Shape;

/**
 * It's a circle collider that extends the Collider class
 */
public class CircleCollider extends Collider {
    private float radius = 32;

    public CircleCollider(float radius) {
        this.radius = radius;
    }

    public CircleCollider() {
    }

    @Override
    public Shape shape() {
        CircleShape shape = new CircleShape();
        shape.setRadius(radius / scene.getPpm());
        shape.m_p.set(Yld.toVec2(getOffset()));
        return shape;
    }

    /**
     * This function returns the radius of the circle.
     *
     * @return The radius of the circle.
     */
    public float getRadius() {
        return radius;
    }

    /**
     * This function sets the radius of the circle to the value of the parameter radius.
     *
     * @param radius The radius of the circle.
     */
    public void setRadius(float radius) {
        this.radius = radius;
    }
}
