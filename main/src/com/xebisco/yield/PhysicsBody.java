package com.xebisco.yield;

import com.xebisco.yield.engine.YldEngineAction;
import com.xebisco.yield.exceptions.MissingPhysicsSystemException;
import com.xebisco.yield.systems.PhysicsSystem;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;

import java.util.ArrayList;
import java.util.List;

public class PhysicsBody extends YldScript {
    private Vector2 linearVelocity = new Vector2();
    private float angularDamping = .8f, linearDamping = .9f, angle, angularVelocity, mass = 1f, gravityScale = 1f;
    private PhysicsBodyType physicsBodyType = PhysicsBodyType.DYNAMIC;
    private boolean fixedRotation, continuousCollision, allowSleep, rotateEntity = true;
    private Body box2dBody;
    private PhysicsSystem physicsSystem;

    public PhysicsBody() {

    }

    public PhysicsBody(PhysicsBodyType physicsBodyType) {
        this.physicsBodyType = physicsBodyType;
    }

    @Override
    public void start() {
        physicsSystem = scene.getSystem(PhysicsSystem.class);
        if (physicsSystem == null)
            throw new MissingPhysicsSystemException();
        else {
            reloadObject();
            resetColliders();
        }
    }

    public void reloadObject() {
        World world = physicsSystem.getBox2dWorld();
        if (box2dBody != null)
            world.destroyBody(box2dBody);
        BodyDef bodyDef = new BodyDef();
        bodyDef.angle = transform.rotation;
        bodyDef.position.set(Yld.toVec2(transform.position.div(scene.getPpm())));
        bodyDef.angularDamping = angularDamping;
        bodyDef.linearDamping = linearDamping;
        bodyDef.fixedRotation = fixedRotation;
        bodyDef.bullet = continuousCollision;
        bodyDef.allowSleep = allowSleep;
        bodyDef.gravityScale = gravityScale;
        switch (physicsBodyType) {
            case STATIC:
                bodyDef.type = BodyType.STATIC;
                break;
            case DYNAMIC:
                bodyDef.type = BodyType.DYNAMIC;
                break;
            case KINEMATIC:
                bodyDef.type = BodyType.KINEMATIC;
                break;
            default:
                Yld.throwException(new IllegalArgumentException("'" + physicsBodyType.name() + "'"));
        }
        bodyDef.linearVelocity = Yld.toVec2(linearVelocity);
        bodyDef.userData = getEntity();
        bodyDef.gravityScale = gravityScale;
        bodyDef.angle = angle;
        bodyDef.angularVelocity = angularVelocity;
        while (box2dBody == null)
            box2dBody = world.createBody(bodyDef);
    }

    public void resetColliders() {
        if (box2dBody == null)
            Yld.throwException(new IllegalStateException());
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
            fixtureDef.density = collider.getDensity();
            fixtureDef.friction = collider.getFriction();
            fixtureDef.isSensor = collider.isSensor();
            fixtureDef.userData = collider.getEntity();
            box2dBody.createFixture(fixtureDef);
        }
    }

    @Override
    public void update(float delta) {
        if (box2dBody != null) {
            box2dBody.m_mass = mass;
            linearVelocity = Yld.toVector2(box2dBody.getLinearVelocity());
            transform.goTo(box2dBody.getPosition().x * scene.getPpm(), box2dBody.getPosition().y * scene.getPpm());
            if (rotateEntity)
                transform.rotation = (float) -Math.toDegrees(box2dBody.getAngle());
        }
    }

    @Override
    public void onDestroy() {
        if (box2dBody != null)
            physicsSystem.getBox2dWorld().destroyBody(box2dBody);
    }

    public void goTo(Vector2 location) {
        box2dBody.getPosition().set(Yld.toVec2(location));
    }

    public void applyImpulse(Vector2 value, ImpulseType impulseType) {
        switch (impulseType) {
            case FORCE:
                box2dBody.applyForce(Yld.toVec2(value), box2dBody.getPosition());
                break;
            case LINEAR:
                box2dBody.applyLinearImpulse(Yld.toVec2(value), box2dBody.getPosition());
                break;
        }
    }

    public boolean isAllowSleep() {
        return allowSleep;
    }

    public void setAllowSleep(boolean allowSleep) {
        this.allowSleep = allowSleep;
    }

    public float getAngularVelocity() {
        return angularVelocity;
    }

    public float getMass() {
        return mass;
    }

    public void setMass(float mass) {
        this.mass = mass;
        if (box2dBody != null) {
            box2dBody.m_mass = mass;
        }
    }

    public PhysicsSystem getPhysicsSystem() {
        return physicsSystem;
    }

    public void setPhysicsSystem(PhysicsSystem physicsSystem) {
        this.physicsSystem = physicsSystem;
    }

    public void addLinearVelocity(Vector2 value) {
        box2dBody.m_linearVelocity.set(box2dBody.m_linearVelocity.add(Yld.toVec2(value)));
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

    public void applyImpulse(Vector2 value) {
        applyImpulse(value, ImpulseType.LINEAR);
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
        if (box2dBody != null)
            box2dBody.setFixedRotation(fixedRotation);
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

    public float getGravityScale() {
        return gravityScale;
    }

    public void setGravityScale(float gravityScale) {
        this.gravityScale = gravityScale;
        if (box2dBody != null)
            box2dBody.setGravityScale(gravityScale);
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
        if (box2dBody != null)
            box2dBody.setTransform(box2dBody.getPosition(), (float) Math.toRadians(-angle));
    }

    public boolean isRotateEntity() {
        return rotateEntity;
    }

    public void setRotateEntity(boolean rotateEntity) {
        this.rotateEntity = rotateEntity;
    }
}
