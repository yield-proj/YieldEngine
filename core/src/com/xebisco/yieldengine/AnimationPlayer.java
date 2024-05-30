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

package com.xebisco.yieldengine;

import com.xebisco.yieldengine.texture.TexturedRectangleMesh;

/**
 * The AnimationPlayer class is a component behavior that handles the playing of animations by updating the texture
 * rectangle with the current frame.
 */
public final class AnimationPlayer extends ComponentBehavior {
    private Animation animation;
    private Animation toSwitchAnimation;

    private int actualFrame;
    private double toChange;

    /**
     * This method is called every frame to update the animation player.
     *
     * @param time The context time, containing delta time.
     */
    @Override
    public void onUpdate(ContextTime time) {
        if (animation == null) {
            setAnimation(toSwitchAnimation);
            toSwitchAnimation = null;
        }

        if (animation != null) {
            toChange += time.deltaTime();

            if (actualFrame < animation.frames().length - 1) {
                if (toChange >= animation.delay()) {
                    toChange = 0;
                    actualFrame++;
                }
            }

            if (actualFrame == animation.frames().length - 1) {
                if (toChange >= animation.delay()) {
                    toChange = 0;

                    if (toSwitchAnimation != null) {
                        setAnimation(toSwitchAnimation);
                        toSwitchAnimation = null;
                    }
                    else if (animation.loop()) actualFrame = 0;
                }
            }

            TexturedRectangleMesh r = component(TexturedRectangleMesh.class);
            if (r != null)
                r.setTexture(animation.frames()[actualFrame]);
        }
    }

    /**
     * Returns the currently playing animation.
     *
     * @return The currently playing animation.
     */
    public Animation getAnimation() {
        return animation;
    }

    /**
     * Sets the current animation to the given animation.
     *
     * @param animation The animation to set.
     * @return This AnimationPlayer instance.
     */
    public AnimationPlayer setAnimation(Animation animation) {
        actualFrame = 0;
        toChange = 0;
        this.animation = animation;
        return this;
    }

    /**
     * Returns the currently playing animation.
     *
     * @return The currently playing animation.
     */
    public Animation animation() {
        return animation;
    }

    /**
     * Returns the animation that will be played after the current animation finishes.
     *
     * @return The animation that will be played after the current animation finishes.
     */
    public Animation toSwitchAnimation() {
        return toSwitchAnimation;
    }

    /**
     * Sets the animation that will be played after the current animation finishes.
     *
     * @param toSwitchAnimation The animation to set.
     * @return This AnimationPlayer instance.
     */
    public AnimationPlayer setToSwitchAnimation(Animation toSwitchAnimation) {
        this.toSwitchAnimation = toSwitchAnimation;
        return this;
    }

    /**
     * Returns the index of the current frame in the animation.
     *
     * @return The index of the current frame in the animation.
     */
    public int actualFrame() {
        return actualFrame;
    }

    /**
     * Sets the index of the current frame in the animation.
     *
     * @param actualFrame The index of the frame to set.
     * @return This AnimationPlayer instance.
     */
    public AnimationPlayer setActualFrame(int actualFrame) {
        this.actualFrame = actualFrame;
        return this;
    }

    /**
     * Returns the time since the last frame change.
     *
     * @return The time since the last frame change.
     */
    public double toChange() {
        return toChange;
    }

    /**
     * Sets the time since the last frame change.
     *
     * @param toChange The time to set.
     * @return This AnimationPlayer instance.
     */
    public AnimationPlayer setToChange(double toChange) {
        this.toChange = toChange;
        return this;
    }
}
