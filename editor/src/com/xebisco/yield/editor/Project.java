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

import com.xebisco.yield.editor.scene.EditorScene;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Project extends PreferredInstall implements Serializable {
    private String name;
    private final Map<String, EditorScene> scenes = new HashMap<>();
    private final File projectLocation;
    private final Date createdDate = new Date();

    public Project(String name, File projectLocation) {
        this.name = name;
        this.projectLocation = projectLocation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public File getProjectLocation() {
        return projectLocation;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public Map<String, EditorScene> getScenes() {
        return scenes;
    }
}
