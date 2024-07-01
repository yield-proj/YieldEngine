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

package com.xebisco.yield.core.rendering;

import org.joml.*;

public enum DataType {
    //Basic Types
    BOOLEAN(boolean.class), INT(int.class), FLOAT(float.class),
    //Vector Types
    I_VEC2(Vector2ic.class), I_VEC3(Vector3ic.class), I_VEC4(Vector4ic.class),
    F_VEC2(Vector2fc.class), F_VEC3(Vector3fc.class), F_VEC4(Vector4fc.class),

    //Matrix Types
    F_MAT2(Matrix2fc.class), F_MAT3(Matrix3fc.class), F_MAT4(Matrix4fc.class),

    //Specialized Types
    SAMPLER_2D(int.class);

    private final Class<?> javaType;

    DataType(Class<?> javaType) {
        this.javaType = javaType;
    }

    public static DataType getDataType(Class<?> javaType) throws InvalidShaderTypeException {
        for (DataType dataType : values()) {
            if (dataType.javaType.isAssignableFrom(javaType)) {
                return dataType;
            }
        }
        throw new InvalidShaderTypeException(javaType.toString());
    }

    public Class<?> getJavaType() {
        return javaType;
    }
}
