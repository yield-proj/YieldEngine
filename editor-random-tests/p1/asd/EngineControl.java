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

package com.xebisco.yield.editor;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class EngineControl {
    private final Map<String, Class<?>> classes = new HashMap<>();

    public EngineControl(File jarFile) throws IOException {
        JarFile jar = new JarFile(jarFile);
        for (Enumeration<JarEntry> entries = jar.entries(); entries.hasMoreElements(); ) {
            JarEntry entry = entries.nextElement();
            String file = entry.getName();
            if (file.endsWith(".class")) {
                String classname = file.replace('/', '.').substring(0, file.length() - 6);
                try {
                    Class<?> c = Class.forName(classname);
                    classes.put(classname, c);
                } catch (ClassNotFoundException e) {
                    YieldEditor.throwException(e, null);
                }
            }
        }
        jar.close();
    }

    public Object newSceneInstance() {
        try {
            return classes.get("com.xebisco.yield.Scene").getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            YieldEditor.throwException(e, null);
        }
        return null;
    }

    public Map<String, Class<?>> getClasses() {
        return classes;
    }
}
