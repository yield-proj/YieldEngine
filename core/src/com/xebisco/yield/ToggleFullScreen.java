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

public interface ToggleFullScreen {
    /**
     * The function sets the full screen mode of a display.
     *
     * @param fullScreen A boolean parameter that determines whether the application should be displayed in full screen
     * mode or not. If the value is true, the application will be displayed in full screen mode, otherwise it will be
     * displayed in windowed mode.
     */
    void setFullScreen(boolean fullScreen);
}