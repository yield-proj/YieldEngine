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

public class PhysicsSystem extends SystemBehavior {
    private World b2World;
    private Vector2D gravity;
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
                if (c.getEntity().getContactAdapter() != null)
                    c.getEntity().getContactAdapter().onContactBegin(c, ((Collider) contact.m_fixtureB.getUserData()));
            }

            @Override
            public void endContact(Contact contact) {
                Collider c = ((Collider) contact.m_fixtureA.getUserData());
                if (c.getEntity().getContactAdapter() != null)
                    c.getEntity().getContactAdapter().onContactEnd(c, ((Collider) contact.m_fixtureB.getUserData()));
            }

            @Override
            public void preSolve(Contact contact, Manifold manifold) {
                Collider c = ((Collider) contact.m_fixtureA.getUserData());
                if (c.getEntity().getContactAdapter() != null)
                    c.getEntity().getContactAdapter().preSolve(contact, manifold);
            }

            @Override
            public void postSolve(Contact contact, ContactImpulse contactImpulse) {
                Collider c = ((Collider) contact.m_fixtureA.getUserData());
                if (c.getEntity().getContactAdapter() != null)
                    c.getEntity().getContactAdapter().postSolve(contact, contactImpulse);
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
        b2World.setGravity(Global.toVec2(gravity));
        b2World.step((float) getScene().getApplication().getApplicationManager().getManagerContext().getContextTime().getDeltaTime(), velocityIterations, positionIterations);
    }

    public Vector2D getGravity() {
        return gravity;
    }

    public void setGravity(Vector2D gravity) {
        this.gravity = gravity;
    }

    public Vector2D getGravity2D() {
        return gravity;
    }

    public World getB2World() {
        return b2World;
    }

    public void setB2World(World b2World) {
        this.b2World = b2World;
    }

    public int getVelocityIterations() {
        return velocityIterations;
    }

    public void setVelocityIterations(int velocityIterations) {
        this.velocityIterations = velocityIterations;
    }

    public int getPositionIterations() {
        return positionIterations;
    }

    public void setPositionIterations(int positionIterations) {
        this.positionIterations = positionIterations;
    }
}
