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

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SceneObject implements Serializable {
    private int x, y, width, height, id = nextID();

    private static int sID = 0;

    private static int nextID() {
        return sID++;
    }
    private List<File> sceneObjects = new ArrayList<>();
    private final EntityPrefab entityPrefab;
    private SceneObject parent;

    public SceneObject(EntityPrefab entityPrefab, SceneObject parent) {
        this.parent = parent;
        this.entityPrefab = entityPrefab;
    }

    public int x() {
        return x;
    }

    public SceneObject setX(int x) {
        this.x = x;
        return this;
    }

    public int y() {
        return y;
    }

    public SceneObject setY(int y) {
        this.y = y;
        return this;
    }

    public int width() {
        return width;
    }

    public SceneObject setWidth(int width) {
        this.width = width;
        return this;
    }

    public int height() {
        return height;
    }

    public SceneObject setHeight(int height) {
        this.height = height;
        return this;
    }

    public List<File> sceneObjects() {
        return sceneObjects;
    }

    public SceneObject setSceneObjects(List<File> sceneObjects) {
        this.sceneObjects = sceneObjects;
        return this;
    }

    public EntityPrefab entityPrefab() {
        return entityPrefab;
    }

    public SceneObject parent() {
        return parent;
    }

    public SceneObject setParent(SceneObject parent) {
        this.parent = parent;
        return this;
    }

    public int id() {
        return id;
    }

    public SceneObject setId(int id) {
        this.id = id;
        return this;
    }
}
