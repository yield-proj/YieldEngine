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
            if (r != null)
                r.setTexture(animation.getFrames()[actualFrame]);
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
    public void setAnimation(Animation animation) {
        actualFrame = 0;
        toChange = 0;
        this.animation = animation;
    }

    /**
     * The function returns the "toSwitchAnimation" object of type Animation.
     *
     * @return The method is returning an object of type `Animation`. The specific `Animation` object being returned is
     * stored in the variable `toSwitchAnimation`.
     */
    public Animation getToSwitchAnimation() {
        return toSwitchAnimation;
    }

    /**
     * This function sets the value of a variable called "toSwitchAnimation" to the value of the input parameter
     * "toSwitchAnimation".
     *
     * @param toSwitchAnimation The parameter "toSwitchAnimation" is an object of the class "Animation". This method sets
     *                          the value of the instance variable "toSwitchAnimation" to the value passed as the parameter, which is the next animation that is going to be played by this `AnimationPlayer` object.
     */
    public void setToSwitchAnimation(Animation toSwitchAnimation) {
        this.toSwitchAnimation = toSwitchAnimation;
    }

    /**
     * The function returns the value of the variable "actualFrame".
     *
     * @return The method is returning the value of the variable "actualFrame".
     */
    public int getActualFrame() {
        return actualFrame;
    }

    /**
     * This function sets the value of the "actualFrame" variable.
     *
     * @param actualFrame actualFrame is a variable of type int that represents the current frame of an animation. The
     * method setActualFrame is a setter method that sets the value of actualFrame to the value passed as a parameter.
     */
    public void setActualFrame(int actualFrame) {
        this.actualFrame = actualFrame;
    }

    /**
     * The function returns the value of a double variable named "toChange".
     *
     * @return The method is returning a double value named "toChange".
     */
    public double getToChange() {
        return toChange;
    }

    /**
     * This function sets the value of a variable named "toChange".
     *
     * @param toChange toChange is a variable of type double that is being set using the setter method.
     */
    public void setToChange(double toChange) {
        this.toChange = toChange;
    }
}
