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

    public Texture[] frames() {
        return frames;
    }

    public Animation setFrames(Texture[] frames) {
        this.frames = frames;
        return this;
    }

    public double delay() {
        return delay;
    }

    public Animation setDelay(double delay) {
        this.delay = delay;
        return this;
    }

    public boolean loop() {
        return loop;
    }

    public Animation setLoop(boolean loop) {
        this.loop = loop;
        return this;
    }
}
