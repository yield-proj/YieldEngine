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
import com.xebisco.yield.Collider;
import com.xebisco.yield.YldWorld;
import com.xebisco.yield.systems.PhysicsSystem;
import com.xebisco.yield.utils.Conversions;
import org.jbox2d.collision.AABB;
import org.jbox2d.collision.RayCastInput;
import org.jbox2d.collision.RayCastOutput;
import org.jbox2d.collision.shapes.MassData;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

public class PhysicsBody extends Component
{
    private Body box2dBody;
    private BodyType type = BodyType.STATIC;

    public void setShapeAs(Collider collider)
    {
        box2dBody.destroyFixture(box2dBody.m_fixtureList);
        PolygonShape polygonShape = new PolygonShape();
        if (collider.getShape() == Collider.Shape.RECTANGLE)
        {
            polygonShape.setAsBox(collider.getSize().x, collider.getSize().y);
        }
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;
        fixtureDef.density = collider.getDensity();
        fixtureDef.friction = collider.getFriction();
        box2dBody.createFixture(fixtureDef);
    }

    @Override
    public void start()
    {
        YldWorld world = scene.getSystem(PhysicsSystem.class).getWorld();
        BodyDef def = new BodyDef();
        def.position.set(Conversions.toBox2dVec2(transform.position));
        def.type = BodyType.STATIC;
        box2dBody = world.getBox2dWorld().createBody(def)
        ;
    }

    public Body getBox2dBody()
    {
        return box2dBody;
    }

    public void setBox2dBody(Body box2dBody)
    {
        this.box2dBody = box2dBody;
    }

    public BodyType getType()
    {
        return type;
    }

    public void setType(BodyType type)
    {
        this.type = type;
    }
}
