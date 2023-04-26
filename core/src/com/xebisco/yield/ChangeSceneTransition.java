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

    /**
     * The function returns the value of deltaTime, which is a measure of time elapsed since the last frame or
     * update.
     *
     * @return The method is returning a double value named `deltaTime`.
     */
    public double getDeltaTime() {
        return deltaTime;
    }

    /**
     * This function sets the value of deltaTime for an object.
     *
     * @param deltaTime deltaTime is a variable of type double that represents the time difference between two consecutive
     * updates in a simulation or game loop.
     */
    public void setDeltaTime(double deltaTime) {
        this.deltaTime = deltaTime;
    }

    /**
     * The function returns the value of the passedTime variable as a double.
     *
     * @return The method is returning a double value representing the amount of time that has passed.
     */
    public double getPassedTime() {
        return passedTime;
    }

    /**
     * This function sets the value of the "passedTime" variable.
     *
     * @param passedTime passedTime is a variable of type double that represents the amount of time that has passed.
     */
    public void setPassedTime(double passedTime) {
        this.passedTime = passedTime;
    }

    /**
     * The function returns the value of the variable "timeToWait" as a double.
     *
     * @return The method is returning a double value which is the value of the variable `timeToWait`.
     */
    public double getTimeToWait() {
        return timeToWait;
    }

    /**
     * The function returns the value of the frames variable.
     *
     * @return The method is returning an integer value of the variable "frames".
     */
    public int getFrames() {
        return frames;
    }

    /**
     * The function sets the number of frames for a given object.
     *
     * @param frames The "frames" parameter is an integer value that represents the number of frames passed.
     */
    public void setFrames(int frames) {
        this.frames = frames;
    }

    /**
     * The function returns an instance of the Application class.
     *
     * @return The method is returning an object of type `Application`.
     */
    public Application getApplication() {
        return application;
    }

    /**
     * This function sets the value of the "application" variable in the current object to the value passed as a parameter.
     *
     * @param application The parameter "application" is an object of the class "Application". The method "setApplication"
     * sets the value of the instance variable "application" to the value passed as a parameter.
     */
    public void setApplication(Application application) {
        this.application = application;
    }

    /**
     * The function returns a boolean value indicating whether the effect is finished or not.
     *
     * @return The method is returning a boolean value, specifically the value of the variable "finished".
     */
    public boolean isFinished() {
        return finished;
    }

    /**
     * This function sets the value of a boolean variable called "finished".
     *
     * @param finished A boolean variable that represents whether the effect has been completed or not.
     */
    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    /**
     * This function returns the value of a boolean variable called "stopUpdatingScene".
     *
     * @return The method is returning a boolean value, specifically the value of the variable `stopUpdatingScene`.
     */
    public boolean isStopUpdatingScene() {
        return stopUpdatingScene;
    }
}
