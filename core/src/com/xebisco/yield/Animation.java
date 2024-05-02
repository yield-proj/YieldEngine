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

package com.xebisco.yield;

import com.xebisco.yield.texture.Texture;

import java.io.Closeable;
import java.io.IOException;

/**
 * Represents an animation made up of multiple frames.
 */
public class Animation implements Closeable {
    private AbstractTexture[] frames;
    private double delay;
    private boolean loop;

    /**
     * Constructor with looping enabled and 0.5-second delay.
     *
     * @param frames The frames of the animation.
     */
    public Animation(AbstractTexture... frames) {
        this(true, 0.5, frames);
    }

    /**
     * Constructor with customizable loop and delay.
     *
     * @param loop   Whether the animation should loop.
     * @param delay  The delay between frames in seconds.
     * @param frames The frames of the animation.
     */
    public Animation(boolean loop, double delay, AbstractTexture... frames) {
        this.loop = loop;
        this.frames = frames;
        this.delay = delay;
    }

    /**
     * Closes all frames of the animation.
     *
     * @throws IOException If an I/O error occurs.
     */
    @Override
    public void close() throws IOException {
        for (AbstractTexture t : frames)
            t.close();
    }

    /**
     * Returns the frames of the animation.
     *
     * @return The frames of the animation.
     */
    public AbstractTexture[] frames() {
        return frames;
    }

    /**
     * Sets the frames of the animation.
     *
     * @param frames The new frames of the animation.
     * @return This animation instance for chaining.
     */
    public Animation setFrames(AbstractTexture[] frames) {
        this.frames = frames;
        return this;
    }

    /**
     * Returns the delay between frames in seconds.
     *
     * @return The delay between frames in seconds.
     */
    public double delay() {
        return delay;
    }

    /**
     * Sets the delay between frames in seconds.
     *
     * @param delay The new delay between frames in seconds.
     * @return This animation instance for chaining.
     */
    public Animation setDelay(double delay) {
        this.delay = delay;
        return this;
    }

    /**
     * Returns whether the animation should loop.
     *
     * @return Whether the animation should loop.
     */
    public boolean loop() {
        return loop;
    }

    /**
     * Sets whether the animation should loop.
     *
     * @param loop Whether the animation should loop.
     * @return This animation instance for chaining.
     */
    public Animation setLoop(boolean loop) {
        this.loop = loop;
        return this;
    }
}