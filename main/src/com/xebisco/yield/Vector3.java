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

public class Vector3 extends Vector2 {
    public float z;
    public Vector3() {

    }

    public Vector3(float x, float y, float z) {
        super(x, y);
        this.z = z;
    }

    public Vector3(float x, float y) {
        super(x, y);
    }

    public Vector3(Vector2 vector2) {
        super(vector2.x, vector2.y);
    }

    @Override
    public Vector3 get() {
        return new Vector3(x, y, z);
    }
}
