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

import com.xebisco.yield.ComponentBehavior;
import org.jbox2d.collision.shapes.Shape;

public abstract class Collider2D extends ComponentBehavior {
    private double density = 1, restitution = 0, friction = 0.2;
    private int collisionMask = 1;
    private int[] excludedMasks = new int[0];


    public abstract Shape createShape(double ppm);
    public abstract void updateShape(Shape shape, double ppm);

    public double density() {
        return density;
    }

    public Collider2D setDensity(double density) {
        this.density = density;
        return this;
    }

    public double restitution() {
        return restitution;
    }

    public Collider2D setRestitution(double restitution) {
        this.restitution = restitution;
        return this;
    }

    public double friction() {
        return friction;
    }

    public Collider2D setFriction(double friction) {
        this.friction = friction;
        return this;
    }

    public int collisionMask() {
        return collisionMask;
    }

    public Collider2D setCollisionMask(int collisionMask) {
        this.collisionMask = collisionMask;
        return this;
    }

    public int[] excludedMasks() {
        return excludedMasks;
    }

    public Collider2D setExcludedMasks(int[] excludedMasks) {
        this.excludedMasks = excludedMasks;
        return this;
    }
}
