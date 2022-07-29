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

package com.xebisco.yield;

import org.jbox2d.callbacks.RayCastCallback;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;

public class RayCast implements RayCastCallback {
    private Fixture fixture;
    private Vector2 hitPoint = new Vector2(), normal = new Vector2();
    private float fraction;
    private boolean hit;
    private Entity requestEntity, hitEntity;

    @Override
    public float reportFixture(Fixture fixture, Vec2 point, Vec2 normal, float fraction) {
        if(fixture.m_userData == requestEntity) {
            return 1;
        }
        this.fixture = fixture;
        this.hitPoint = Yld.toVector2(point);
        this.normal = Yld.toVector2(normal);
        this.hit = fraction != 0;
        this.hitEntity = (Entity) fixture.m_userData;
        return fraction;
    }

    public Fixture getFixture() {
        return fixture;
    }

    public void setFixture(Fixture fixture) {
        this.fixture = fixture;
    }

    public Vector2 getHitPoint() {
        return hitPoint;
    }

    public void setHitPoint(Vector2 hitPoint) {
        this.hitPoint = hitPoint;
    }

    public Vector2 getNormal() {
        return normal;
    }

    public void setNormal(Vector2 normal) {
        this.normal = normal;
    }

    public float getFraction() {
        return fraction;
    }

    public void setFraction(float fraction) {
        this.fraction = fraction;
    }

    public boolean isHit() {
        return hit;
    }

    public void setHit(boolean hit) {
        this.hit = hit;
    }

    public Entity getRequestEntity() {
        return requestEntity;
    }

    public void setRequestEntity(Entity requestEntity) {
        this.requestEntity = requestEntity;
    }

    public Entity getHitEntity() {
        return hitEntity;
    }

    public void setHitEntity(Entity hitEntity) {
        this.hitEntity = hitEntity;
    }
}
