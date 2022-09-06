/*
 * Copyright [2022] [Xebisco]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xebisco.yield;

/**
 * A Condition is a boolean value that can be set and retrieved.
 * The class is a simple wrapper around a boolean value. It has a default constructor and a constructor that takes a
 * boolean value. It has a get() method that returns a new Condition with the same value as the current Condition. It has
 * an is() method that returns the boolean value. And it has a set() method that sets the boolean value
 */
public class Condition {
    private boolean value;

    public Condition() {
    }

    public Condition(boolean value) {
        this.value = value;
    }

    /**
     * This function returns a new Condition object with the value of the current object.
     *
     * @return A new instance of the Condition class.
     */
    public Condition get() {
        return new Condition(value);
    }

    /**
     * Returns true if the value is true, otherwise returns false.
     *
     * @return The value of the boolean variable value.
     */
    public boolean is() {
        return value;
    }

    /**
     * This function sets the value of the boolean variable to the value of the parameter.
     *
     * @param value The value to set the boolean to.
     */
    public void set(boolean value) {
        this.value = value;
    }
}
