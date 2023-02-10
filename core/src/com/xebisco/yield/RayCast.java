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

/**
 * It's a callback that is used to get information about a ray-cast
 */
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

    /**
     * This function returns the fixture.
     *
     * @return The fixture object.
     */
    public Fixture getFixture() {
        return fixture;
    }

    /**
     * This function sets the fixture variable to the fixture parameter.
     *
     * @param fixture The fixture value to be set.
     */
    public void setFixture(Fixture fixture) {
        this.fixture = fixture;
    }

    /**
     * This function returns the hitPoint variable.
     *
     * @return The hitPoint variable is being returned.
     */
    public Vector2 getHitPoint() {
        return hitPoint;
    }

    /**
     * This function sets the hitPoint variable to the value of the hitPoint parameter.
     *
     * @param hitPoint The hitPoint.
     */
    public void setHitPoint(Vector2 hitPoint) {
        this.hitPoint = hitPoint;
    }

    /**
     * Returns the normal of the ray-cast.
     *
     * @return The normal of the ray-cast.
     */
    public Vector2 getNormal() {
        return normal;
    }

    /**
     * Sets the normal of the contact.
     *
     * @param normal The normal of the ray-cast.
     */
    public void setNormal(Vector2 normal) {
        this.normal = normal;
    }

    /**
     * Returns the fraction of the ray-cast.
     *
     * @return The fraction of the ray-cast.
     */
    public float getFraction() {
        return fraction;
    }

    /**
     * This function sets the fraction to the value of the parameter fraction.
     *
     * @param fraction The fraction of the animation that has been run.
     */
    public void setFraction(float fraction) {
        this.fraction = fraction;
    }

    /**
     * Returns true if the ray-cast hit something, false otherwise.
     *
     * @return The boolean value of hit.
     */
    public boolean isHit() {
        return hit;
    }

    /**
     * This function sets the hit variable to the value of the hit parameter.
     *
     * @param hit This hit value to set.
     */
    public void setHit(boolean hit) {
        this.hit = hit;
    }

    /**
     * This function returns the ray-cast request entity.
     *
     * @return The requestEntity
     */
    public Entity getRequestEntity() {
        return requestEntity;
    }

    /**
     * This function sets the request entity.
     *
     * @param requestEntity The entity that requested this ray-cast.
     */
    public void setRequestEntity(Entity requestEntity) {
        this.requestEntity = requestEntity;
    }

    /**
     * Returns the entity that was hit by the ray-cast.
     *
     * @return The hitEntity variable is being returned.
     */
    public Entity getHitEntity() {
        return hitEntity;
    }

    /**
     * This function sets the hitEntity variable to the value of the hitEntity parameter.
     *
     * @param hitEntity The entity that was hit by the ray-cast.
     */
    public void setHitEntity(Entity hitEntity) {
        this.hitEntity = hitEntity;
    }
}