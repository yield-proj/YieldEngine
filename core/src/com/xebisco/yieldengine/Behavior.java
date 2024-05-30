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


import java.io.Closeable;

/**
 * This interface represents a behavior that can be started and updated over time.
 * It extends the {@link java.io.Closeable} interface to allow for proper resource management.
 */
public interface Behavior extends Closeable {
    /**
     * This method is called when the behavior is started.
     * It is typically used to initialize any necessary state or resources.
     */
    void onStart();
    /**
     * This method is called when the behavior needs to be updated with the current time.
     * It is typically used to perform any necessary calculations or updates based on the current time.
     *
     * @param time The current time context.
     */
    void onUpdate(ContextTime time);
}
