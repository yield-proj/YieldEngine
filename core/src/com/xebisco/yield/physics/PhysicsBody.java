/*
 * Copyright [2022-2023] [Xebisco]
 *
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

package com.xebisco.yield.physics;

import com.xebisco.yield.*;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;

@ComponentIcon(iconType = ComponentIconType.PHYSICS)
public class PhysicsBody extends ComponentBehavior {

    private Body b2Body;
    @VisibleOnEditor
    private PhysicsType type = PhysicsType.DYNAMIC;
    @VisibleOnEditor
    private double gravityScale = 1, mass = 1;
    @VisibleOnEditor
    private boolean bullet, fixedRotation;

    @Override
    public void onStart() {
        BodyDef def = new BodyDef();
        def.position = Global.toVec2(transform().position().divide(new Vector2D(application().physicsPpm(), application().physicsPpm())));
        def.angle = (float) Math.toRadians(transform().zRotation());
        def.userData = entity();
        setB2Body(application().scene().physicsMain().b2World().createBody(def));
    }

    @Override
    public void onPhysicsUpdate() {
        for (int i = 0; i < entity().components().size(); i++) {
            ComponentBehavior c = entity().components().get(i);
            if (c instanceof Collider) {
                boolean contains = false;
                for (Fixture f = b2Body().getFixtureList(); f != null; f = f.getNext()) {
                    if (f.getUserData() == c) {
                        contains = true;
                        break;
                    }
                }
                if (!contains) {
                    Fixture f = b2Body().createFixture(((Collider) c).shape(), (float) ((Collider) c).density());
                    f.setUserData(c);
                }
            }
        }
        for (Fixture f = b2Body().getFixtureList(); f != null; f = f.getNext()) {
            if (!entity().components().contains((Collider) f.getUserData())) {
                b2Body().destroyFixture(f);
            } else {
                Collider c = ((Collider) f.getUserData());
                f.m_shape = c.shape();
                f.setDensity((float) ((Collider) f.getUserData()).density());
                f.setFriction((float) ((Collider) f.getUserData()).friction());
                f.setSensor(((Collider) f.getUserData()).sensor());
                f.m_filter.categoryBits = c.collisionMask().hashCode();
                f.setSensor(c.sensor());
                for (String filter : c.collisionFilter()) {
                    f.m_filter.maskBits &= ~(filter.hashCode());
                }
            }
        }
        switch (type) {
            case DYNAMIC -> b2Body().setType(BodyType.DYNAMIC);
            case STATIC -> b2Body().setType(BodyType.STATIC);
            case KINEMATIC -> b2Body().setType(BodyType.KINEMATIC);
        }
        b2Body().setGravityScale((float) gravityScale);
        b2Body().setBullet(bullet);
        b2Body().m_mass = (float) mass;
        b2Body().setFixedRotation(fixedRotation);
        transform().position().set(b2Body().getPosition().x * application().physicsPpm(), b2Body().getPosition().y * application().physicsPpm());
        transform().setzRotation(Math.toDegrees(b2Body().getAngle()));
    }

    @Override
    public void dispose() {
        if (b2Body != null)
            application().scene().physicsMain().b2World().destroyBody(b2Body);
    }

    public void addForce(Vector2D force, ForceType forceType) {
        switch (forceType) {
            case FORCE -> b2Body().applyForceToCenter(Global.toVec2(force));
            case LINEAR_IMPULSE -> b2Body().applyLinearImpulse(Global.toVec2(force), b2Body().getWorldCenter());
            default -> throw new IllegalArgumentException(forceType.name());
        }
    }

    public void addForce(double force, ForceType forceType) {
        checkBodyCreation();
        switch (forceType) {
            case TORQUE -> b2Body().applyTorque((float) force);
            case ANGULAR_IMPULSE -> b2Body().applyAngularImpulse((float) force);
            default -> throw new IllegalArgumentException(forceType.name());
        }
    }

    public void translate(Vector2D a) {
        setPosition(new Vector2D(position().x + a.x() / application().physicsPpm(), position().y + a.y() / application().physicsPpm()));
    }

    public void checkBodyCreation() {
        if (b2Body == null) throw new BodyNotCreatedException();
    }

    public double angle() {
        return Math.toDegrees(b2Body().getAngle());
    }

    public PhysicsBody setAngle(double angle) {
        b2Body().setTransform(b2Body().getPosition(), (float) Math.toRadians(angle));
        return this;
    }

    public Vec2 position() {
        return b2Body().getPosition();
    }

    public PhysicsBody setPosition(Vector2D value) {
        b2Body().setTransform(new Vec2((float) (value.x() / application().physicsPpm()), (float) (value.y() / application().physicsPpm())), b2Body.getAngle());
        return this;
    }

    public Vec2 linearVelocity() {
        return b2Body().getLinearVelocity();
    }

    public PhysicsBody setLinearVelocity(Vec2 linearVelocity) {
        b2Body().setLinearVelocity(linearVelocity);
        return this;
    }

    public PhysicsBody setLinearVelocity(Vector2D linearVelocity) {
        setLinearVelocity(Global.toVec2(linearVelocity));
        return this;
    }


    public double angularVelocity() {
        return b2Body().getAngularVelocity();
    }

    public PhysicsBody setAngularVelocity(double angularVelocity) {
        b2Body().setAngularVelocity((float) angularVelocity);
        return this;
    }

    public PhysicsType type() {
        return type;
    }

    public PhysicsBody setType(PhysicsType type) {
        this.type = type;
        return this;
    }

    public double gravityScale() {
        return gravityScale;
    }

    public PhysicsBody setGravityScale(double gravityScale) {
        this.gravityScale = gravityScale;
        return this;
    }

    public Body b2Body() {
        checkBodyCreation();
        return b2Body;
    }

    public PhysicsBody setB2Body(Body b2Body) {
        this.b2Body = b2Body;
        return this;
    }

    public double mass() {
        return mass;
    }

    public PhysicsBody setMass(double mass) {
        this.mass = mass;
        return this;
    }

    public boolean bullet() {
        return bullet;
    }

    public PhysicsBody setBullet(boolean bullet) {
        this.bullet = bullet;
        return this;
    }

    public boolean fixedRotation() {
        return fixedRotation;
    }

    public PhysicsBody setFixedRotation(boolean fixedRotation) {
        this.fixedRotation = fixedRotation;
        return this;
    }
}
