package com.xebisco.yield.physics;

import com.xebisco.yield.physics.colliders.Collider2D;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.contacts.Contact;

public abstract class CollisionListener implements org.jbox2d.callbacks.ContactListener {
    @Override
    public final void beginContact(Contact contact) {
        onCollisionEnter((Collider2D) contact.getFixtureA().m_userData, (Collider2D) contact.getFixtureB().m_userData);
    }

    public abstract void onCollisionEnter(Collider2D o1, Collider2D o2);
    public abstract void onCollisionExit(Collider2D o1, Collider2D o2);

    @Override
    public final void endContact(Contact contact) {
        onCollisionExit((Collider2D) contact.getFixtureA().m_userData, (Collider2D) contact.getFixtureB().m_userData);
    }

    @Override
    public void preSolve(Contact contact, Manifold manifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse) {

    }
}
