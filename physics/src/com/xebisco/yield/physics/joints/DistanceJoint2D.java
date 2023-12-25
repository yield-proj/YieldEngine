package com.xebisco.yield.physics.joints;

import com.xebisco.yield.ComponentBehavior;
import com.xebisco.yield.OnlyModifiableBeforeCreationException;
import com.xebisco.yield.physics.PhysicsBody;
import com.xebisco.yield.physics.PhysicsSystem;
import org.jbox2d.dynamics.joints.DistanceJoint;
import org.jbox2d.dynamics.joints.DistanceJointDef;

public class DistanceJoint2D extends ComponentBehavior {
    private PhysicsBody body1, body2;
    private boolean collideConnected = true;
    private DistanceJoint b2DistanceJoint;
    private double frequency = 4, dampingRatio = .5;
    private PhysicsSystem physicsSystem;

    @Override
    public void onCreate() {
        physicsSystem = application().scene().system(PhysicsSystem.class);
    }

    @Override
    public void onStart() {
        DistanceJointDef jointDef = new DistanceJointDef();
        jointDef.initialize(body1.b2Body(), body2.b2Body(), body1.b2Body().getWorldCenter(), body2.b2Body().getWorldCenter());
        jointDef.collideConnected = collideConnected;
        jointDef.dampingRatio = (float) dampingRatio;
        jointDef.frequencyHz = (float) frequency;
        jointDef.userData = this;
        b2DistanceJoint = (DistanceJoint) physicsSystem.b2World().createJoint(jointDef);
    }

    public PhysicsBody body1() {
        return body1;
    }

    public DistanceJoint2D setBody1(PhysicsBody body1) throws OnlyModifiableBeforeCreationException {
        if (b2DistanceJoint != null) throw new OnlyModifiableBeforeCreationException();
        this.body1 = body1;
        return this;
    }

    public PhysicsBody body2() {
        return body2;
    }

    public DistanceJoint2D setBody2(PhysicsBody body2) throws OnlyModifiableBeforeCreationException {
        if (b2DistanceJoint != null) throw new OnlyModifiableBeforeCreationException();
        this.body2 = body2;
        return this;
    }

    public boolean collideConnected() {
        return collideConnected;
    }

    public DistanceJoint2D setCollideConnected(boolean collideConnected) throws OnlyModifiableBeforeCreationException {
        if (b2DistanceJoint != null) throw new OnlyModifiableBeforeCreationException();
        this.collideConnected = collideConnected;
        return this;
    }

    public DistanceJoint b2DistanceJoint() {
        return b2DistanceJoint;
    }

    public DistanceJoint2D setB2DistanceJoint(DistanceJoint b2DistanceJoint) {
        this.b2DistanceJoint = b2DistanceJoint;
        return this;
    }

    public double frequency() {
        return frequency;
    }

    public DistanceJoint2D setFrequency(double frequency) {
        if(b2DistanceJoint != null) b2DistanceJoint.setFrequency((float) frequency);
        this.frequency = frequency;
        return this;
    }

    public double dampingRatio() {
        return dampingRatio;
    }

    public DistanceJoint2D setDampingRatio(double dampingRatio) {
        if(b2DistanceJoint != null) b2DistanceJoint.setDampingRatio((float) frequency);
        this.dampingRatio = dampingRatio;
        return this;
    }
}
