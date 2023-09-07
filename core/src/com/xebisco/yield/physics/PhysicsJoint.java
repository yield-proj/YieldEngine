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
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.dynamics.joints.JointDef;
import org.jbox2d.dynamics.joints.JointType;

@ComponentIcon(iconType = ComponentIconType.PHYSICS)
public class PhysicsJoint extends ComponentBehavior {

    @VisibleOnEditor
    private Entity2D entity1, entity2;

    private Joint b2Joint;

    @VisibleOnEditor
    private PhysicsJointType type = PhysicsJointType.DISTANCE;

    @Override
    public void onPhysicsUpdate() {
        if(b2Joint == null)
            createJoint();
    }

    private void createJoint() {
        JointDef jointDef = new JointDef();
        jointDef.userData = this;
        jointDef.bodyA = entity1.component(PhysicsBody.class).b2Body();
        jointDef.bodyB = entity2.component(PhysicsBody.class).b2Body();
        jointDef.type = JointType.valueOf(type.name());
        b2Joint = application().scene().physicsMain().getB2World().createJoint(jointDef);
    }

    public Entity2D getEntity1() {
        return entity1;
    }

    public void setEntity1(Entity2D entity1) {
        this.entity1 = entity1;
    }

    public Entity2D getEntity2() {
        return entity2;
    }

    public void setEntity2(Entity2D entity2) {
        this.entity2 = entity2;
    }

    public Joint getB2Joint() {
        return b2Joint;
    }

    public void setB2Joint(Joint b2Joint) {
        this.b2Joint = b2Joint;
    }

    public PhysicsJointType getType() {
        return type;
    }

    public void setType(PhysicsJointType type) {
        this.type = type;
    }
}
