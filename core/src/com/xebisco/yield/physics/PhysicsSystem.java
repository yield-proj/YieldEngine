package com.xebisco.yield.physics;

import com.xebisco.yield.Global;
import com.xebisco.yield.SystemBehavior;
import com.xebisco.yield.Vector2D;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;

public class PhysicsSystem extends SystemBehavior {
    private World b2World;
    private Vec2 gravity;
    private int velocityIterations = 4;
    private int positionIterations = 3;

    @Override
    public void onStart() {
        gravity = new Vec2(0, -10);
        b2World = new World(gravity);
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

    @Override
    public void onUpdate() {
        b2World.step((float) getScene().getApplication().getApplicationManager().getManagerContext().getContextTime().getDeltaTime(), velocityIterations, positionIterations);
    }

    public Vec2 getGravity() {
        return gravity;
    }

    public void setGravity(Vec2 gravity) {
        this.gravity = gravity;
    }

    public Vector2D getGravity2D() {
        return Global.toVector2D(gravity);
    }

    public void setGravity2D(Vector2D gravity) {
        this.gravity = Global.toVec2(gravity);
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
