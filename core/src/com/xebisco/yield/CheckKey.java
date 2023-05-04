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

public interface CheckKey {
    /**
     * The function checks if a certain key is pressed or not.
     *
     * @param key The "key" parameter in the method signature is likely an object of the Input.Key class, which represents
     * a keyboard key. This method is used to check if a specific key has been pressed or released. The method
     * return a boolean value indicating whether the specified key is currently pressed or not.
     * @return A boolean value is being returned.
     */
    boolean checkKey(Input.Key key);
    /**
     * The function checks if a specific mouse button is being pressed.
     *
     * @param button The "button" parameter is a variable that represents a mouse button. It is of type
     * "Input.MouseButton", which is an enumeration that defines the different mouse buttons that can be pressed.
     * @return A boolean value is being returned. The method is likely checking whether the specified mouse button is
     * currently pressed or not, and returning true if it is pressed and false if it is not pressed.
     */
    boolean checkMouseButton(Input.MouseButton button);
}
