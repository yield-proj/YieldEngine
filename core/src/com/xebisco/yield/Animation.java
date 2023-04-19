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
 * The Animation class represents a sequence of textures with a delay and loop option, and can be disposed.
 */
public class Animation implements Disposable {
    private Texture[] frames;
    private double delay;
    private boolean loop;

    public Animation(Texture... frames) {
        this(true, 0.5, frames);
    }

    public Animation(boolean loop, double delay, Texture... frames) {
        this.loop = loop;
        this.frames = frames;
        this.delay = delay;
    }

    @Override
    public void dispose() {
        for (Texture t : frames)
            t.dispose();
    }

    /**
     * The function returns an array of Texture objects.
     *
     * @return An array of Texture objects named "frames" is being returned.
     */
    public Texture[] getFrames() {
        return frames;
    }

    /**
     * This function sets the frames of a texture.
     *
     * @param frames The "frames" parameter is an array of Texture objects. The method "setFrames" sets the value of the
     *               instance variable "frames" to the value of the "frames" parameter.
     */
    public void setFrames(Texture[] frames) {
        this.frames = frames;
    }

    /**
     * The function returns the value of the delay variable as a double.
     *
     * @return The method `getDelay()` is returning a `double` value, which is the value of the variable `delay`.
     */
    public double getDelay() {
        return delay;
    }

    /**
     * The function sets the delay value for a certain object.
     *
     * @param delay The "delay" parameter is a double data type that represents the amount of time (in seconds) to wait
     *              before going to the next frame. This method sets the value of the "delay"
     *              instance variable to the specified value passed as a parameter.
     */
    public void setDelay(double delay) {
        this.delay = delay;
    }

    /**
     * The function returns a boolean value indicating whether the animation will loop or not.
     *
     * @return The method `isLoop()` is returning a boolean value, which is the value of the variable `loop`.
     */
    public boolean isLoop() {
        return loop;
    }

    /**
     * This function sets the value of a boolean variable called "loop".
     *
     * @param loop The "loop" parameter is a boolean variable that is used to control whether the animation will loop or not. If the value of "loop" is true, then the animation will be repeated
     *             indefinitely until it is explicitly stopped or interrupted.
     */
    public void setLoop(boolean loop) {
        this.loop = loop;
    }

}
