package com.xebisco.yield.physics;

import com.xebisco.yield.ComponentBehavior;
import com.xebisco.yield.ContextTime;
import com.xebisco.yield.Vector2D;
import com.xebisco.yield.physics.colliders.Collider2D;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PhysicsBody extends ComponentBehavior {
    private BodyType type = BodyType.STATIC;
    private Body b2Body;
    private double linearDamping = 0.0F;
    private double angularDamping = 0.0F;
    private boolean allowSleep = true;
    private boolean fixedRotation = false;
    private boolean bullet = false;
    private double gravityScale = 1;
    private PhysicsSystem physicsSystem;

    private final List<ContactListener> contactListeners = new ArrayList<>();

    @Override
    public void onCreate() {
        physicsSystem = application().scene().system(PhysicsSystem.class);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = switch (type) {
            case STATIC -> org.jbox2d.dynamics.BodyType.STATIC;
            case DYNAMIC -> org.jbox2d.dynamics.BodyType.DYNAMIC;
            case KINEMATIC -> org.jbox2d.dynamics.BodyType.KINEMATIC;
        };
        bodyDef.linearDamping = (float) linearDamping;
        bodyDef.angularDamping = (float) angularDamping;
        bodyDef.allowSleep = allowSleep;
        bodyDef.fixedRotation = fixedRotation;
        bodyDef.bullet = bullet;
        bodyDef.gravityScale = (float) gravityScale;
        bodyDef.userData = this;
        bodyDef.position = Utils.toVec2(transform().position().multiply(physicsSystem.ppm()));
        bodyDef.angle = (float) transform().zRotation();
        b2Body = physicsSystem.b2World().createBody(bodyDef);
        for (ComponentBehavior componentBehavior : entity().components()) {
            if (componentBehavior instanceof Collider2D c) {
                FixtureDef f = new FixtureDef();
                f.shape = c.createShape(physicsSystem.ppm());
                f.density = (float) c.density();
                f.userData = c;
                f.restitution = (float) c.restitution();
                f.friction = (float) c.friction();
                f.filter.categoryBits = c.collisionMask();
                f.filter.maskBits = 0xFFFF;
                for (int o : c.excludedMasks()) {
                    f.filter.maskBits &= ~o;
                }
                b2Body.createFixture(f);
            }
        }
    }

    @Override
    public void close() {
        physicsSystem.b2World().destroyBody(b2Body);
    }

    public void applyForce(Vector2D force) {
        b2Body.applyForceToCenter(Utils.toVec2(force));
    }

    @Override
    public void onUpdate(ContextTime time) {
        Fixture fixture = b2Body.m_fixtureList;
        for (int i = 0; i < b2Body.m_fixtureCount; i++) {
            ((Collider2D) fixture.getUserData()).updateShape(fixture.m_shape, physicsSystem.ppm());
            fixture = fixture.getNext();
        }
    }

    @Override
    public void onLateUpdate(ContextTime time) {
        transform().position().set(b2Body.getPosition().x * physicsSystem.ppm(), b2Body.getPosition().y * physicsSystem.ppm());
        transform().setzRotation(b2Body.getAngle());
    }

    public void addContactListener(ContactListener contactListener) {
        contactListeners.add(contactListener);
    }

    public BodyType type() {
        return type;
    }

    public PhysicsBody setType(BodyType type) {
        this.type = type;
        return this;
    }

    public Body b2Body() {
        return b2Body;
    }

    public PhysicsBody setB2Body(Body b2Body) {
        this.b2Body = b2Body;
        return this;
    }

    public double linearDamping() {
        return linearDamping;
    }

    public PhysicsBody setLinearDamping(double linearDamping) {
        if(b2Body != null) b2Body.setLinearDamping((float) linearDamping);
        this.linearDamping = linearDamping;
        return this;
    }

    public double angularDamping() {
        return angularDamping;
    }

    public PhysicsBody setAngularDamping(double angularDamping) {
        if(b2Body != null) b2Body.setAngularDamping((float) linearDamping);
        this.angularDamping = angularDamping;
        return this;
    }

    public boolean allowSleep() {
        return allowSleep;
    }

    public PhysicsBody setAllowSleep(boolean allowSleep) {
        if(b2Body != null) b2Body.setSleepingAllowed(allowSleep);
        this.allowSleep = allowSleep;
        return this;
    }

    public boolean fixedRotation() {
        return fixedRotation;
    }

    public PhysicsBody setFixedRotation(boolean fixedRotation) {
        if(b2Body != null) b2Body.setFixedRotation(fixedRotation);
        this.fixedRotation = fixedRotation;
        return this;
    }

    public boolean bullet() {
        return bullet;
    }

    public PhysicsBody setBullet(boolean bullet) {
        if(b2Body != null) b2Body.setBullet(bullet);
        this.bullet = bullet;
        return this;
    }

    public double gravityScale() {
        return gravityScale;
    }

    public PhysicsBody setGravityScale(double gravityScale) {
        if(b2Body != null) b2Body.setGravityScale((float) gravityScale);
        this.gravityScale = gravityScale;
        return this;
    }

    public List<ContactListener> contactListeners() {
        return contactListeners;
    }
}
