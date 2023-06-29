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

package com.xebisco.yield.script.obj;

import com.xebisco.yield.script.YS10;

public class ObjectValue {
    private Object value;
    private final ObjectValueGet objectValueGet;
    private final ObjectValueSet objectValueSet;

    private final Class<?> cast;

    public ObjectValue(ObjectValueGet objectValueGet, ObjectValueSet objectValueSet, Object value, Class<?> cast) {
        this.objectValueGet = objectValueGet;
        this.objectValueSet = objectValueSet;
        if (cast == null)
            this.value = value;
        else this.value = YS10.ysCastObject(value, cast);
        this.cast = cast;
    }

    public Object getValue() {
        if (cast != null) YS10.ysCastObject(value, cast);
        return objectValueGet.get(value);
    }

    public void setValue(Object value) {
        if (cast != null) YS10.ysCastObject(value, cast);
        this.value = objectValueSet.set(value);
    }

    public ObjectValueGet getObjectValueGet() {
        return objectValueGet;
    }

    public ObjectValueSet getObjectValueSet() {
        return objectValueSet;
    }

    public Class<?> getCast() {
        if (cast == null)
            return value.getClass();
        else
            return cast;
    }
}
