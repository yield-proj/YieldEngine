/*
 * Copyright [2022] [Xebisco]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xebisco.yield.components;

import com.xebisco.yield.Component;
import com.xebisco.yield.Yld;
import com.xebisco.yield.colliders.Collider;
import com.xebisco.yield.systems.PhysicsSystem;
import com.xebisco.yield.utils.Conversions;
import com.xebisco.yield.utils.Vector2;
import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.MassType;

public class PhysicsBody extends Component
{
    private PhysicsSystem physicsSystem;
    private Body body;
    private float gravityMultiplier;

    public enum ForceType
    {
        FORCE, IMPULSE
    }

    @Override
    public void create()
    {
        physicsSystem = scene.getSystem(PhysicsSystem.class);
        body = new Body();
        body.setMass(MassType.FIXED_LINEAR_VELOCITY);
        body.setLinearDamping(.97);
    }

    @Override
    public void update(float delta)
    {
        transform.position.x = (float) body.getTransform().getTranslation().x;
        transform.position.y = (float) -body.getTransform().getTranslation().y;
    }

    @Override
    public void start()
    {
        body.translate(Conversions.toDyn4jVector2(transform.position));
        physicsSystem.getWorld().addBody(body);
        Yld.log(body.isStatic());
    }

    public PhysicsSystem getPhysicsSystem()
    {
        return physicsSystem;
    }

    public void setPhysicsSystem(PhysicsSystem physicsSystem)
    {
        this.physicsSystem = physicsSystem;
    }

    public Body getBody()
    {
        return body;
    }

    public void setBody(Body body)
    {
        this.body = body;
    }

    public void setCollider(Collider collider)
    {
        body.removeAllFixtures();
        collider.processFixture(body.addFixture(collider.convex()));
    }

    public void addCollider(Collider collider)
    {
        collider.processFixture(body.addFixture(collider.convex()));
    }

    public float getGravityMultiplier()
    {
        return gravityMultiplier;
    }

    public void setGravityMultiplier(float gravityMultiplier)
    {
        this.gravityMultiplier = gravityMultiplier;
        body.setGravityScale(gravityMultiplier);
    }

    public void addForce(Vector2 force, ForceType forceType)
    {
        if (forceType == ForceType.FORCE)
            body.applyForce(Conversions.toDyn4jVector2(force));
        else if (forceType == ForceType.IMPULSE)
            body.applyImpulse(Conversions.toDyn4jVector2(force));
    }

    public void addForce(Vector2 force)
    {
        addForce(force, ForceType.FORCE);
    }

    public void setVelocity(Vector2 velocity)
    {
        body.setLinearVelocity(Conversions.toDyn4jVector2(velocity));
    }
}
