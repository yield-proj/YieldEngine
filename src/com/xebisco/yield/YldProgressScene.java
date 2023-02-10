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

/**
 * It's a scene that can be used to show progress of loading other scenes, like a loading screen.
 */
public class YldProgressScene extends YldScene {
    private float progress;
    private Class<? extends YldScene> toChangeScene;



    /**
     * This function changes the scene to the toChangeScene.
     */
    public void change() {
        game.setScene(toChangeScene);
    }

    /**
     * This function returns the progress of the current task.
     *
     * @return The progress variable is being returned.
     */
    public float getProgress() {
        return progress;
    }

    /**
     * This function sets the progress to the value of the parameter progress.
     *
     * @param progress The progress value to set.
     */
    public void setProgress(float progress) {
        this.progress = progress;
    }

    /**
     * It returns the scene that will be loaded.
     *
     * @return The scene to change to.
     */
    public Class<? extends YldScene> getToChangeScene() {
        return toChangeScene;
    }

    /**
     * This function sets the value of the variable toChangeScene to the value of the variable toChangeScene that is passed
     * into the function.
     *
     * @param toChangeScene The name of the scene to change to.
     */
    public void setToChangeScene(Class<? extends YldScene> toChangeScene) {
        this.toChangeScene = toChangeScene;
    }
}
