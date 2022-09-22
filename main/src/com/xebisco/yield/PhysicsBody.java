package com.xebisco.yield;

import com.xebisco.yield.exceptions.MissingPhysicsSystemException;
import com.xebisco.yield.systems.PhysicsSystem;
import org.jbox2d.dynamics.*;

import java.util.ArrayList;
import java.util.List;

/**
 * It's a class that allows you to add physics to your entities
 */
public class PhysicsBody extends YldScript {
    private Vector2 linearVelocity = new Vector2();
    private float angularDamping = .8f, linearDamping = .9f, angle, angularVelocity, mass = 1f, gravityScale = 1f;
    private PhysicsBodyType physicsBodyType = PhysicsBodyType.DYNAMIC;
    private boolean fixedRotation, continuousCollision, allowSleep, rotateEntity = true, intValuePosition, autoUpdateEntity = true;
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
            resetBody();
            resetColliders();
        }
    }

    /**
     * It creates a new Box2D body with the same properties as the old one.
     */
    public void resetBody() {
        World world = physicsSystem.getBox2dWorld();
        if (box2dBody != null)
            world.destroyBody(box2dBody);
        BodyDef bodyDef = new BodyDef();
        bodyDef.angle = transform.rotation;
        bodyDef.position.set(Yld.toVec2(getTransform().position.div(scene.getPpm())));
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

    /**
     * > Destroy all the fixtures attached to the body
     */
    public void destroyFixtures() {
        int size = 0;
        Fixture fixture = box2dBody.getFixtureList();
        while (fixture != null) {
            size++;
            fixture = fixture.m_next;
        }
        for (int i = 0; i < size; i++) {
            box2dBody.destroyFixture(box2dBody.getFixtureList());
        }
    }

    /**
     * Destroy all the fixtures on the body, then create new ones based on the colliders attached to the entity.
     * The first thing we do is get a list of all the colliders attached to the entity. Then we destroy all the fixtures on
     * the body. Finally, we loop through the colliders and create new fixtures based on them
     */
    public void resetColliders() {
        if (box2dBody == null)
            Yld.throwException(new IllegalStateException());
        List<Collider> colliders = new ArrayList<>();
        for (Component c : getComponents()) {
            if (c instanceof Collider) {
                colliders.add((Collider) c);
            }
        }
        destroyFixtures();

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
        if(autoUpdateEntity)
            updateEntity();
    }

    /**
     * It updates the entity's position and rotation based on the Box2D body's position and rotation
     */
    public void updateEntity() {
        if (box2dBody != null) {
            box2dBody.m_mass = mass;
            angle = (float) (360f - Math.toDegrees(box2dBody.getAngle()));
            linearVelocity = Yld.toVector2(box2dBody.getLinearVelocity());
            if (!intValuePosition)
                transform.goTo(box2dBody.getPosition().x * scene.getPpm(), box2dBody.getPosition().y * scene.getPpm());
            else transform.goTo((int) (box2dBody.getPosition().x * scene.getPpm()), (int) (box2dBody.getPosition().y * scene.getPpm()));
            if (rotateEntity)
                transform.rotation = (float) -Math.toDegrees(box2dBody.getAngle());
        }
    }

    @Override
    public void onDestroy() {
        if (box2dBody != null) {
            Yld.debug(() -> Yld.log(this + " destroyed."));
            destroyFixtures();
            physicsSystem.getBox2dWorld().destroyBody(box2dBody);
            box2dBody = null;
        }
    }

    /**
     * If the body exists, set the position of the body to the location divided by the pixels per meter ratio. Otherwise,
     * set the position of the self transform to the location
     *
     * @param location The location to go to.
     */
    public void goTo(Vector2 location) {
        if (box2dBody != null)
            box2dBody.setTransform(Yld.toVec2(location.div(scene.getPpm())), box2dBody.getAngle());
        else transform.goTo(location);
    }

    /**
     * Return the position of the body.
     *
     * @return The position of the body in the world.
     */
    public Vector2 position() {
        return Yld.toVector2(box2dBody.getPosition()).mul(scene.getPpm());
    }

    /**
     * If the body exists, translates the body's position by the location, otherwise translates the transform's position by the location.
     *
     * @param location The location to translate.
     */
    public void translate(Vector2 location) {
        if (box2dBody != null)
            box2dBody.setTransform(Yld.toVec2(location.subt(transform.position)), getAngle());
        else transform.translate(location);
    }

    /**
     * Apply a force or impulse to the body.
     * The first parameter is the force or impulse to apply. The second parameter is an enum that tells the function
     * whether to apply a force or an impulse.
     *
     * @param value The value of the impulse to apply.
     * @param impulseType This is an enum that can be either FORCE or LINEAR.
     */
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

    /**
     * Returns true if the body is allowed to sleep
     *
     * @return The boolean value of allowSleep.
     */
    public boolean isAllowSleep() {
        return allowSleep;
    }

    /**
     * Sets whether this body is allowed to sleep
     *
     * @param allowSleep If true, bodies are allowed to sleep. Sleeping bodies have very low CPU cost.
     */
    public void setAllowSleep(boolean allowSleep) {
        this.allowSleep = allowSleep;
    }

    /**
     * Returns the angular velocity of the body
     *
     * @return The angular velocity of the object.
     */
    public float getAngularVelocity() {
        return angularVelocity;
    }

    /**
     * This function returns the mass of the object.
     *
     * @return The mass of the object.
     */
    public float getMass() {
        return mass;
    }

    /**
     * If the Box2D body exists, set its mass to the mass variable.
     *
     * @param mass The mass of the body.
     */
    public void setMass(float mass) {
        this.mass = mass;
        if (box2dBody != null) {
            box2dBody.m_mass = mass;
        }
    }

    /**
     * This function returns the physics system.
     *
     * @return The physics system.
     */
    public PhysicsSystem getPhysicsSystem() {
        return physicsSystem;
    }

    /**
     * This function sets the physics system to the physics system passed in.
     *
     * @param physicsSystem The PhysicsSystem that will be used to update the physics of the entity.
     */
    public void setPhysicsSystem(PhysicsSystem physicsSystem) {
        this.physicsSystem = physicsSystem;
    }

    /**
     * Add the given value to the linear velocity of the body.
     *
     * @param value The value to add to the linear velocity.
     */
    public void addLinearVelocity(Vector2 value) {
        box2dBody.m_linearVelocity.set(box2dBody.m_linearVelocity.add(Yld.toVec2(value)));
    }

    /**
     * If the Box2D body exists, set its angular velocity to the value of the angularVelocity variable
     *
     * @param angularVelocity The angular velocity of the body in radians per second.
     */
    public void setAngularVelocity(float angularVelocity) {
        this.angularVelocity = angularVelocity;
        if (box2dBody != null) {
            box2dBody.setAngularVelocity(angularVelocity);
        }
    }

    /**
     * If the Box2D body exists, set its linear velocity to the value of the linearVelocity field
     *
     * @param linearVelocity The linear velocity of the body.
     */
    public void setLinearVelocity(Vector2 linearVelocity) {
        this.linearVelocity = linearVelocity;
        if (box2dBody != null) {
            box2dBody.setLinearVelocity(Yld.toVec2(linearVelocity));
        }
    }

    /**
     * Applies a linear impulse to the body.
     *
     * @param value The impulse to apply.
     */
    public void applyImpulse(Vector2 value) {
        applyImpulse(value, ImpulseType.LINEAR);
    }

    /**
     * If the Box2D body exists, return the linear velocity of the Box2D body, otherwise return the linear velocity of the
     * Yieldable
     *
     * @return A Vector2 object.
     */
    public Vector2 getLinearVelocity() {
        if (box2dBody != null)
            return Yld.toVector2(box2dBody.getLinearVelocity());
        else
            return linearVelocity;
    }

    /**
     * Returns the angular damping of the body
     *
     * @return The angular damping of the body.
     */
    public float getAngularDamping() {
        return angularDamping;
    }

    /**
     * Sets the angular damping of the body
     *
     * @param angularDamping The angular damping of the body.
     */
    public void setAngularDamping(float angularDamping) {
        this.angularDamping = angularDamping;
    }

    /**
     * Returns the linear damping of the body
     *
     * @return The linearDamping variable is being returned.
     */
    public float getLinearDamping() {
        return linearDamping;
    }

    /**
     * Sets the linear damping of the body
     *
     * @param linearDamping This is the amount of damping applied to the linear velocity of the body.
     */
    public void setLinearDamping(float linearDamping) {
        this.linearDamping = linearDamping;
    }

    /**
     * Returns the physics body type
     *
     * @return The physicsBodyType is being returned.
     */
    public PhysicsBodyType getPhysicsBodyType() {
        return physicsBodyType;
    }

    /**
     * Sets the physics body type of the entity
     *
     * @param physicsBodyType The type of physics body to create.
     */
    public void setPhysicsBodyType(PhysicsBodyType physicsBodyType) {
        this.physicsBodyType = physicsBodyType;
    }

    /**
     * > Returns true if the body is prevented from rotating
     *
     * @return The boolean value of fixedRotation.
     */
    public boolean isFixedRotation() {
        return fixedRotation;
    }

    /**
     * If the body is not null, set the fixed rotation to the value of the fixedRotation variable
     *
     * @param fixedRotation If true, the body will not rotate.
     */
    public void setFixedRotation(boolean fixedRotation) {
        this.fixedRotation = fixedRotation;
        if (box2dBody != null)
            box2dBody.setFixedRotation(fixedRotation);
    }

    /**
     * Returns true if the collision is continuous
     *
     * @return The boolean value of continuousCollision.
     */
    public boolean isContinuousCollision() {
        return continuousCollision;
    }

    /**
     * > Sets whether continuous collision detection is enabled
     *
     * @param continuousCollision If true, the object will be checked for collisions continuously. If false, the object
     * will only be checked for collisions when it moves.
     */
    public void setContinuousCollision(boolean continuousCollision) {
        this.continuousCollision = continuousCollision;
    }

    /**
     * This function returns the Box2D body that is associated with this entity.
     *
     * @return The box2dBody is being returned.
     */
    public Body getBox2dBody() {
        return box2dBody;
    }

    /**
     * This function sets the box2dBody variable to the value of the box2dBody parameter.
     *
     * @param box2dBody This is the Box2D body that represents the object in the world.
     */
    public void setBox2dBody(Body box2dBody) {
        this.box2dBody = box2dBody;
    }

    /**
     * Returns the gravity scale of this body
     *
     * @return The gravity scale.
     */
    public float getGravityScale() {
        return gravityScale;
    }

    /**
     * If the Box2D body exists, set its gravity scale to the value of the gravityScale variable
     *
     * @param gravityScale This is the scale of gravity that will be applied to the body.
     */
    public void setGravityScale(float gravityScale) {
        this.gravityScale = gravityScale;
        if (box2dBody != null)
            box2dBody.setGravityScale(gravityScale);
    }

    /**
     * This function returns the angle of the object.
     *
     * @return The angle of the object.
     */
    public float getAngle() {
        return angle;
    }

    /**
     * If the Box2D body exists, set its angle to the angle of the body.
     *
     * @param angle The angle of the object in degrees.
     */
    public void setAngle(float angle) {
        this.angle = angle;
        if (box2dBody != null)
            box2dBody.setTransform(box2dBody.getPosition(), (float) Math.toRadians(-angle));
    }

    /**
     * Returns whether the entity should be rotated to match the direction of the box2dBody.
     *
     * @return The boolean value of rotateEntity.
     */
    public boolean isRotateEntity() {
        return rotateEntity;
    }

    /**
     *  Sets whether the entity should be rotated to match the direction of the box2dBody.
     *
     * @param rotateEntity If true, entity will be rotated to match the direction of the box2dBody.
     */
    public void setRotateEntity(boolean rotateEntity) {
        this.rotateEntity = rotateEntity;
    }

    /**
     * Returns if the position of the entity will be set in integer or in float.
     *
     * @return The value of the boolean variable intValuePosition.
     */
    public boolean isIntValuePosition() {
        return intValuePosition;
    }

    /**
     * Sets if the position of the entity will be set in integer or in float.
     *
     * @param intValuePosition If the position of the entity will be set in integer or in float.
     */
    public void setIntValuePosition(boolean intValuePosition) {
        this.intValuePosition = intValuePosition;
    }

    /**
     * Returns whether the entity is automatically updated
     *
     * @return The value of the autoUpdateEntity variable.
     */
    public boolean isAutoUpdateEntity() {
        return autoUpdateEntity;
    }

    /**
     * Sets whether the entity should be automatically updated.
     *
     * @param autoUpdateEntity If true, the entity will be updated automatically.
     */
    public void setAutoUpdateEntity(boolean autoUpdateEntity) {
        this.autoUpdateEntity = autoUpdateEntity;
    }
}
