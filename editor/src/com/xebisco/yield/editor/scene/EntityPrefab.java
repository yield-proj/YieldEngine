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

package com.xebisco.yield.editor.scene;

import com.xebisco.yield.editor.EngineInstall;
import com.xebisco.yield.editor.Utils;
import com.xebisco.yield.editor.prop.ComponentProp;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class EntityPrefab implements Serializable {
    private final List<ComponentProp> components = new ArrayList<>();

    public EntityPrefab(EngineInstall install) {
        try(URLClassLoader cl = new URLClassLoader(new URL[] {new File(Utils.EDITOR_DIR + "/installs/" + install.install(), "yield-core.jar").toURI().toURL()})) {
            components.add(new ComponentProp(cl.loadClass("com.xebisco.yield.Transform2D"), false));
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private String name;
    private int zIndex;
    private List<String> tags = new ArrayList<>();
    private boolean visible;

    public List<ComponentProp> components() {
        return components;
    }

    public String name() {
        return name;
    }

    public EntityPrefab setName(String name) {
        this.name = name;
        return this;
    }

    public int zIndex() {
        return zIndex;
    }

    public EntityPrefab setzIndex(int zIndex) {
        this.zIndex = zIndex;
        return this;
    }

    public List<String> tags() {
        return tags;
    }

    public EntityPrefab setTags(List<String> tags) {
        this.tags = tags;
        return this;
    }

    public boolean visible() {
        return visible;
    }

    public EntityPrefab setVisible(boolean visible) {
        this.visible = visible;
        return this;
    }
}
