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

import com.xebisco.yield.editor.Pair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EntityPrefab implements Serializable {
    private double x, y, sx, sy, rz;
    private final List<Pair<String, Map<String, Serializable>>> components = new ArrayList<>();

    private String name;
    private int zIndex;
    private List<String> tags = new ArrayList<>();
    private boolean visible;

    public double x() {
        return x;
    }

    public EntityPrefab setX(double x) {
        this.x = x;
        return this;
    }

    public double y() {
        return y;
    }

    public EntityPrefab setY(double y) {
        this.y = y;
        return this;
    }

    public List<Pair<String, Map<String, Serializable>>> components() {
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

    public double sx() {
        return sx;
    }

    public EntityPrefab setSx(double sx) {
        this.sx = sx;
        return this;
    }

    public double sy() {
        return sy;
    }

    public EntityPrefab setSy(double sy) {
        this.sy = sy;
        return this;
    }

    public double rz() {
        return rz;
    }

    public EntityPrefab setRz(double rz) {
        this.rz = rz;
        return this;
    }
}
