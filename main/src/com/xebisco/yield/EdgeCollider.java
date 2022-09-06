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

import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;

/**
 * It's a collider that represents a line
 */
public class EdgeCollider extends Collider {
    private Vector2 point1, point2;

    public EdgeCollider(Vector2 point1, Vector2 point2) {
        this.point1 = point1;
        this.point2 = point2;
    }

    @Override
    public Shape shape() {
        EdgeShape shape = new EdgeShape();
        shape.set(Yld.toVec2(point1.div(scene.getPpm())), Yld.toVec2(point2.div(scene.getPpm())));
        return shape;
    }

    /**
     * This function returns the value of the point1 variable.
     *
     * @return The first point of the line.
     */
    public Vector2 getPoint1() {
        return point1;
    }

    /**
     * This function sets the value of the point1 variable to the value of the point1 parameter.
     *
     * @param point1 The first point of the line.
     */
    public void setPoint1(Vector2 point1) {
        this.point1 = point1;
    }

    /**
     * This function returns the value of the point2 variable.
     *
     * @return The second point of the line.
     */
    public Vector2 getPoint2() {
        return point2;
    }

    /**
     * This function sets the value of the point2 variable to the value of the point2 parameter.
     *
     * @param point2 The second point of the line.
     */
    public void setPoint2(Vector2 point2) {
        this.point2 = point2;
    }
}
