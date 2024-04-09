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

package com.xebisco.yield.editor.app.editor;

import com.xebisco.yield.editor.annotations.Visible;
import com.xebisco.yield.editor.app.Global;
import com.xebisco.yield.utils.Pair;

import java.io.Serial;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class EditorComponent implements Serializable {
    @Serial
    private static final long serialVersionUID = -9027305243114159863L;
    private List<Pair<Pair<String, String>, String[]>> fields = new ArrayList<>();
    private final String className;

    private boolean canRemove = true;

    public EditorComponent(Class<?> clazz) {
        this.className = clazz.getName();
        Object o;
        try {
            o = clazz.getConstructor().newInstance();
        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        Class<?> c = clazz;
        do {
            addFields(c, o);
            c = c.getSuperclass();
        } while (!c.isPrimitive() && c.getSuperclass() != null);
    }

    private void addFields(Class<?> clazz, Object o) {
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            try {
                String[] value;
                if (field.getType().getName().equals("com.xebisco.yield.Vector2D")) {
                    Object obj = field.get(o);
                    value = new String[]{String.valueOf(obj.getClass().getMethod("x").invoke(obj)), String.valueOf(obj.getClass().getMethod("y").invoke(obj))};
                } else if (field.getType().getName().equals("com.xebisco.yield.Color")) {
                    Object obj = field.get(o);
                    value = new String[]{String.valueOf(obj.getClass().getMethod("red").invoke(obj)), String.valueOf(obj.getClass().getMethod("green").invoke(obj)), String.valueOf(obj.getClass().getMethod("blue").invoke(obj)), String.valueOf(obj.getClass().getMethod("alpha").invoke(obj))};
                } else if (Global.yieldEngineClassLoader.loadClass("com.xebisco.yield.FileInput").isAssignableFrom(field.getType())) {
                    Object obj = null;
                    if (o != null) obj = field.get(o);
                    String path = null;
                    if (obj != null) path = (String) obj.getClass().getMethod("path").invoke(obj);
                    try {
                        String[] extensions = (String[]) clazz.getMethod("extensions").invoke(null);
                        StringBuilder extensionsS = new StringBuilder();
                        for (String ext : extensions) {
                            extensionsS.append(ext).append(";");
                        }
                        if (extensionsS.toString().endsWith(";"))
                            extensionsS = new StringBuilder(extensionsS.substring(0, extensionsS.length() - 1));
                        value = new String[]{path, extensionsS.toString()};
                    } catch (NoSuchMethodException e) {
                        value = new String[]{path};
                    }
                } else value = new String[]{String.valueOf(field.get(o))};
                if (field.isAnnotationPresent(Global.VISIBLE_ANNOTATION))
                    fields.add(new Pair<>(new Pair<>(field.getName(), field.getType().getName()), value));
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException |
                     ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public List<Pair<Pair<String, String>, String[]>> fields() {
        return fields;
    }

    public EditorComponent sameAs(List<Pair<Pair<String, String>, String[]>> fields, EditorComponent editorComponent) {
        for (Pair<Pair<String, String>, String[]> field : fields) {
            editorComponent.fields.forEach(p -> {
                if (p.first().first().equals(field.first().first())) {
                    System.arraycopy(field.second(), 0, p.second(), 0, p.second().length);
                }
            });
        }
        return editorComponent;
    }

    public EditorComponent sameAs() {
        try {
            return sameAs(fields, new EditorComponent(Global.yieldEngineClassLoader.loadClass(className)));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public EditorComponent setFields(List<Pair<Pair<String, String>, String[]>> fields) {
        this.fields = fields;
        return this;
    }

    public boolean canRemove() {
        return canRemove;
    }

    public EditorComponent setCanRemove(boolean canRemove) {
        this.canRemove = canRemove;
        return this;
    }

    public String className() {
        return className;
    }
}