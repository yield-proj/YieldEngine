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
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;

/**
 * The TriangleCollider class represents a triangle shape for collision detection, with adjustable
 * vertices and centroid.
 */
@ComponentIcon(iconType = ComponentIconType.PHYSICS)
public class TriangleCollider extends Collider {
    @VisibleOnEditor
    private final Vector2D[] vertices = new Vector2D[]{new Vector2D(-50, -50), new Vector2D(0, 50), new Vector2D(50, -50)};


    @VisibleOnEditor
    private Vector2D centroid = new Vector2D();

    @Override
    public Shape shape() {
        PolygonShape s = new PolygonShape();
        Vector2D dppm = new Vector2D(application().physicsPpm(), application().physicsPpm());
        if (!ignoreScaling()) {
            s.set(new Vec2[]{Global.toVec2(vertices[0].divide(dppm).multiply(transform().scale().absolute())), Global.toVec2(vertices[1].divide(dppm).multiply(transform().scale().absolute())), Global.toVec2(vertices[2].divide(dppm).multiply(transform().scale()))}, 3);
        } else {
            s.set(new Vec2[]{Global.toVec2(vertices[0].divide(dppm)), Global.toVec2(vertices[1].divide(dppm)), Global.toVec2(vertices[2].divide(dppm))}, 3);
        }
        s.m_centroid.set(Global.toVec2(centroid.divide(new Vector2D(application().physicsPpm(), application().physicsPpm()))));
        return s;
    }

    /**
     * The function returns an array of Vector2D objects representing the vertices.
     *
     * @return An array of Vector2D objects named "vertices" is being returned.
     */
    public Vector2D[] getVertices() {
        return vertices;
    }

    /**
     * The function returns the centroid of the triangle collider.
     *
     * @return A Vector2D object representing the centroid.
     */
    public Vector2D getCentroid() {
        return centroid;
    }

    /**
     * This function sets the centroid of the triangle collider.
     *
     * @param centroid The parameter "centroid" is a Vector2D object representing the centroid of  the triangle collider. The method "setCentroid" sets the value of the centroid for the current object to the value
     *                 passed as the parameter.
     */
    public void setCentroid(Vector2D centroid) {
        this.centroid = centroid;
    }
}
