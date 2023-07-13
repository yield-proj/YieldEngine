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

import com.xebisco.yield.Entity2D;
import com.xebisco.yield.Global;
import com.xebisco.yield.SystemBehavior;
import com.xebisco.yield.Vector2D;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;

/**
 * The PhysicsSystem class is a Java class that handles physics simulation using the Box2D engine.
 */
public class PhysicsSystem extends SystemBehavior {
    private World b2World;
    private Vector2D gravity = new Vector2D(0, -10);
    private int velocityIterations = 6;
    private int positionIterations = 2;

    @Override
    public void onStart() {
        if (gravity == null)
            gravity = new Vector2D(0, -10);
        b2World = new World(Global.toVec2(gravity));


        b2World.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Collider c = ((Collider) contact.m_fixtureA.getUserData());
                c.getEntity().getContactListeners().forEach(l -> l.onContactBegin(c, ((Collider) contact.m_fixtureB.getUserData())));
            }

            @Override
            public void endContact(Contact contact) {
                Collider c = ((Collider) contact.m_fixtureA.getUserData());
                c.getEntity().getContactListeners().forEach(l -> l.onContactEnd(c, ((Collider) contact.m_fixtureB.getUserData())));
            }

            @Override
            public void preSolve(Contact contact, Manifold manifold) {
                Collider c = ((Collider) contact.m_fixtureA.getUserData());
                c.getEntity().getContactListeners().forEach(l -> l.preSolve(contact, manifold));
            }

            @Override
            public void postSolve(Contact contact, ContactImpulse contactImpulse) {
                Collider c = ((Collider) contact.m_fixtureA.getUserData());
                c.getEntity().getContactListeners().forEach(l -> l.postSolve(contact, contactImpulse));
            }
        });
    }

    /**
     * Raycast from point1 to point2 and return the closest hit.
     *
     * @param requestingEntity The entity that is requesting the ray-cast.
     * @param point1           The starting point of the ray-cast.
     * @param point2           The end point of the ray.
     * @return A RayCast object.
     */
    public final RayCast rayCast(Entity2D requestingEntity, Vector2D point1, Vector2D point2) {
        RayCast rayCastCallback = new RayCast();
        rayCastCallback.setRequestEntity(requestingEntity);
        b2World.raycast(rayCastCallback, Global.toVec2(point1), Global.toVec2(point2));
        return rayCastCallback;
    }

    @Override
    public void onUpdate() {
        if(b2World.getGravity().x != gravity.getX() || b2World.getGravity().y != gravity.getY())
            b2World.setGravity(Global.toVec2(gravity));
        b2World.step((float) getScene().getApplication().getApplicationManager().getManagerContext().getContextTime().getDeltaTime(), velocityIterations, positionIterations);
    }

    @Override
    public void dispose() {
        b2World = null;
    }

    /**
     * The function returns a Vector2D representing the gravity.
     *
     * @return A Vector2D object representing the gravity.
     */
    public Vector2D getGravity() {
        return gravity;
    }

    /**
     * This function sets the gravity vector for an object.
     *
     * @param gravity The "gravity" parameter is a Vector2D object that represents the gravitational force acting on an
     * object in a 2D space. This method sets the value of the "gravity" instance variable to the provided Vector2D object.
     */
    public void setGravity(Vector2D gravity) {
        this.gravity = gravity;
    }

    /**
     * The function returns a World object.
     *
     * @return The method is returning an object of type "World".
     */
    public World getB2World() {
        return b2World;
    }

    /**
     * This function sets the value of a variable named "b2World" to the value passed as a parameter.
     *
     * @param b2World b2World is an object of the Box2D physics engine's World class. It represents the simulation world in
     * which physical bodies interact with each other according to the laws of physics.
     */
    public void setB2World(World b2World) {
        this.b2World = b2World;
    }

    /**
     * This function returns the number of velocity iterations used in a physics simulation.
     *
     * @return The method is returning the value of the variable `velocityIterations`.
     */
    public int getVelocityIterations() {
        return velocityIterations;
    }

    /**
     * This function sets the number of velocity iterations for a physics simulation in Box2D.
     *
     * @param velocityIterations The parameter "velocityIterations" is an integer value that represents the number of
     * iterations the physics engine will perform to calculate the velocity of objects in a simulation. Increasing the
     * number of velocity iterations can improve the accuracy of the simulation, but it can also increase the computational
     * cost.
     */
    public void setVelocityIterations(int velocityIterations) {
        this.velocityIterations = velocityIterations;
    }

    /**
     * The function returns the value of the variable positionIterations.
     *
     * @return The method `getPositionIterations()` is returning an integer value, which is the value of the variable
     * `positionIterations`.
     */
    public int getPositionIterations() {
        return positionIterations;
    }

    /**
     * This function sets the number of position iterations for a physics simulation in Box2D.
     *
     * @param positionIterations positionIterations is an integer parameter that represents the number of iterations the
     * physics engine will perform to calculate the positions of objects in a simulation. Increasing the number of position
     * iterations can improve the accuracy of the simulation, but it can also increase the computational cost.
     */
    public void setPositionIterations(int positionIterations) {
        this.positionIterations = positionIterations;
    }
}
