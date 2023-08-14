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
 * This class toggles full screen mode in an application when the user presses the ALT and ENTER keys.
 */
public class ToggleFullScreenSystem extends SystemBehavior {

    private boolean canStartAction, doAction, onFullScreen;

    @Override
    public void onStart() {
        onFullScreen = getScene().application().platformInit().fullscreen();
    }

    @Override
    public void onUpdate() {
        if (getScene().application().pressingKey(Input.Key.VK_ALT)) {
            if (getScene().application().pressingKey(Input.Key.VK_ENTER)) {
                if (doAction) {
                    doAction = false;
                    onFullScreen = !onFullScreen;
                    getScene().application().applicationPlatform().toggleFullScreen().setFullScreen(onFullScreen);
                }
            } else {
                doAction = true;
            }
        } else {
            doAction = false;
        }
    }

    /**
     * The function returns a boolean value indicating whether the program is currently on full screen mode or not.
     *
     * @return The method is returning the value of the boolean variable `onFullScreen`.
     */
    public boolean isOnFullScreen() {
        return onFullScreen;
    }
}
