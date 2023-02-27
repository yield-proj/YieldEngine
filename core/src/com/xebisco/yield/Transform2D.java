/*
 * Copyright [2022-2023] [Xebisco]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.xebisco.yield;

public class Transform2D {
    private final Point2D position = new Point2D();
    private final Size2D size = new Size2D(100, 100);
    private final Vector2D scale = new Vector2D(1, 1);
    private double zRotation;

    public Point2D getPosition() {
        return position;
    }

    public double getzRotation() {
        return zRotation;
    }

    public void setzRotation(double zRotation) {
        this.zRotation = zRotation;
    }

    public Vector2D getScale() {
        return scale;
    }

    public Size2D getSize() {
        return size;
    }
}