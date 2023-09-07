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
import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.collision.shapes.Shape;

/**
 * The EdgeCollider class defines an edge shape collider with two points in a 2D space.
 */
@ComponentIcon(iconType = ComponentIconType.PHYSICS)
public class EdgeCollider extends Collider {
    @VisibleOnEditor
    private Vector2D point1 = new Vector2D(-50, 0), point2 = new Vector2D(50, 0);

    @Override
    public Shape shape() {
        EdgeShape s = new EdgeShape();
        if(!ignoreScaling()) {
            s.set(Global.toVec2(point1.divide(new Vector2D(application().physicsPpm(), application().physicsPpm())).multiply(transform().scale().absolute())), Global.toVec2(point2.divide(new Vector2D(application().physicsPpm(), application().physicsPpm())).multiply(transform().scale().absolute())));
        } else {
            s.set(Global.toVec2(point1.divide(new Vector2D(application().physicsPpm(), application().physicsPpm()))), Global.toVec2(point2.divide(new Vector2D(application().physicsPpm(), application().physicsPpm()))));
        }
        return s;
    }

    /**
     * The function returns the value of point1 as a Vector2D object.
     *
     * @return The method `getPoint1()` is returning a `Vector2D` object, specifically the `point1` instance variable.
     */
    public Vector2D getPoint1() {
        return point1;
    }

    /**
     * This function sets the value of a Vector2D object called point1.
     *
     * @param point1 The parameter `point1` is a Vector2D object representing a point in a two-dimensional space. The
     * method `setPoint1` sets the value of the instance variable `point1` to the value of the parameter `point1`.
     */
    public void setPoint1(Vector2D point1) {
        this.point1 = point1;
    }

    /**
     * The function returns the value of the point2 vector.
     *
     * @return A Vector2D object representing the second point.
     */
    public Vector2D getPoint2() {
        return point2;
    }

    /**
     * This function sets the value of a Vector2D object called point2.
     *
     * @param point2 point2 is a variable of type Vector2D that represents the second point in a line segment. The method
     * setPoint2() sets the value of this variable to the value passed as a parameter.
     */
    public void setPoint2(Vector2D point2) {
        this.point2 = point2;
    }
}
