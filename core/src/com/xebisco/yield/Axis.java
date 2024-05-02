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

/**
 * Represents an input axis with two keys for positive and negative directions.
 * It also includes optional keys for alternative positive and negative directions.
 *
 * @param positiveKey The key for the positive direction.
 * @param negativeKey The key for the negative direction.
 * @param altPositiveKey The optional key for the alternative positive direction.
 * @param altNegativeKey The optional key for the alternative negative direction.
 */
public record Axis(Input.Key positiveKey, Input.Key negativeKey, Input.Key altPositiveKey, Input.Key altNegativeKey) {

    /**
     * Constructor for Axis with only positive and negative keys.
     * The alternative positive and negative keys will be set to null.
     *
     * @param positiveKey The key for the positive direction.
     * @param negativeKey The key for the negative direction.
     */
    public Axis(Input.Key positiveKey, Input.Key negativeKey) {
        this(positiveKey, negativeKey, null, null);
    }
}
