/*
 * Copyright [2022-2024] [Xebisco]
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

package com.xebisco.yield.physics.colliders;

import com.xebisco.yield.Vector2D;
import com.xebisco.yield.physics.PhysicsSystem;
import com.xebisco.yield.physics.Utils;
import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.collision.shapes.Shape;

public class EdgeCollider2D extends Collider2D {

    private Vector2D point1 = new Vector2D(-50, 0), point2 = new Vector2D(50, 0);

    @Override
    public Shape createShape(double ppm) {
        EdgeShape shape = new EdgeShape();
        shape.set(Utils.toVec2(point1.divide(ppm)), Utils.toVec2(point2.divide(ppm)));
        return shape;
    }

    @Override
    public void updateShape(Shape shape, double ppm) {
        ((EdgeShape) shape).set(Utils.toVec2(point1.divide(ppm)), Utils.toVec2(point2.divide(ppm)));
    }

    public Vector2D point1() {
        return point1;
    }

    public EdgeCollider2D setPoint1(Vector2D point1) {
        this.point1 = point1;
        return this;
    }

    public Vector2D point2() {
        return point2;
    }

    public EdgeCollider2D setPoint2(Vector2D point2) {
        this.point2 = point2;
        return this;
    }
}
