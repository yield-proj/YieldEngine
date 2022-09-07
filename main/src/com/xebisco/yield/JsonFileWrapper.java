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

import com.google.gson.stream.JsonReader;

import java.io.InputStreamReader;

public class JsonFileWrapper extends RelativeFile {

    private final Object object;
    private final Class<?> type;

    public JsonFileWrapper(String relativePath, Class<?> type) {
        super(relativePath);
        this.type = type;
        object = Yld.getGson().fromJson(new JsonReader(new InputStreamReader(getInputStream())), type);
    }

    public Object getObject() {
        return type.cast(object);
    }

    public Class<?> getType() {
        return type;
    }
}
