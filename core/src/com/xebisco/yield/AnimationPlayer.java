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

package com.xebisco.yield;

import com.xebisco.yield.texture.TexturedSquareMesh;

/**
 * The AnimationPlayer class is a component behavior that handles the playing of animations by updating the texture
 * rectangle with the current frame.
 */
@ComponentIcon(iconType = ComponentIconType.ANIMATION)
public final class AnimationPlayer extends ComponentBehavior {
    private Animation animation;
    private Animation toSwitchAnimation;

    private int actualFrame;
    private double toChange;

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
                    } else if (animation.loop()) actualFrame = 0;
                }
            }
            TexturedSquareMesh r = component(TexturedSquareMesh.class);
            if (r != null)
                r.setTexture(animation.frames()[actualFrame]);
        }
    }

    /**
     * The function returns an Animation object.
     *
     * @return The method is returning an object of type `Animation`.
     */
    public Animation getAnimation() {
        return animation;
    }

    /**
     * This function sets the animation and resets the frame and change counters.
     *
     * @param animation The animation object that is being set for the current object.
     */
    public AnimationPlayer setAnimation(Animation animation) {
        actualFrame = 0;
        toChange = 0;
        this.animation = animation;
        return this;
    }

    public Animation animation() {
        return animation;
    }

    public Animation toSwitchAnimation() {
        return toSwitchAnimation;
    }

    public AnimationPlayer setToSwitchAnimation(Animation toSwitchAnimation) {
        this.toSwitchAnimation = toSwitchAnimation;
        return this;
    }

    public int actualFrame() {
        return actualFrame;
    }

    public AnimationPlayer setActualFrame(int actualFrame) {
        this.actualFrame = actualFrame;
        return this;
    }

    public double toChange() {
        return toChange;
    }

    public AnimationPlayer setToChange(double toChange) {
        this.toChange = toChange;
        return this;
    }
}
