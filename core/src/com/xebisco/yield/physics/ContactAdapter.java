package com.xebisco.yield.physics;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.contacts.Contact;

public abstract class ContactAdapter {
    public abstract void onContactBegin(Collider entity, Collider colliding);

    public abstract void onContactEnd(Collider entity, Collider colliding);

    void preSolve(Contact var1, Manifold var2) {
    }

    void postSolve(Contact var1, ContactImpulse var2) {
    }
}
