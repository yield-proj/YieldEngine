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

package com.xebisco.yieldengine.editor.runtime.pack;

import com.xebisco.yieldengine.editor.annotations.Config;
import com.xebisco.yieldengine.editor.annotations.IntColor;
import com.xebisco.yieldengine.editor.annotations.Visible;

import java.awt.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Config
public class EditorScene implements Serializable {
    @Serial
    private static final long serialVersionUID = -642348471217520688L;
    @Visible
    private String name = "Empty Scene";
    @Visible
    private boolean addToBuild = true;
    @Visible
    @IntColor
    private int backgroundColor = new Color(45, 46, 49).getRGB();

    private List<EditorEntity> entities = new ArrayList<>();

    public String name() {
        return name;
    }

    public EditorScene setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String toString() {
        return name;
    }

    public int backgroundColor() {
        return backgroundColor;
    }

    public EditorScene setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public boolean addToBuild() {
        return addToBuild;
    }

    public EditorScene setAddToBuild(boolean addToBuild) {
        this.addToBuild = addToBuild;
        return this;
    }


    public List<EditorEntity> entities() {
        return entities;
    }

    public EditorScene setEntities(List<EditorEntity> entities) {
        this.entities = entities;
        return this;
    }
}