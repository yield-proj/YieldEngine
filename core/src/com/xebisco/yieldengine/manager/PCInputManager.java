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

package com.xebisco.yieldengine.manager;

import com.xebisco.yieldengine.Input;
import com.xebisco.yieldengine.Vector2D;

import java.util.Collection;

/**
 * This interface represents a PC input manager that handles user input from a keyboard and mouse.
 */
public interface PCInputManager {
    /**
     * The function returns a collection of currently pressed keys.
     *
     * @return A collection of currently pressing keys is being returned.
     */
    Collection<Input.Key> getPressingKeys();

    /**
     * This function returns a collection of currently pressing mouse buttons.
     *
     * @return A collection of currently pressing mouse buttons.
     */
    Collection<Input.MouseButton> getPressingMouseButtons();

    //0.0 -> 1.0
    Vector2D mouse();
}