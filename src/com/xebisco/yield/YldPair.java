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

import java.util.Objects;

/**
 * It's a generic class that holds two objects of any type
 * @author Xebisco
 * @since 4-1.2
 */
public class YldPair<F, S> {
    private F first;
    private S second;

    public YldPair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        YldPair<?, ?> pair = (YldPair<?, ?>) o;
        return Objects.equals(first, pair.first) && Objects.equals(second, pair.second);
    }

    @Override
    public String toString() {
        return "YldPair{" +
                "first=" + first +
                ", second=" + second +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

    /**
     * Returns the first element of the pair.
     *
     * @return The first element of the pair.
     */
    public F getFirst() {
        return first;
    }

    /**
     * This function sets the first element of the pair to the value of the first parameter.
     *
     * @param first The first element of the pair.
     */
    public void setFirst(F first) {
        this.first = first;
    }

    /**
     * Returns the second element of the pair.
     *
     * @return The second element of the pair.
     */
    public S getSecond() {
        return second;
    }

    /**
     * This function sets the second element of the pair to the value of the second parameter.
     *
     * @param second The second value of the pair.
     */
    public void setSecond(S second) {
        this.second = second;
    }
}
