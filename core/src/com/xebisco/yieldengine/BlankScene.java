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

package com.xebisco.yieldengine;

/**
 * The BlankScene class is a custom scene for an application.
 * It sets the background color to black when the scene starts.
 */
public class BlankScene extends Scene {

    /**
     * Constructs a new BlankScene object with the given application.
     *
     * @param application the application to which the scene belongs
     */
    public BlankScene(Application application) {
        super(application);
    }

    @Override
    public void onStart() {
        setBackGroundColor(Colors.BLACK);
    }
}
