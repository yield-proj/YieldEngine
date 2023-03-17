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

public final class AnimationPlayer extends ComponentBehavior {
    private Animation animation;
    private Animation toSwitchAnimation;

    private int actualFrame;
    private double toChange;

    private TextureRectangle textureRectangle;

    @Override
    public void onStart() {
        textureRectangle = getComponent(TextureRectangle.class);
    }

    @Override
    public void onUpdate() {
        if (animation == null) {
            setAnimation(toSwitchAnimation);
            toSwitchAnimation = null;
        }
        if (animation != null) {
            toChange += getTime().getDeltaTime();
            if (actualFrame < animation.getFrames().length - 1) {
                if (toChange >= animation.getDelay()) {
                    toChange = 0;
                    actualFrame++;
                }
            }
            if (actualFrame == animation.getFrames().length - 1) {
                if (toChange >= animation.getDelay()) {
                    toChange = 0;
                    if (toSwitchAnimation != null) {
                        setAnimation(toSwitchAnimation);
                        toSwitchAnimation = null;
                    } else if (animation.isLoop()) actualFrame = 0;
                }
            }
            TextureRectangle r = getComponent(TextureRectangle.class);
            if(r != null)
                r.setTexture(animation.getFrames()[actualFrame]);
        }
    }

    public Animation getAnimation() {
        return animation;
    }

    public void setAnimation(Animation animation) {
        actualFrame = 0;
        toChange = 0;
        this.animation = animation;
    }

    public Animation getToSwitchAnimation() {
        return toSwitchAnimation;
    }

    public void setToSwitchAnimation(Animation toSwitchAnimation) {
        this.toSwitchAnimation = toSwitchAnimation;
    }

    public int getActualFrame() {
        return actualFrame;
    }

    public void setActualFrame(int actualFrame) {
        this.actualFrame = actualFrame;
    }

    public double getToChange() {
        return toChange;
    }

    public void setToChange(double toChange) {
        this.toChange = toChange;
    }
}
