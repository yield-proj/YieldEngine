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
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.dynamics.joints.JointDef;

public abstract class Joint2D extends ComponentBehavior {
    private PhysicsBody body1, body2;
    private PhysicsSystem physicsSystem;
    private boolean collideConnected;

    private Joint b2Joint;

    @Override
    public void onCreate() {
        physicsSystem = application().scene().system(PhysicsSystem.class);
    }

    public abstract JointDef createJoint();

    @Override
    public void onStart() {
        JointDef jointDef = createJoint();
        jointDef.collideConnected = collideConnected;
        jointDef.userData = this;
        b2Joint = physicsSystem.b2World().createJoint(jointDef);
    }

    public PhysicsBody body1() {
        return body1;
    }

    public Joint2D setBody1(PhysicsBody body1) throws OnlyModifiableBeforeCreationException {
        if (b2Joint != null) throw new OnlyModifiableBeforeCreationException();
        this.body1 = body1;
        return this;
    }

    public PhysicsBody body2() {
        return body2;
    }

    public Joint2D setBody2(PhysicsBody body2) throws OnlyModifiableBeforeCreationException {
        if (b2Joint != null) throw new OnlyModifiableBeforeCreationException();
        this.body2 = body2;
        return this;
    }

    public Joint b2Joint() {
        return b2Joint;
    }

    public Joint2D setB2Joint(Joint b2Joint) {
        this.b2Joint = b2Joint;
        return this;
    }

    public boolean collideConnected() {
        return collideConnected;
    }

    public Joint2D setCollideConnected(boolean collideConnected) throws OnlyModifiableBeforeCreationException {
        if (b2Joint() != null) throw new OnlyModifiableBeforeCreationException();
        this.collideConnected = collideConnected;
        return this;
    }
}
