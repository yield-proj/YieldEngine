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

import javax.tools.*;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;

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

        if (!new File(path, "icon.png").exists()) {
            if (ignoreCheck)
                Files.copy(Objects.requireNonNull(Project.class.getResourceAsStream("/empty-project-template/icon.png")), new File(path, "icon.png").toPath(), StandardCopyOption.REPLACE_EXISTING);
            else return true;
        }

        if (!new File(path, "default-font.ttf").exists()) {
            if (ignoreCheck)
                Files.copy(Objects.requireNonNull(Project.class.getResourceAsStream("/empty-project-template/default-font.ttf")), new File(path, "default-font.ttf").toPath(), StandardCopyOption.REPLACE_EXISTING);
            else return true;
        }

        if (!new File(path, "Scripts").exists()) {
            if (ignoreCheck) new File(path, "Scripts").mkdir();
            else return true;
        }

        if (!new File(path, "Libraries").exists()) {
            if (ignoreCheck) new File(path, "Libraries").mkdir();
            else return true;
        }

        if (!new File(path, "Libraries/yield-core.jar").exists()) {
            if (ignoreCheck)
                Files.copy(Objects.requireNonNull(Project.class.getResourceAsStream("/engine/core.jar")), new File(path, "Libraries/yield-core.jar").toPath(), StandardCopyOption.REPLACE_EXISTING);
            else return true;
        }

        for (String s : Editor.STD_PROJECT_VALUES.keySet()) {
            if (!propsValues.containsKey(s)) {
                if (ignoreCheck) propsValues.put(s, Editor.STD_PROJECT_VALUES.get(s));
                else return true;
            } else {
                for (String s1 : Editor.STD_PROJECT_VALUES.get(s).keySet()) {
                    if (!propsValues.get(s).containsKey(s1)) {
                        if (ignoreCheck) propsValues.get(s).put(s1, Editor.STD_PROJECT_VALUES.get(s).get(s1));
                        else return true;
                    }
                }
            }
        }
        return false;
    }

    public void saveProjectFile() {
        setLastModified(new Date());

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

    public String compileScripts() {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            return "Java compiler not found. Make sure you're using a JDK.";
        }

        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);

        List<File> files = Global.listf(new File(path, "Scripts"));

        if (files.isEmpty()) return null;


        Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(files);

        DiagnosticCollector<JavaFileObject> diagnosticCollector = new DiagnosticCollector<>();

        //TODO ADD JAR FILES(LIBRARIES AND ENGINE)
        Iterable<String> options = Arrays.asList("-d", path.getPath());

        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnosticCollector, options, null, compilationUnits);

        // Perform the compilation
        try {
            if (!task.call()) {
                StringBuilder dig = new StringBuilder();
                for (Diagnostic<? extends JavaFileObject> diagnostic : diagnosticCollector.getDiagnostics()) {
                    dig
                            .append("<small>")
                            .append(diagnostic.getSource().getName())
                            .append(":")
                            .append(diagnostic.getLineNumber())
                            .append(":")
                            .append(diagnostic.getColumnNumber())
                            .append("</small><br><pre>java: ")
                            .append(diagnostic.getMessage(null))
                            .append("</pre><br>");
                }
                return dig.toString();
            }
        } catch (Exception e) {
            return e.getMessage();
        }

        // Close the file manager
        try {
            fileManager.close();
        } catch (Exception e) {
            return e.getMessage();
        }
        return null;
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
