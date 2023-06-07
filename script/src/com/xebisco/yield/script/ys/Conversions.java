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

package com.xebisco.yield.script.ys;

public class Conversions {
    public static Number toPrimitive(Number x) {
        switch (x.getClass().getSimpleName()) {
            case "Integer":
                return x.intValue();
            case "Float":
                return x.floatValue();
            case "Double":
                return x.doubleValue();
            case "Byte":
                return x.byteValue();
            case "Long":
                return x.longValue();
            case "Short":
                return x.shortValue();
        }
        throw new LangException("Not a basic number type");
    }
}
