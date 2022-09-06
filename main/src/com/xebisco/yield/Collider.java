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

import org.jbox2d.collision.shapes.Shape;

/**
 * It's a base class for all colliders
 */
public abstract class Collider extends YldScript {
    private Vector2 offset = new Vector2();
    private boolean sensor;
    private float density = 1f, friction = 1f;

    /**
     * Return a Shape object.
     *
     * @return A Shape object.
     */
    public abstract Shape shape();

    /**
     * This function returns the offset of the collider object.
     *
     * @return The offset of the collider.
     */
    public Vector2 getOffset() {
        return offset;
    }

    /**
     * This function sets the offset of the collider.
     *
     * @param offset The offset of the collider from the center of the entity.
     */
    public void setOffset(Vector2 offset) {
        this.offset = offset;
    }

    /**
     * Returns true if the sensor is enabled, false otherwise
     *
     * @return The boolean value of the sensor variable.
     */
    public boolean isSensor() {
        return sensor;
    }

    /**
     * This function sets the sensor variable to the value of the parameter.
     *
     * @param sensor If the collider will be a sensor or a physics object.
     */
    public void setSensor(boolean sensor) {
        this.sensor = sensor;
    }

    /**
     * Returns the collider density.
     *
     * @return The density of the collider.
     */
    public float getDensity() {
        return density;
    }

    /**
     * This function sets the density of the object.
     *
     * @param density The density of the collider.
     */
    public void setDensity(float density) {
        this.density = density;
    }

    /**
     * Returns the friction of the collider.
     *
     * @return The friction variable is being returned.
     */
    public float getFriction() {
        return friction;
    }

    /**
     * This function sets the friction of the object to the value of the parameter.
     *
     * @param friction The friction of the object.
     */
    public void setFriction(float friction) {
        this.friction = friction;
    }
}
