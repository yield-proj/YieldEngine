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
import com.xebisco.yield.VisibleOnInspector;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;

public class PhysicsBody extends ComponentBehavior {

    private Body b2Body;
    @VisibleOnInspector
    private PhysicsType type = PhysicsType.DYNAMIC;
    @VisibleOnInspector
    private double gravityScale = 1, mass = 1;
    @VisibleOnInspector
    private boolean bullet, fixedRotation;

    @Override
    public void onStart() {
        BodyDef def = new BodyDef();
        def.position = Global.toVec2(getTransform().getPosition().divide(new TwoAnchorRepresentation(getApplication().getPhysicsPpm(), getApplication().getPhysicsPpm())));
        def.userData = getEntity();
        setB2Body(getApplication().getScene().getPhysicsSystem().getB2World().createBody(def));
    }

    @Override
    public void onUpdate() {
        for (Fixture f = getB2Body().getFixtureList(); f != null; f = f.getNext()) {
            f.m_shape = ((Collider) f.getUserData()).getShape();
            f.setDensity((float) ((Collider) f.getUserData()).getDensity());
            f.setFriction((float) ((Collider) f.getUserData()).getFriction());
            f.setSensor(((Collider) f.getUserData()).isSensor());
            if (!getEntity().getComponents().contains((Collider) f.getUserData())) {
                getB2Body().destroyFixture(f);
            }
        }
        for (int i = 0; i < getEntity().getComponents().size(); i++) {
            ComponentBehavior c = getEntity().getComponents().get(i);
            if (c instanceof Collider) {
                boolean contains = false;
                for (Fixture f = getB2Body().getFixtureList(); f != null; f = f.getNext()) {
                    if (f.getUserData() == c) {
                        contains = true;
                        break;
                    }
                }
                if (!contains) {
                    Fixture f = getB2Body().createFixture(((Collider) c).getShape(), (float) ((Collider) c).getDensity());
                    f.m_filter.categoryBits = ((Collider) c).getCollisionMask().hashCode();
                    f.setSensor(((Collider) c).isSensor());
                    for (String filter : ((Collider) c).getCollisionFilter()) {
                        f.m_filter.maskBits &= ~(filter.hashCode());
                    }
                    f.setUserData(c);
                }
            }
        }
        switch (type) {
            case DYNAMIC:
                getB2Body().setType(BodyType.DYNAMIC);
                break;
            case STATIC:
                getB2Body().setType(BodyType.STATIC);
                break;
            case KINEMATIC:
                getB2Body().setType(BodyType.KINEMATIC);
                break;
        }
        getB2Body().setGravityScale((float) gravityScale);
        getB2Body().setBullet(bullet);
        getB2Body().m_mass = (float) mass;
        getB2Body().setFixedRotation(fixedRotation);
        getTransform().getPosition().set(getB2Body().getPosition().x * getApplication().getPhysicsPpm(), getB2Body().getPosition().y * getApplication().getPhysicsPpm());
        getTransform().setzRotation(Math.toDegrees(getB2Body().getAngle()));
    }

    @Override
    public void dispose() {
        getApplication().getScene().getPhysicsSystem().getB2World().destroyBody(b2Body);
    }

    public void addForce(Vector2D force, ForceType forceType) {
        switch (forceType) {
            case FORCE:
                getB2Body().applyForceToCenter(Global.toVec2(force));
                break;
            case LINEAR_IMPULSE:
                getB2Body().applyLinearImpulse(Global.toVec2(force), getB2Body().getWorldCenter());
                break;
            default:
                throw new IllegalArgumentException(forceType.name());
        }
    }

    public void addForce(double force, ForceType forceType) {
        checkBodyCreation();
        switch (forceType) {
            case TORQUE:
                getB2Body().applyTorque((float) force);
                break;
            case ANGULAR_IMPULSE:
                getB2Body().applyAngularImpulse((float) force);
                break;
            default:
                throw new IllegalArgumentException(forceType.name());
        }
    }

    public void translate(TwoAnchorRepresentation a) {
        setPosition(new TwoAnchorRepresentation(getPosition().x + a.getX() / getApplication().getPhysicsPpm(), getPosition().y + a.getY() / getApplication().getPhysicsPpm()));
    }

    public void checkBodyCreation() {
        if (b2Body == null) throw new BodyNotCreatedException();
    }

    public double getAngle() {
        return Math.toDegrees(getB2Body().getAngle());
    }

    public void setAngle(double angle) {
        getB2Body().setTransform(getB2Body().getPosition(), (float) Math.toRadians(angle));
    }

    public Vec2 getPosition() {
        return getB2Body().getPosition();
    }

    public void setPosition(TwoAnchorRepresentation value) {
        getB2Body().setTransform(new Vec2((float) (value.getX() / getApplication().getPhysicsPpm()), (float) (value.getY() / getApplication().getPhysicsPpm())), b2Body.getAngle());
    }

    public Vec2 getLinearVelocity() {
        return getB2Body().getLinearVelocity();
    }

    public void setLinearVelocity(Vec2 linearVelocity) {
        getB2Body().setLinearVelocity(linearVelocity);
    }

    public void setLinearVelocity(Vector2D linearVelocity) {
        setLinearVelocity(Global.toVec2(linearVelocity));
    }


    public double getAngularVelocity() {
        return getB2Body().getAngularVelocity();
    }

    public void setAngularVelocity(double angularVelocity) {
        getB2Body().setAngularVelocity((float) angularVelocity);
    }

    public PhysicsType getType() {
        return type;
    }

    public void setType(PhysicsType type) {
        this.type = type;
    }

    public double getGravityScale() {
        return gravityScale;
    }

    public void setGravityScale(double gravityScale) {
        this.gravityScale = gravityScale;
    }

    public Body getB2Body() {
        checkBodyCreation();
        return b2Body;
    }

    public void setB2Body(Body b2Body) {
        this.b2Body = b2Body;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public boolean isBullet() {
        return bullet;
    }

    public void setBullet(boolean bullet) {
        this.bullet = bullet;
    }

    public boolean isFixedRotation() {
        return fixedRotation;
    }

    public void setFixedRotation(boolean fixedRotation) {
        this.fixedRotation = fixedRotation;
    }
}
