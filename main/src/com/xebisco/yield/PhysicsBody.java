package com.xebisco.yield;

import com.xebisco.yield.exceptions.MissingPhysicsSystemException;
import com.xebisco.yield.systems.PhysicsSystem;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.dynamics.*;

import java.util.ArrayList;
import java.util.List;

public class PhysicsBody extends YldScript {
    private Vector2 linearVelocity = new Vector2();
    private float angularDamping = .8f, linearDamping = .9f, inertiaScale = 1f, angle, angularVelocity;
    private PhysicsBodyType physicsBodyType = PhysicsBodyType.DYNAMIC;
    private boolean fixedRotation, continuousCollision = true;
    private Body box2dBody;

    public PhysicsBody() {

    }

    public PhysicsBody(PhysicsBodyType physicsBodyType) {
        this.physicsBodyType = physicsBodyType;
    }

    @Override
    public void start() {
        PhysicsSystem physicsSystem = scene.getSystem(PhysicsSystem.class);
        if (physicsSystem == null)
            throw new MissingPhysicsSystemException();
        else {
            reloadObject();
            resetColliders();
        }
    }

    public void reloadObject() {
        World world = scene.getSystem(PhysicsSystem.class).getBox2dWorld();
        if (box2dBody != null)
            world.destroyBody(box2dBody);
        BodyDef bodyDef = new BodyDef();
        bodyDef.angle = transform.rotation;
        bodyDef.position.set(Yld.toVec2(transform.position));
        bodyDef.angularDamping = angularDamping;
        bodyDef.linearDamping = linearDamping;
        bodyDef.fixedRotation = fixedRotation;
        bodyDef.bullet = continuousCollision;
        bodyDef.type = BodyType.valueOf(physicsBodyType.name());
        bodyDef.linearVelocity = Yld.toVec2(linearVelocity);
        bodyDef.userData = getEntity();
        bodyDef.inertiaScale = inertiaScale;
        bodyDef.angle = angle;
        bodyDef.angularVelocity = angularVelocity;
        box2dBody = world.createBody(bodyDef);
        if (box2dBody == null)
            reloadObject();
    }

    public void resetColliders() {
        if (box2dBody == null)
            return;
        List<Collider> colliders = new ArrayList<>();
        for (Component c : getComponents()) {
            if (c instanceof Collider) {
                colliders.add((Collider) c);
            }
        }
        int size = 0;
        Fixture fixture = box2dBody.getFixtureList();
        while (fixture != null) {
            size++;
            fixture = fixture.m_next;
        }
        for (int i = 0; i < size; i++) {
            box2dBody.destroyFixture(box2dBody.getFixtureList());
        }

        for (Collider collider : colliders) {
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = collider.shape();
            fixtureDef.density = collider.getMass();
            fixtureDef.friction = collider.getFriction();
            fixtureDef.isSensor = collider.isSensor();
            fixtureDef.userData = collider.getEntity();
            box2dBody.createFixture(fixtureDef);
        }
        box2dBody.resetMassData();
    }

    @Override
    public void update(float delta) {
        if (box2dBody != null) {
            linearVelocity = Yld.toVector2(box2dBody.getLinearVelocity());
            transform.goTo(box2dBody.getPosition().x, box2dBody.getPosition().y);
            transform.rotation = (float) -Math.toDegrees(box2dBody.getAngle());
        }
    }

    public void goTo(Vector2 location) {
        box2dBody.getPosition().set(Yld.toVec2(location));
    }

    public void addImpulse(Vector2 value, ImpulseType impulseType) {
        switch (impulseType) {
            case FORCE:
                box2dBody.applyForce(Yld.toVec2(value), box2dBody.getWorldCenter());
                break;
            case LINEAR:
                box2dBody.applyLinearImpulse(Yld.toVec2(value), box2dBody.getWorldCenter());
                break;
        }
    }

    public void addLinearVelocity(Vector2 value) {
        box2dBody.setLinearVelocity(box2dBody.m_linearVelocity.add(Yld.toVec2(value)));
    }

    public void setAngularVelocity(float angularVelocity) {
        this.angularVelocity = angularVelocity;
        if (box2dBody != null) {
            box2dBody.setAngularVelocity(angularVelocity);
        }
    }

    public void setLinearVelocity(Vector2 linearVelocity) {
        this.linearVelocity = linearVelocity;
        if (box2dBody != null) {
            box2dBody.setLinearVelocity(Yld.toVec2(linearVelocity));
        }
    }

    public void addImpulse(Vector2 value) {
        addImpulse(value, ImpulseType.LINEAR);
    }

    public Vector2 getLinearVelocity() {
        if (box2dBody != null)
            return Yld.toVector2(box2dBody.getLinearVelocity());
        else
            return linearVelocity;
    }

    public float getAngularDamping() {
        return angularDamping;
    }

    public void setAngularDamping(float angularDamping) {
        this.angularDamping = angularDamping;
    }

    public float getLinearDamping() {
        return linearDamping;
    }

    public void setLinearDamping(float linearDamping) {
        this.linearDamping = linearDamping;
    }

    public PhysicsBodyType getPhysicsBodyType() {
        return physicsBodyType;
    }

    public void setPhysicsBodyType(PhysicsBodyType physicsBodyType) {
        this.physicsBodyType = physicsBodyType;
    }

    public boolean isFixedRotation() {
        return fixedRotation;
    }

    public void setFixedRotation(boolean fixedRotation) {
        this.fixedRotation = fixedRotation;
    }

    public boolean isContinuousCollision() {
        return continuousCollision;
    }

    public void setContinuousCollision(boolean continuousCollision) {
        this.continuousCollision = continuousCollision;
    }

    public Body getBox2dBody() {
        return box2dBody;
    }

    public void setBox2dBody(Body box2dBody) {
        this.box2dBody = box2dBody;
    }

    public float getInertiaScale() {
        return inertiaScale;
    }

    public void setInertiaScale(float inertiaScale) {
        this.inertiaScale = inertiaScale;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }
}
