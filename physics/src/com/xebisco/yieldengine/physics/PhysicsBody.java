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

package com.xebisco.yieldengine.physics;

import com.xebisco.yieldengine.ComponentBehavior;
import com.xebisco.yieldengine.ContextTime;
import com.xebisco.yieldengine.ImmutableVector2D;
import com.xebisco.yieldengine.Vector2D;
import com.xebisco.yieldengine.physics.colliders.Collider2D;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;

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
        bodyDef.position = Utils.toVec2(transform().position().divide(physicsSystem.ppm()));
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

    public void applyLinearImpulse(Vector2D impulse) {
        b2Body.applyLinearImpulse(Utils.toVec2(impulse), b2Body.getWorldCenter());
    }

    public void applyTorque(double torque) {
        b2Body.applyTorque((float) torque);
    }

    public ImmutableVector2D linearVelocity() {
        return Utils.toImmutableVector2D(b2Body.getLinearVelocity().mul((float) physicsSystem.ppm()));
    }

    public PhysicsBody setLinearVelocity(Vector2D linearVelocity) {
        b2Body.setLinearVelocity(Utils.toVec2(linearVelocity.divide(physicsSystem.ppm())));
        return this;
    }

    public void applyAngularImpulse(double impulse) {
        b2Body.applyAngularImpulse((float) impulse);
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
