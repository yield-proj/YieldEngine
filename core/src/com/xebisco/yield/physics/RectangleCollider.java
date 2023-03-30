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

    public Size2D getSize() {
        return size;
    }

    public void setSize(Size2D size) {
        this.size = size;
    }

    public Vector2D getCentroid() {
        return centroid;
    }

    public void setCentroid(Vector2D centroid) {
        this.centroid = centroid;
    }

    public Vector2D getCenter() {
        return center;
    }

    public void setCenter(Vector2D center) {
        this.center = center;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }
}
