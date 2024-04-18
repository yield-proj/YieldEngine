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

import com.xebisco.yield.editor.app.editor.Editor;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class Project implements Serializable {
    @Serial
    private static final long serialVersionUID = 4908578292511938243L;
    private final HashMap<String, HashMap<String, Serializable>> propsValues = new HashMap<>();
    private Date lastModified = new Date();
    private final String ID = UUID.randomUUID().toString();
    private transient File path;

    private Project() {
    }

    public boolean updatePropsToLatest(boolean ignoreCheck) throws IOException {

        if(!new File(path, "icon.png").exists()) {
            if (ignoreCheck)
                Files.copy(Objects.requireNonNull(Project.class.getResourceAsStream("/empty-project-template/logo.png")), new File(path, "icon.png").toPath(), StandardCopyOption.REPLACE_EXISTING);
            else return true;
        }

        if(!new File(path, "default-font.ttf").exists()) {
            if (ignoreCheck)
                Files.copy(Objects.requireNonNull(Project.class.getResourceAsStream("/empty-project-template/default-font.ttf")), new File(path, "default-font.ttf").toPath(), StandardCopyOption.REPLACE_EXISTING);
            else return true;
        }

        for (String s : Editor.STD_PROJECT_VALUES.keySet()) {
            if (!propsValues.containsKey(s)) {
                if (ignoreCheck)
                    propsValues.put(s, Editor.STD_PROJECT_VALUES.get(s));
                else return true;
            } else {
                for (String s1 : Editor.STD_PROJECT_VALUES.get(s).keySet()) {
                    if (!propsValues.get(s).containsKey(s1)) {
                        if (ignoreCheck)
                            propsValues.get(s).put(s1, Editor.STD_PROJECT_VALUES.get(s).get(s1));
                        else return true;
                    }
                }
            }
        }
        return false;
    }

    public void saveProjectFile() {
        try (ObjectOutputStream oo = new ObjectOutputStream(new FileOutputStream(new File(path, "editor_project.ser")))) {
            oo.writeObject(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Project createProject(String name, File dir) throws IOException, URISyntaxException {
        dir.mkdir();

        Project p = new Project();
        p.updatePropsToLatest(true);
        p.setName(name);

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
        return (String) propsValues.get("p_t_general").get("p_t_general_projectName");
    }

    public Project setName(String name) {
        propsValues.get("p_t_general").replace("p_t_general_projectName", name);
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
