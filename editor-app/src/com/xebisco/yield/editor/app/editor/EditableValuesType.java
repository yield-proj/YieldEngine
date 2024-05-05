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

import com.xebisco.yield.editor.app.Global;

public enum EditableValuesType {
    STRING, INT, FLOAT, LONG, DOUBLE, BOOLEAN, POSITION, ARRAY, COLOR, FILE, TEXTURE, FONT;

    public static EditableValuesType getType(Class<?> c, Editor editor) {
        if(c.equals(String.class)) return STRING;
        if(c.equals(Integer.class) || c.equals(int.class)) return INT;
        if(c.equals(Float.class) || c.equals(float.class)) return FLOAT;
        if(c.equals(Long.class) || c.equals(long.class)) return LONG;
        if(c.equals(Double.class) || c.equals(double.class)) return DOUBLE;
        if(c.equals(Boolean.class) || c.equals(boolean.class)) return BOOLEAN;
        if(c.getName().equals("com.xebisco.yield.Vector2D")) return POSITION;
        if(c.getName().equals("com.xebisco.yield.Color")) return COLOR;
        try {
            if(editor.yieldEngineClassLoader.loadClass("com.xebisco.yield.AbstractTexture").isAssignableFrom(c)) return TEXTURE;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            if(editor.yieldEngineClassLoader.loadClass("com.xebisco.yield.font.Font").isAssignableFrom(c)) return FONT;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            if(editor.yieldEngineClassLoader.loadClass("com.xebisco.yield.FileInput").isAssignableFrom(c)) return FILE;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        if(c.isArray()) return ARRAY;
        return null;
    }
}