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
import com.xebisco.yield.VisibleOnInspector;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;

public class TriangleCollider extends Collider {
    private final Vector2D[] vertices = new Vector2D[] {new Vector2D(-50, -50), new Vector2D(0, 50), new Vector2D(50, -50)};


    @VisibleOnInspector
    private Vector2D centroid = new Vector2D();

    @Override
    public Shape getShape() {
        PolygonShape s = new PolygonShape();
        TwoAnchorRepresentation dppm = new TwoAnchorRepresentation(getApplication().getPhysicsPpm(), getApplication().getPhysicsPpm());
        s.set(new Vec2[]{Global.toVec2(vertices[0].divide(dppm).multiply(getTransform().getScale().absolute())), Global.toVec2(vertices[1].divide(dppm).multiply(getTransform().getScale().absolute())), Global.toVec2(vertices[2].divide(dppm).multiply(getTransform().getScale()))}, 3);
        s.m_centroid.set(Global.toVec2(centroid.divide(new TwoAnchorRepresentation(getApplication().getPhysicsPpm(), getApplication().getPhysicsPpm()))));
        return s;
    }

    public Vector2D[] getVertices() {
        return vertices;
    }

    public Vector2D getCentroid() {
        return centroid;
    }

    public void setCentroid(Vector2D centroid) {
        this.centroid = centroid;
    }
}
