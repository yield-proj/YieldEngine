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

package com.xebisco.yield.physics.joints;

import com.xebisco.yield.ComponentBehavior;
import com.xebisco.yield.OnlyModifiableBeforeCreationException;
import com.xebisco.yield.physics.PhysicsBody;
import com.xebisco.yield.physics.PhysicsSystem;
import org.jbox2d.dynamics.joints.DistanceJoint;
import org.jbox2d.dynamics.joints.DistanceJointDef;
import org.jbox2d.dynamics.joints.JointDef;

public class DistanceJoint2D extends Joint2D {
    private double frequency = 4, dampingRatio = .5;

    @Override
    public JointDef createJoint() {
        DistanceJointDef jointDef = new DistanceJointDef();
        jointDef.initialize(body1().b2Body(), body2().b2Body(), body1().b2Body().getWorldCenter(), body2().b2Body().getWorldCenter());
        jointDef.dampingRatio = (float) dampingRatio;
        jointDef.frequencyHz = (float) frequency;
        return jointDef;
    }

    public double frequency() {
        return frequency;
    }

    public DistanceJoint2D setFrequency(double frequency) {
        if(b2Joint() != null) ((DistanceJoint) b2Joint()).setFrequency((float) frequency);
        this.frequency = frequency;
        return this;
    }

    public double dampingRatio() {
        return dampingRatio;
    }

    public DistanceJoint2D setDampingRatio(double dampingRatio) {
        if(b2Joint() != null) ((DistanceJoint) b2Joint()).setDampingRatio((float) frequency);
        this.dampingRatio = dampingRatio;
        return this;
    }
}
