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
import com.xebisco.yield.Point2D;
import com.xebisco.yield.TwoAnchorRepresentation;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;

public class TriangleCollider extends Collider {
    private final Point2D[] vertices = new Point2D[] {new Point2D(-50, -50), new Point2D(0, 50), new Point2D(50, -50)};

    @Override
    public Shape getShape() {
        PolygonShape s = new PolygonShape();
        TwoAnchorRepresentation dppm = new TwoAnchorRepresentation(getApplication().getPhysicsPpm(), getApplication().getPhysicsPpm());
        s.set(new Vec2[]{Global.toVec2(vertices[0].divide(dppm).multiply(getTransform().getScale())), Global.toVec2(vertices[1].divide(dppm).multiply(getTransform().getScale())), Global.toVec2(vertices[2].divide(dppm).multiply(getTransform().getScale()))}, 3);
        return s;
    }

    public Point2D[] getVertices() {
        return vertices;
    }
}
