/*
 * Copyright [2022] [Xebisco]
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
 * This class is an implementation of YldSystem, is made to process on every update of a YldScene.
 */
public abstract class UpdateSystem extends YldSystem {
    /**
     * Called on every frame of a YldScene instance.
     * @param delta The time variation between the last frame and the actual one in seconds.
     */
    public abstract void update(float delta);
}
