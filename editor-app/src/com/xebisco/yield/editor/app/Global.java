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

package com.xebisco.yield.editor.app;

import com.xebisco.yield.editor.app.editor.ScenePanel;

import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Objects;
import java.util.Properties;

public class Global {
    public static Properties appProps = new Properties();
    public static final String VERSION = "dev1";
    public static final URL yieldEngineJar = Objects.requireNonNull(ScenePanel.class.getResource("/engine/core.jar"));
    public static final ClassLoader yieldEngineClassLoader = new URLClassLoader(new URL[]{yieldEngineJar}, null);
    public static final Class<? extends Annotation> VISIBLE_ANNOTATION, HIDE_ANNOTATION, SIZE_ANNOTATION;

    static {
        try {
            //noinspection unchecked
            VISIBLE_ANNOTATION = (Class<? extends Annotation>) yieldEngineClassLoader.loadClass("com.xebisco.yield.editor.annotations.Visible");
            //noinspection unchecked
            HIDE_ANNOTATION = (Class<? extends Annotation>) yieldEngineClassLoader.loadClass("com.xebisco.yield.editor.annotations.HideComponent");
            //noinspection unchecked
            SIZE_ANNOTATION = (Class<? extends Annotation>) yieldEngineClassLoader.loadClass("com.xebisco.yield.editor.annotations.AffectsEditorEntitySize");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
