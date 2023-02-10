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

import java.util.ArrayList;

/**
 * This class is used to animate a texture
 */
public class Animator extends Component {

    private ArrayList<Animation> animations = new ArrayList<>();
    private Animation actAnim;
    private boolean autoReplay = true, finished, resetAnimation;
    private int actAnimFrame, overlayTime;
    private boolean updateSpriteSize;

    @Override
    public void update(float delta) {
        if (actAnim != null && !finished) {
            if (overlayTime == 0) {
                if (actAnim.getMicrosecondDelay() == 0) {
                    actAnimFrame++;
                    if (actAnimFrame >= actAnim.getFrameDelay()) {
                        actAnimFrame = 0;
                        actAnim.setActFrame(actAnim.getActFrame() + 1);
                    }
                } else {
                    actAnimFrame += (delta * 1000f);
                    if (actAnimFrame >= actAnim.getMicrosecondDelay()) {
                        actAnimFrame = 0;
                        actAnim.setActFrame(actAnim.getActFrame() + 1);
                    }
                }
            } else {
                actAnimFrame += (delta * 1000f);
                if (actAnimFrame >= overlayTime) {
                    actAnimFrame = 0;
                    actAnim.setActFrame(actAnim.getActFrame() + 1);
                }
            }
            if (actAnim.getActFrame() >= actAnim.getFrames().length) {
                if (!autoReplay) {
                    finished = true;
                } else {
                    actAnim.setActFrame(0);
                    finished = false;
                }
            }
            getMaterial().setTexture(actAnim.getFrames()[actAnim.getActFrame()]);
            Sprite sprite = getComponent(Sprite.class);
            if(sprite != null && updateSpriteSize) {
                sprite.setSizeAsTexture(getMaterial().getTexture());
            }
        }
    }

    /**
     * It sets the current animation to the animation with the name of the parameter
     *
     * @param animName The name of the animation you want to set.
     */
    public void setAnimation(String animName) {
        boolean finded = false;
        for (Animation animation : animations) {
            if (animation.getName().hashCode() == animName.hashCode() && animation.getName().equals(animName)) {
                finded = true;
                setActAnim(animation);
                if(resetAnimation)
                    getActAnim().setActFrame(0);
                actAnimFrame = 0;
                break;
            }
        }
        if (!finded)
            throw new IllegalArgumentException("'" + animName + "' is not a animation");
    }

    /**
     * Sets the animation to the given animation, and sets the animation frame to 0.
     *
     * @param anim The animation to set.
     */
    public void setAnimation(Animation anim) {
        setActAnim(anim);
        actAnimFrame = 0;
    }

    /**
     * Adds an animation to the list of animations.
     *
     * @param animation The animation to add.
     */
    public void addAnimation(Animation animation) {
        animations.add(animation);
    }

    /**
     * This function returns the actAnim variable.
     *
     * @return The actAnim variable is being returned.
     */
    public Animation getActAnim() {
        return actAnim;
    }

    /**
     * If the animation is not finished, set the current animation to the new animation.
     *
     * @param actAnim The animation to be played.
     */
    public void setActAnim(Animation actAnim) {
        finished = false;
        this.actAnim = actAnim;
    }

    /**
     * This function returns the animations array list.
     *
     * @return An ArrayList of Animation objects.
     */
    public ArrayList<Animation> getAnimations() {
        return animations;
    }

    /**
     * This function sets the animations of the current object to the animations passed in as a parameter.
     *
     * @param animations The list of animations.
     */
    public void setAnimations(ArrayList<Animation> animations) {
        this.animations = animations;
    }

    /**
     * This function returns the current animation frame.
     *
     * @return The actAnimFrame variable is being returned.
     */
    public int getActAnimFrame() {
        return actAnimFrame;
    }

    /**
     * This function sets the current animation frame to the value of the parameter actAnimFrame.
     *
     * @param actAnimFrame The current frame of the animation.
     */
    public void setActAnimFrame(int actAnimFrame) {
        this.actAnimFrame = actAnimFrame;
    }

    /**
     * This function returns the time that the overlay will be displayed
     *
     * @return The overlayTime variable is being returned.
     */
    public int getOverlayTime() {
        return overlayTime;
    }

    /**
     * This function sets the overlay time for the animation.
     *
     * @param overlayTime The time in milliseconds to overlay in the animation.
     */
    public void setOverlayTime(int overlayTime) {
        this.overlayTime = overlayTime;
    }

    /**
     * Returns whether the animator will automatically replay the animation when it ends
     *
     * @return The value of the autoReplay variable.
     */
    public boolean isAutoReplay() {
        return autoReplay;
    }

    /**
     * If autoReplay is true, set finished to false
     *
     * @param autoReplay If true, the animation will automatically replay when it finishes.
     */
    public void setAutoReplay(boolean autoReplay) {
        if (autoReplay)
            finished = false;
        this.autoReplay = autoReplay;
    }

    /**
     * Returns true if the animation is finished, false otherwise.
     *
     * @return The boolean value of the variable finished.
     */
    public boolean isFinished() {
        return finished;
    }

    /**
     * This function sets the value of the finished variable to the value of the finished parameter.
     *
     * @param finished This is a boolean value that indicates whether the animation is finished or not.
     */
    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    /**
     * Returns true if the animation should reset when switched.
     *
     * @return The value of the resetAnimation variable.
     */
    public boolean isResetAnimation() {
        return resetAnimation;
    }

    /**
     * This function sets the resetAnimation variable to the value of the parameter passed in
     *
     * @param resetAnimation If true, the animation will reset when switched.
     */
    public void setResetAnimation(boolean resetAnimation) {
        this.resetAnimation = resetAnimation;
    }

    /**
     * Returns whether the sprite size should be updated
     *
     * @return The value of the updateSpriteSize variable.
     */
    public boolean isUpdateSpriteSize() {
        return updateSpriteSize;
    }

    /**
     * This function sets the updateSpriteSize variable to the value of the parameter passed in
     *
     * @param updateSpriteSize If true, the sprite's size will be updated to match the size of the actual animation frame.
     */
    public void setUpdateSpriteSize(boolean updateSpriteSize) {
        this.updateSpriteSize = updateSpriteSize;
    }
}
