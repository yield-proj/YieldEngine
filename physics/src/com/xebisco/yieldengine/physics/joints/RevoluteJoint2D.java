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

package com.xebisco.yieldengine.physics.joints;

import org.jbox2d.dynamics.joints.JointDef;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;

public class RevoluteJoint2D extends Joint2D {

    private double lowerAngle = -90, upperAngle = 45, maxMotorTorque = 10, motorSpeed = 0;
    private boolean enableLimit = true, enableMotor = true;

    @Override
    public JointDef createJoint() {
        RevoluteJointDef jointDef = new RevoluteJointDef();
        jointDef.initialize(body1().b2Body(), body2().b2Body(), body1().b2Body().getWorldCenter());
        jointDef.lowerAngle = (float) Math.toRadians(lowerAngle);
        jointDef.upperAngle = (float) Math.toRadians(upperAngle);
        jointDef.maxMotorTorque = (float) maxMotorTorque;
        jointDef.motorSpeed = (float) motorSpeed;
        jointDef.enableLimit = enableLimit;
        jointDef.enableMotor = enableMotor;

        return jointDef;
    }
    public double jointAngle() {
        return Math.toDegrees(((RevoluteJoint) b2Joint()).getJointAngle());
    }

    public double jointSpeed() {
        return ((RevoluteJoint) b2Joint()).getJointSpeed();
    }

    public double lowerAngle() {
        return lowerAngle;
    }

    public RevoluteJoint2D setLowerAngle(double lowerAngle) {
        if(b2Joint() != null) ((RevoluteJoint) b2Joint()).setLimits((float) Math.toRadians(lowerAngle), ((RevoluteJoint) b2Joint()).getUpperLimit());
        this.lowerAngle = lowerAngle;
        return this;
    }

    public double upperAngle() {
        return upperAngle;
    }

    public RevoluteJoint2D setUpperAngle(double upperAngle) {
        if(b2Joint() != null) ((RevoluteJoint) b2Joint()).setLimits(((RevoluteJoint) b2Joint()).getLowerLimit(), (float) Math.toRadians(upperAngle));
        this.upperAngle = upperAngle;
        return this;
    }

    public double maxMotorTorque() {
        return maxMotorTorque;
    }

    public RevoluteJoint2D setMaxMotorTorque(double maxMotorTorque) {
        if(b2Joint() != null) ((RevoluteJoint) b2Joint()).setMaxMotorTorque((float) maxMotorTorque);
        this.maxMotorTorque = maxMotorTorque;
        return this;
    }

    public double motorSpeed() {
        return motorSpeed;
    }

    public RevoluteJoint2D setMotorSpeed(double motorSpeed) {
        if(b2Joint() != null) ((RevoluteJoint) b2Joint()).setMotorSpeed((float) motorSpeed);
        this.motorSpeed = motorSpeed;
        return this;
    }

    public boolean enableLimit() {
        return enableLimit;
    }

    public RevoluteJoint2D setEnableLimit(boolean enableLimit) {
        if(b2Joint() != null) ((RevoluteJoint) b2Joint()).enableLimit(enableLimit);
        this.enableLimit = enableLimit;
        return this;
    }

    public boolean enableMotor() {
        return enableMotor;
    }

    public RevoluteJoint2D setEnableMotor(boolean enableMotor) {
        if(b2Joint() != null) ((RevoluteJoint) b2Joint()).enableMotor(enableMotor);
        this.enableMotor = enableMotor;
        return this;
    }
}
