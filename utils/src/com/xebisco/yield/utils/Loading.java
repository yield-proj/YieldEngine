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

package com.xebisco.yield.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class Loading {
    public static void applyPropsToObject(List<Pair<Pair<String, String>, String[]>> fields, Object o) throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        for (Pair<Pair<String, String>, String[]> field : fields) {
            Field f = o.getClass().getDeclaredField(field.first().first());
            f.setAccessible(true);
            if (field.first().second().equals("com.xebisco.yield.Vector2D")) {
                f.set(o, f.getType().getConstructor(double.class, double.class).newInstance(Double.parseDouble(field.second()[0]), Double.parseDouble(field.second()[1])));
            } else if (field.first().second().equals("com.xebisco.yield.Color")) {
                f.set(o, f.getType().getConstructor(double.class, double.class, double.class, double.class).newInstance(Double.parseDouble(field.second()[0]), Double.parseDouble(field.second()[1]), Double.parseDouble(field.second()[2]), Double.parseDouble(field.second()[3])));
            } else {
                EditableValuesType type = EditableValuesType.getPrimitiveType(f.getType());
                if (type == null) {
                    if (f.isEnumConstant()) {
                        f.set(o, f.getType().getMethod("valueOf", String.class).invoke(null, field.second()[0]));
                    }
                    continue;
                }
                switch (type) {
                    case STRING -> f.set(o, field.second()[0]);
                    case INT -> f.set(o, Integer.parseInt(field.second()[0]));
                    case LONG -> f.set(o, Long.parseLong(field.second()[0]));
                    case FLOAT -> f.set(o, Float.parseFloat(field.second()[0]));
                    case DOUBLE -> f.set(o, Double.parseDouble(field.second()[0]));
                }
            }
        }
    }
}
