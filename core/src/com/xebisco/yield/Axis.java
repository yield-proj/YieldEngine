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

/**
 * The class "Axis" represents an input axis with keys for positive and negative values, and can store and retrieve its
 * current value.
 */
public class Axis {
    private final String name;
    private final Input.Key positiveKey, negativeKey, altPositiveKey, altNegativeKey;
    private double value;

    public Axis(String name, Input.Key positiveKey, Input.Key negativeKey, Input.Key altPositiveKey, Input.Key altNegativeKey) {
        this.name = name;
        this.positiveKey = positiveKey;
        this.negativeKey = negativeKey;
        this.altPositiveKey = altPositiveKey;
        this.altNegativeKey = altNegativeKey;
    }

    public Axis(String name, Input.Key positiveKey, Input.Key negativeKey) {
        this.name = name;
        this.positiveKey = positiveKey;
        this.negativeKey = negativeKey;
        this.altPositiveKey = null;
        this.altNegativeKey = null;
    }

    /**
     * The function returns the name.
     *
     * @return The method `getName()` is returning the value of the `name` variable, which is a `String`.
     */
    public String getName() {
        return name;
    }

    /**
     * The function returns the positive key input.
     *
     * @return The method is returning the value of the variable `positiveKey`, which is of type `Input.Key`.
     */
    public Input.Key getPositiveKey() {
        return positiveKey;
    }

    /**
     * The function returns the negative key input.
     *
     * @return The method is returning the value of the variable `negativeKey`, which is of type `Input.Key`.
     */
    public Input.Key getNegativeKey() {
        return negativeKey;
    }

    /**
     * The function returns the alternate positive key input.
     *
     * @return The method is returning the value of the variable `altPositiveKey`, which is of type `Input.Key`.
     */
    public Input.Key getAltPositiveKey() {
        return altPositiveKey;
    }

    /**
     * The function returns the alternate negative key input.
     *
     * @return The method is returning the value of the variable `altNegativeKey`, which is of type `Input.Key`.
     */
    public Input.Key getAltNegativeKey() {
        return altNegativeKey;
    }

    /**
     * The function returns the value of this axis.
     *
     * @return The value of the variable "value".
     */
    public double getValue() {
        return value;
    }

    /**
     * The function sets the value of this axis.
     *
     * @param value The value to be set.
     */
    public void setValue(double value) {
        this.value = value;
    }
}
