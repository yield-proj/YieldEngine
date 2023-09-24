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

import com.xebisco.yield.editor.scene.SceneObject;

import java.awt.*;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EditorScene implements Serializable {
    private String name;
    private Color backgroundColor = Color.BLACK;
    private java.util.List<File> sceneObjects = new ArrayList<>();

    public String name() {
        return name;
    }

    public EditorScene setName(String name) {
        this.name = name;
        return this;
    }

    public Color backgroundColor() {
        return backgroundColor;
    }

    public EditorScene setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public List<File> sceneObjects() {
        return sceneObjects;
    }

    public EditorScene setSceneObjects(List<File> sceneObjects) {
        this.sceneObjects = sceneObjects;
        return this;
    }
}
