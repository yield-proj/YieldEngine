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

package com.xebisco.yield.physics;

import com.xebisco.yield.physics.colliders.Collider2D;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.contacts.Contact;

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        for(ContactListener contactListener : ((Collider2D) contact.m_fixtureA.getUserData()).component(PhysicsBody.class).contactListeners()) {
            contactListener.beginContact(contact);
        }
    }

    @Override
    public void endContact(Contact contact) {
        for(ContactListener contactListener : ((Collider2D) contact.m_fixtureA.getUserData()).component(PhysicsBody.class).contactListeners()) {
            contactListener.endContact(contact);
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold manifold) {
        for(ContactListener contactListener : ((Collider2D) contact.m_fixtureA.getUserData()).component(PhysicsBody.class).contactListeners()) {
            contactListener.preSolve(contact, manifold);
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse) {
        for(ContactListener contactListener : ((Collider2D) contact.m_fixtureA.getUserData()).component(PhysicsBody.class).contactListeners()) {
            contactListener.postSolve(contact, contactImpulse);
        }
    }
}
