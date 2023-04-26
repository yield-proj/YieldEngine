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

public class ToggleFullScreenSystem extends SystemBehavior {

    private boolean canStartAction, doAction, onFullScreen;

    @Override
    public void onStart() {
        onFullScreen = getScene().getApplication().getPlatformInit().isFullscreen();
    }

    @Override
    public void onUpdate() {
        if (getScene().getApplication().isPressingKey(Input.Key.VK_ALT)) {
            if (getScene().getApplication().isPressingKey(Input.Key.VK_ENTER)) {
                if (doAction) {
                    doAction = false;
                    onFullScreen = !onFullScreen;
                    getScene().getApplication().getToggleFullScreen().setFullScreen(onFullScreen);
                }
            } else {
                doAction = true;
            }
        } else {
            doAction = false;
        }
    }

    public boolean isCanStartAction() {
        return canStartAction;
    }

    public void setCanStartAction(boolean canStartAction) {
        this.canStartAction = canStartAction;
    }

    public boolean isDoAction() {
        return doAction;
    }

    public void setDoAction(boolean doAction) {
        this.doAction = doAction;
    }

    public boolean isOnFullScreen() {
        return onFullScreen;
    }

    public void setOnFullScreen(boolean onFullScreen) {
        this.onFullScreen = onFullScreen;
    }
}
