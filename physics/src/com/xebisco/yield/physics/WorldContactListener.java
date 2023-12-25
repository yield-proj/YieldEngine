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
