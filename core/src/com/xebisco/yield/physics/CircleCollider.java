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
import com.xebisco.yield.Vector2D;
import com.xebisco.yield.VisibleOnInspector;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.Shape;

public class CircleCollider extends Collider {
    @VisibleOnInspector
    private double radius = 50;

    @VisibleOnInspector
    private Vector2D center = new Vector2D();

    @Override
    public Shape getShape() {
        CircleShape s = new CircleShape();
        float largerScale = (float) (Math.max(Math.abs(getTransform().getScale().getX()), Math.abs(getTransform().getScale().getY())));
        s.setRadius((float) (radius * largerScale / getApplication().getPhysicsPpm()));
        s.m_p.set(Global.toVec2(center));
        return s;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public Vector2D getCenter() {
        return center;
    }

    public void setCenter(Vector2D center) {
        this.center = center;
    }
}
