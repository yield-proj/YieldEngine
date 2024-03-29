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


import java.io.Closeable;

/**
 * The Behavior is the main interface of the Yield Game Engine, almost all elements of an application implements this interface.
 */
public interface Behavior extends Closeable {
    /**
     * This function is called when the object starts
     */
    void onStart();
    /**
     * This function is called every time the application updates
     */
    void onUpdate(ContextTime time);
}
