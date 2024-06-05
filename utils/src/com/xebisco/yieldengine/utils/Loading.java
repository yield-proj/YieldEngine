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

package com.xebisco.yieldengine.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Loading {

    public final static Pattern SIZEP = Pattern.compile("^(.*)\\s*,\\s*([0-9]+)$");

    public static void applyPropsToObject(List<Pair<Pair<String, String>, String[]>> fields, Object o, Object... extras) throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        for (Pair<Pair<String, String>, String[]> field : fields) {
            Field f = null;
            Class<?> c = o.getClass();
            do {
                try {
                    f = c.getDeclaredField(field.first().first());
                } catch (NoSuchFieldException ignore) {
                }
                c = c.getSuperclass();
                if (c == null) break;
            } while (f == null);
            if (f == null) throw new NoSuchFieldException(field.first().first());
            f.setAccessible(true);
            switch (field.first().second()) {
                case "com.xebisco.yieldengine.AbstractTexture" -> {
                    try {
                        f.set(o, o.getClass().getClassLoader().loadClass("com.xebisco.yieldengine.texture.Texture").getConstructor(
                                        String.class,
                                        o.getClass().getClassLoader().loadClass("com.xebisco.yieldengine.texture.TextureFilter"),
                                        o.getClass().getClassLoader().loadClass("com.xebisco.yieldengine.manager.TextureManager"),
                                        o.getClass().getClassLoader().loadClass("com.xebisco.yieldengine.manager.FileIOManager"))
                                .newInstance(field.second()[0], o.getClass().getClassLoader().loadClass("com.xebisco.yieldengine.texture.TextureFilter").getEnumConstants()[0], extras[0], extras[1])
                        );
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
                case "com.xebisco.yieldengine.font.Font" -> {
                    try {
                        System.out.println(Arrays.toString(field.second()));
                        Matcher m = SIZEP.matcher(field.second()[0]);
                        String path = "default-font.ttf";
                        double size = 12;
                        if (m.find()) {
                            if (!m.group(1).isEmpty() && !m.group(1).equals("null"))
                                path = m.group(1);
                            size = Double.parseDouble(m.group(2));
                        } else {
                            if (!field.second()[0].isEmpty() && !field.second()[0].equals("null"))
                                path = field.second()[0];
                        }
                        f.set(o, o.getClass().getClassLoader().loadClass("com.xebisco.yieldengine.font.Font").getConstructor(
                                        String.class,
                                        double.class,
                                        o.getClass().getClassLoader().loadClass("com.xebisco.yieldengine.manager.FontManager"),
                                        o.getClass().getClassLoader().loadClass("com.xebisco.yieldengine.manager.FileIOManager"))
                                .newInstance(path, size, extras[0], extras[1])
                        );
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
                case "com.xebisco.yieldengine.Vector2D" ->
                        f.set(o, f.getType().getConstructor(double.class, double.class).newInstance(Double.parseDouble(field.second()[0]), Double.parseDouble(field.second()[1])));
                case "com.xebisco.yieldengine.Color" ->
                        f.set(o, f.getType().getConstructor(double.class, double.class, double.class, double.class).newInstance(Double.parseDouble(field.second()[0]), Double.parseDouble(field.second()[1]), Double.parseDouble(field.second()[2]), Double.parseDouble(field.second()[3])));
                default -> {
                    EditableValuesType type = EditableValuesType.getPrimitiveType(f.getType());
                    if (type == null) {
                        if (f.getType().isEnum()) {
                            f.set(o, f.getType().getMethod("valueOf", String.class).invoke(null, field.second()[0]));
                        }
                        continue;
                    }
                    switch (type) {
                        case STRING -> f.set(o, field.second()[0]);
                        case BOOLEAN -> f.set(o, Boolean.parseBoolean(field.second()[0]));
                        case INT -> f.set(o, Integer.parseInt(field.second()[0]));
                        case LONG -> f.set(o, Long.parseLong(field.second()[0]));
                        case FLOAT -> f.set(o, Float.parseFloat(field.second()[0]));
                        case DOUBLE -> f.set(o, Double.parseDouble(field.second()[0]));
                    }
                }
            }
        }
    }
}
