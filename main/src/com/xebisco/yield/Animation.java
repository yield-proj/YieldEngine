/*
 * Copyright [2022] [Xebisco]
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
 * This class is a list of Textures that can be used to make animated Sprites.
 */
public class Animation {
    private final Texture[] frames;
    private final String name;
    private int actFrame = 0, frameDelay = 0, microsecondDelay = 1667;

    /**
     * Create an Animation instance with predefined name and frames.
     *
     * @param name   The name of this Animation.
     * @param frames All the frames of this Animation.
     */
    public Animation(String name, Texture... frames) {
        this.frames = frames;
        this.name = name;
    }

    public Animation(int microsecondDelay, String name, Texture... frames) {
        this.microsecondDelay = microsecondDelay;
        this.frames = frames;
        this.name = name;
    }

    public Animation(int microsecondDelay, Texture... frames) {
        this.microsecondDelay = microsecondDelay;
        this.frames = frames;
        this.name = "default";
    }

    /**
     * Create an Animation instance with predefined frames.
     *
     * @param frames All the frames of this Animation.
     */
    public Animation(Texture... frames) {
        this.frames = frames;
        this.name = "default";
    }

    /**
     * This function returns the frames array.
     *
     * @return The frames array.
     */
    public Texture[] getFrames() {
        return frames;
    }

    /**
     * This function returns the current frame of the animation
     *
     * @return The actFrame variable is being returned.
     */
    public int getActFrame() {
        return actFrame;
    }

    /**
     * This function sets the actFrame variable to the value of the actFrame parameter.
     *
     * @param actFrame The current frame of the animation.
     */
    public void setActFrame(int actFrame) {
        this.actFrame = actFrame;
    }

    /**
     * This function returns the frame delay of the animation
     *
     * @return The frameDelay variable is being returned.
     */
    public int getFrameDelay() {
        return frameDelay;
    }

    /**
     * This function sets the frame delay to the value passed in.
     *
     * @param frameDelay The amount of time in frames to wait before displaying the next frame.
     */
    public void setFrameDelay(int frameDelay) {
        this.frameDelay = frameDelay;
    }

    /**
     * This function returns the name of the animation.
     *
     * @return The name of the animation.
     */
    public String getName() {
        return name;
    }

    /**
     * It returns the microsecond delay.
     *
     * @return The microsecondDelay
     */
    public int getMicrosecondDelay() {
        return microsecondDelay;
    }

    /**
     * This function sets the frame delay to the value passed in.
     *
     * @param microsecondDelay The amount of time in milliseconds to wait before displaying the next frame.
     */
    public void setMicrosecondDelay(int microsecondDelay) {
        this.microsecondDelay = microsecondDelay;
    }
}
