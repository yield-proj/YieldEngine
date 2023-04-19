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
 * The ImmutableSize2D class extends Size2D and overrides the setX and setY methods to throw an exception, making it
 * immutable.
 */
public class ImmutableSize2D extends Size2D {
    public ImmutableSize2D(double width, double height) {
        super(width, height);
    }

    @Override
    public void setX(double x) {
        throw new ImmutableBreakException();
    }

    @Override
    public void setY(double y) {
        throw new ImmutableBreakException();
    }
}
