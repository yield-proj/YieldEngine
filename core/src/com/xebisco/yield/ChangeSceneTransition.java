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

/**
 * The abstract class ChangeSceneTransition contains methods for managing time and frames in a scene transition effect.
 */
public abstract class ChangeSceneTransition implements Renderable {

    private double deltaTime, passedTime;
    private final double timeToWait;
    private Application application;
    private int frames;
    private boolean finished;
    private final boolean stopUpdatingScene;

    protected ChangeSceneTransition(double timeToWait, boolean stopUpdatingScene) {
        this.timeToWait = timeToWait;
        this.stopUpdatingScene = stopUpdatingScene;
    }

    public double deltaTime() {
        return deltaTime;
    }

    public ChangeSceneTransition setDeltaTime(double deltaTime) {
        this.deltaTime = deltaTime;
        return this;
    }

    public double passedTime() {
        return passedTime;
    }

    public ChangeSceneTransition setPassedTime(double passedTime) {
        this.passedTime = passedTime;
        return this;
    }

    public double timeToWait() {
        return timeToWait;
    }

    public Application application() {
        return application;
    }

    public ChangeSceneTransition setApplication(Application application) {
        this.application = application;
        return this;
    }

    public int frames() {
        return frames;
    }

    public ChangeSceneTransition setFrames(int frames) {
        this.frames = frames;
        return this;
    }

    public boolean finished() {
        return finished;
    }

    public ChangeSceneTransition setFinished(boolean finished) {
        this.finished = finished;
        return this;
    }

    public boolean stopUpdatingScene() {
        return stopUpdatingScene;
    }
}
