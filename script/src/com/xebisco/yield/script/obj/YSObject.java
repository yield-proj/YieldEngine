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

import java.util.HashMap;
import java.util.Map;

public class YSObject {
    private final Map<String, ObjectValue> variables = new HashMap<>();

    public YSObject() {
        variables.put("null", new ObjectValue(new StandardGet(), new ImmutableSet(), null, null));
        variables.put("self", new ObjectValue(new StandardGet(), new ImmutableSet(), this, YSObject.class));
        variables.put("true", new ObjectValue(new StandardGet(), new ImmutableSet(), true, null));
        variables.put("false", new ObjectValue(new StandardGet(), new ImmutableSet(), false, null));
    }

    public Map<String, ObjectValue> getVariables() {
        return variables;
    }
}
