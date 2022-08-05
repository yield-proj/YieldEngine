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

public class Vector extends Vector2 {

    private final float module, angle;

    public Vector(float module, float angle) {
        if(module < 0)
            throw new ArithmeticException("Vector's module can't be less than 0");
        this.module = module;
        this.angle = angle;
        float rad = (float) Math.toRadians(angle);
        x = (float) Math.cos(rad) * module;
        y = (float) -Math.sin(rad) * module;
    }

    public float getModule() {
        return module;
    }

    public float getAngle() {
        return angle;
    }
}
