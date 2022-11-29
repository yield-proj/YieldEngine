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

import com.xebisco.yield.render.Renderable;
import com.xebisco.yield.render.RenderableType;

import java.util.HashSet;
import java.util.Set;

public class ParticleSystem extends Component {
    private Prefab particlePrefab = null;
    private float amount = 10, speed = 10, lifeTime = 3, toCrateTimer;
    private int angle = 90, angleRange = 10, randomSpeedValue = 0;
    private Vector2 gravity = new Vector2(0, 10), size = new Vector2(100, 100), emissionOffset = new Vector2();
    private Texture texture;
    private final static Texture defaultParticleTexture = new Texture("com/xebisco/yield/assets/default-particle.png");
    private final Set<YldPair<PosRef, Float>> particles = new HashSet<>();

    @Override
    public void render(Set<Renderable> renderables) {
        if (texture == null)
            texture = defaultParticleTexture.get();
        toCrateTimer += getTime().getDelta();
        if (toCrateTimer > 1f / amount) {
            toCrateTimer = 0;
            int rsv = randomSpeedValue, ar = angleRange;
            if(rsv != 0) rsv = Yld.RAND.nextInt(rsv);
            if(ar != 0) ar = Yld.RAND.nextInt(ar);
            particles.add(new YldPair<>(new PosRef(emissionOffset, new Vector(speed + rsv, ar + angle - angleRange / 2f)), lifeTime));
        }
        for (YldPair<PosRef, Float> particle : particles) {
            particle.setSecond(particle.getSecond() - getTime().getDelta());
            particle.getFirst().setSpeed(particle.getFirst().getSpeed().sum(gravity.mul(1000f / getTime().getTargetFPS() / 1000f)));
            particle.getFirst().setPosition(particle.getFirst().getPosition().sum(particle.getFirst().getSpeed()));
            Renderable renderable = new Renderable();
            renderable.setType(RenderableType.IMAGE);
            renderable.setSpecific(texture.getSpecificImage());
            renderable.setzIndex(getEntity().getIndex());
            renderable.setX((int) (particle.getFirst().getPosition().x + transform.position.x));
            renderable.setY((int) (particle.getFirst().getPosition().y + transform.position.y));
            renderable.setWidth((int) size.x);
            renderable.setHeight((int) size.y);
            renderables.add(renderable);
        }
        particles.removeIf(p -> p.getSecond() <= 0);
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public Prefab getParticlePrefab() {
        return particlePrefab;
    }

    public void setParticlePrefab(Prefab particlePrefab) {
        this.particlePrefab = particlePrefab;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public int getRandomSpeedValue() {
        return randomSpeedValue;
    }

    public void setRandomSpeedValue(int randomSpeedValue) {
        this.randomSpeedValue = randomSpeedValue;
    }

    public float getLifeTime() {
        return lifeTime;
    }

    public void setLifeTime(float lifeTime) {
        this.lifeTime = lifeTime;
    }

    public int getAngle() {
        return angle;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }

    public int getAngleRange() {
        return angleRange;
    }

    public void setAngleRange(int angleRange) {
        this.angleRange = angleRange;
    }

    public Vector2 getGravity() {
        return gravity;
    }

    public void setGravity(Vector2 gravity) {
        this.gravity = gravity;
    }

    public float getToCrateTimer() {
        return toCrateTimer;
    }

    public void setToCrateTimer(float toCrateTimer) {
        this.toCrateTimer = toCrateTimer;
    }

    public Vector2 getSize() {
        return size;
    }

    public void setSize(Vector2 size) {
        this.size = size;
    }

    public Vector2 getEmissionOffset() {
        return emissionOffset;
    }

    public void setEmissionOffset(Vector2 emissionOffset) {
        this.emissionOffset = emissionOffset;
    }

    public Set<YldPair<PosRef, Float>> getParticles() {
        return particles;
    }

    public static Texture getDefaultParticleTexture() {
        return defaultParticleTexture;
    }
}
