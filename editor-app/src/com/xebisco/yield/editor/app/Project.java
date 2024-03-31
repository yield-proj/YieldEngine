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

import java.awt.*;
import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class Project implements Serializable {
    private HashMap<String, HashMap<String, Serializable>> propsValues;

    private String name;
    private Date lastModified = new Date();
    private final String ID = UUID.randomUUID().toString();
    private transient File path;

    private Project() {
    }

    public static Project createProject(String name, Dimension size, String ec, File dir) throws IOException {
        int[][] map = new int[size.width][size.height];
        for (int x = 0; x < map.length; x++)
            for (int y = 0; y < map[0].length; y++) {
                map[x][y] = -1;
            }

        dir.mkdir();

        Project p = new Project().setName(name);

        File projectFile = new File(dir, "editor_project.ser");
        projectFile.createNewFile();
        try (ObjectOutputStream oo = new ObjectOutputStream(new FileOutputStream(projectFile))) {
            oo.writeObject(p);
        }

        return p;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return Objects.equals(ID, project.ID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID);
    }

    public String name() {
        return name;
    }

    public Project setName(String name) {
        this.name = name;
        return this;
    }

    public Date lastModified() {
        return lastModified;
    }

    public Project setLastModified(Date lastModified) {
        this.lastModified = lastModified;
        return this;
    }

    public String ID() {
        return ID;
    }

    public File path() {
        return path;
    }

    public Project setPath(File path) {
        this.path = path;
        return this;
    }
}
