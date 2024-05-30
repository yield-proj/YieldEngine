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

package com.xebisco.yieldengine.openglimpl.shader;

import static org.lwjgl.opengl.GL11.*;

public enum DataType {
    BYTE, UNSIGNED_BYTE, SHORT, UNSIGNED_SHORT, UNSIGNED_INT;
    public static int toGL(DataType dataType) {
        switch (dataType) {
            case BYTE -> {
                return GL_BYTE;
            }
            case UNSIGNED_BYTE -> {
                return GL_UNSIGNED_BYTE;
            }
            case SHORT -> {
                return GL_SHORT;
            }
            case UNSIGNED_SHORT -> {
                return GL_UNSIGNED_SHORT;
            }
            case UNSIGNED_INT -> {
                return GL_UNSIGNED_INT;
            }
        }
        throw new IllegalArgumentException();
    }
}
