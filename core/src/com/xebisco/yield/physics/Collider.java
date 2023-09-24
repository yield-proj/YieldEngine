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
import org.jbox2d.collision.shapes.Shape;

import java.util.HashSet;
import java.util.Set;

/**
 * The abstract class Collider defines properties and methods for objects that can collide with other objects in a game or
 * simulation.
 */
@HideComponent
public abstract class Collider extends ComponentBehavior {
    @VisibleOnEditor
    private double density = 1, friction = 1;

    @VisibleOnEditor
    private Set<String> collisionFilter = new HashSet<>();

    @VisibleOnEditor
    private String collisionMask = "default";
    @VisibleOnEditor
    private boolean sensor, ignoreScaling;

    /**
     * This function returns a Shape object and is declared as abstract.
     * This shape will be used in PhysicsBodies when creating its bodies fixtures in Box2D.
     *
     * @return An abstract Shape object is being returned.
     */
    public abstract Shape shape();

    /**
     * The function returns the value of the density variable as a double.
     *
     * @return The method is returning a double value, which is the value of the variable "density".
     */
    public double density() {
        return density;
    }

    /**
     * This function sets the density value of an object.
     *
     * @param density The parameter "density" is a double data type that represents the density of a fixture.
     *                The method "setDensity" sets the value of the density variable to the value passed as an argument to the method.
     */
    public Collider setDensity(double density) {
        this.density = density;
        return this;
    }

    /**
     * The function returns the value of the friction.
     *
     * @return The method is returning a double value which represents the friction.
     */
    public double friction() {
        return friction;
    }

    /**
     * This function sets the friction value of an object.
     *
     * @param friction The "friction" parameter is a double data type that represents the amount of resistance between two
     *                 surfaces in contact with each other. This value will be used to determine how
     *                 much an object will slow down or stop when it comes into contact with another object or surface.
     */
    public Collider setFriction(double friction) {
        this.friction = friction;
        return this;
    }

    /**
     * The function returns a boolean value indicating whether the collider is a sensor or not.
     *
     * @return The method `sensor()` is returning a boolean value, which indicates whether the object is a sensor or not.
     * The value returned will be `true` if the object is a sensor, and `false` otherwise.
     */
    public boolean sensor() {
        return sensor;
    }

    /**
     * The function sets the value of a boolean variable called "sensor".
     *
     * @param sensor The "sensor" parameter is a boolean variable that represents whether a collider is a sensor or not.
     */
    public Collider setSensor(boolean sensor) {
        this.sensor = sensor;
        return this;
    }

    /**
     * The function returns a set of Strings representing a collision filter.
     *
     * @return A Set of Strings named "collisionFilter" is being returned.
     */
    public Set<String> collisionFilter() {
        return collisionFilter;
    }

    /**
     * This function sets the collision filter for an object.
     *
     * @param collisionFilter The parameter "collisionFilter" is a Set of Strings that is used to filter collisions. It contains the IDs of the objects that should be checked for collisions. Objects with IDs
     *                        that are present in the collisionFilter will be ignored in collisions.
     */
    public Collider setCollisionFilter(Set<String> collisionFilter) {
        this.collisionFilter = collisionFilter;
        return this;
    }

    /**
     * The function returns the collision mask as a String value.
     *
     * @return The method is returning a String value which represents the collision mask.
     */
    public String collisionMask() {
        return collisionMask;
    }

    /**
     * This function sets the collision mask of an object.
     *
     * @param collisionMask The collisionMask parameter is a String value that represents the ID of this collider when colliding with other objects.
     */
    public Collider setCollisionMask(String collisionMask) {
        this.collisionMask = collisionMask;
        return this;
    }

    public boolean ignoreScaling() {
        return ignoreScaling;
    }

    public Collider setIgnoreScaling(boolean ignoreScaling) {
        this.ignoreScaling = ignoreScaling;
        return this;
    }
}
