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

@FunctionalInterface
public interface EntityStarter {
    /**
     * The function "start" takes an object of type Entity2D as a parameter.
     *
     * @param e The parameter "e" in the function "start" is of type Entity2D, which means it is an object or instance of
     * the Entity2D class. This parameter is passed to the function as an argument and can be used within the function to
     * access the properties and methods of the Entity2D
     */
    void start(Entity2D e);
}
