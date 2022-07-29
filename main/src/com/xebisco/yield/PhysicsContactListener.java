package com.xebisco.yield;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.WorldManifold;
import org.jbox2d.dynamics.contacts.Contact;

public class PhysicsContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Entity eA = (Entity) contact.getFixtureA().m_userData, eB = (Entity) contact.getFixtureB().m_userData;
        WorldManifold worldManifold = new WorldManifold();
        contact.getWorldManifold(worldManifold);
        Vector2 aNormal = new Vector2(worldManifold.normal.x, worldManifold.normal.y), bNormal = aNormal.mul(-1f);
        for (Component c : eA.getComponents()) {
            c.onCollisionEnter(eB, aNormal);
            c.onCollisionEnter(eB);
        }
        for (Component c : eB.getComponents()) {
            c.onCollisionEnter(eA, bNormal);
            c.onCollisionEnter(eA);
        }
    }

    @Override
    public void endContact(Contact contact) {
        Entity eA = (Entity) contact.getFixtureA().m_userData, eB = (Entity) contact.getFixtureB().m_userData;
        WorldManifold worldManifold = new WorldManifold();
        contact.getWorldManifold(worldManifold);
        Vector2 aNormal = new Vector2(worldManifold.normal.x, worldManifold.normal.y), bNormal = aNormal.mul(-1f);
        for (Component c : eA.getComponents()) {
            c.onCollisionExit(eB, aNormal);
            c.onCollisionExit(eB);
        }
        for (Component c : eB.getComponents()) {
            c.onCollisionExit(eA, bNormal);
            c.onCollisionExit(eA);
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold manifold) {
        Entity eA = (Entity) contact.getFixtureA().m_userData, eB = (Entity) contact.getFixtureB().m_userData;
        WorldManifold worldManifold = new WorldManifold();
        contact.getWorldManifold(worldManifold);
        Vector2 aNormal = new Vector2(worldManifold.normal.x, worldManifold.normal.y), bNormal = aNormal.mul(-1f);
        for (Component c : eA.getComponents()) {
            c.preSolveCollision(eB, aNormal);
            c.preSolveCollision(eB);
        }
        for (Component c : eB.getComponents()) {
            c.preSolveCollision(eA, bNormal);
            c.preSolveCollision(eA);
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse) {
        Entity eA = (Entity) contact.getFixtureA().m_userData, eB = (Entity) contact.getFixtureB().m_userData;
        WorldManifold worldManifold = new WorldManifold();
        contact.getWorldManifold(worldManifold);
        Vector2 aNormal = new Vector2(worldManifold.normal.x, worldManifold.normal.y), bNormal = aNormal.mul(-1f);
        for (Component c : eA.getComponents()) {
            c.postSolveCollision(eB, aNormal);
            c.postSolveCollision(eB);
        }
        for (Component c : eB.getComponents()) {
            c.postSolveCollision(eA, bNormal);
            c.postSolveCollision(eA);
        }
    }
}
