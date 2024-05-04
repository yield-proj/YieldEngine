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

import com.xebisco.yield.assets.compressing.AssetsCompressing;
import com.xebisco.yield.editor.app.editor.Editor;

import javax.tools.*;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

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
        if (!new File(path, "Scripts").exists()) {
            if (ignoreCheck) new File(path, "Scripts").mkdir();
            else return true;
        }

        if (!new File(path, "Assets").exists()) {
            if (ignoreCheck) new File(path, "Assets").mkdir();
            else return true;
        }

        if (!new File(path, "Assets/icon.png").exists()) {
            if (ignoreCheck)
                Files.copy(Objects.requireNonNull(Project.class.getResourceAsStream("/empty-project-template/icon.png")), new File(path, "Assets/icon.png").toPath(), StandardCopyOption.REPLACE_EXISTING);
            else return true;
        }

        if (!new File(path, "Assets/default-font.ttf").exists()) {
            if (ignoreCheck)
                Files.copy(Objects.requireNonNull(Project.class.getResourceAsStream("/empty-project-template/default-font.ttf")), new File(path, "Assets/default-font.ttf").toPath(), StandardCopyOption.REPLACE_EXISTING);
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

        Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(files);


        DiagnosticCollector<JavaFileObject> diagnosticCollector = new DiagnosticCollector<>();

        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnosticCollector, compileOptions(), null, compilationUnits);

        // Perform the compilation
        try {
            if (!task.call()) {
                StringBuilder dig = new StringBuilder();
                for (Diagnostic<? extends JavaFileObject> diagnostic : diagnosticCollector.getDiagnostics()) {
                    dig.append("<small>").append(diagnostic.getSource().getName()).append(":").append(diagnostic.getLineNumber()).append(":").append(diagnostic.getColumnNumber()).append("</small><br><pre>java: ").append(diagnostic.getMessage(null)).append("</pre><br>");
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

    public String createManifest() {
        new File(path, "Build/META-INF").mkdir();
        File manifestFile = new File(path, "Build/META-INF/MANIFEST.MF");

        try (PrintWriter writer = new PrintWriter(new FileWriter(manifestFile))) {
            writer.println("Manifest-Version: 1.0");
            File[] libs = new File(path, "Libraries").listFiles();
            if (libs != null && libs.length > 0) {
                writer.print("Class-Path: ");
                StringBuilder jarLibs = new StringBuilder();
                for (int i = 0; i < libs.length; i++) {
                    jarLibs.append("libs/").append(libs[i].getName());
                    if (i < libs.length - 1) jarLibs.append(' ');
                }
                writer.println(jarLibs);
            }

            writer.println("Main-Class: injected.Launcher");

            manifestFile.createNewFile();
        } catch (IOException e) {
            return e.getMessage();
        }

        return null;
    }

    public String buildToJar() {
        try (JarOutputStream jos = new JarOutputStream(new FileOutputStream(new File(path, "Output/" + name().replaceAll("\\s", "_") + ".jar")))) {
            addFilesToJar("", new File(path, "Output"), new File(path, "Build"), jos);
        } catch (IOException e) {
            return e.getMessage();
        }
        return null;
    }

    public String packAssets(String folder) {
        if (new File(path, folder).exists()) deleteDir(new File(path, folder));
        new File(path, folder).mkdir();

        try {
            File tempYieldIcon = File.createTempFile("yieldIcon", ".png");
            Files.copy(Objects.requireNonNull(Project.class.getResourceAsStream("/logo/logo.png")), tempYieldIcon.toPath(), StandardCopyOption.REPLACE_EXISTING);

            try (AssetsCompressing ac = new AssetsCompressing(new File(path, folder))) {
                Global.listf(new File(path, "Assets")).forEach(asset -> {
                    try {
                        ac.addFile(asset, asset.getPath().substring(new File(path, "Assets").getPath().length() + 1));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                ac.addFile(tempYieldIcon, "yieldIcon.png");
            }

            tempYieldIcon.delete();
        } catch (IOException e) {
            return e.getMessage();
        }
        return null;
    }

    public String copyLibraries() {
        if (new File(path, "Output/libs").exists()) deleteDir(new File(path, "Output/libs"));
        new File(path, "Output/libs").mkdir();

        for (File lib : Global.listf(new File(path, "Libraries"))) {
            try {
                Files.copy(lib.toPath(), new File(path, "Output/libs/" + lib.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                return e.getMessage();
            }
        }


        return null;
    }

    public void clearBuild() {
        deleteDir(new File(path, "Build"));
        new File(path, "Build").mkdir();
    }

    public void clearOutput() {
        deleteDir(new File(path, "Output"));
        new File(path, "Output").mkdir();
    }

    private static void deleteDir(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                deleteDir(f);
            }
        }
        file.delete();
    }

    private static void addFilesToJar(String jarPackage, File rootDir, File currentDir, JarOutputStream jos) throws IOException {
        if (!jarPackage.isEmpty()) jarPackage = jarPackage + '/';
        File[] files = currentDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    addFilesToJar(jarPackage, rootDir, file, jos);
                } else {
                    String entryName = file.getPath().substring(rootDir.getPath().length());
                    entryName = entryName.replace(File.separatorChar, '/');
                    JarEntry jarEntry = new JarEntry(jarPackage + entryName);
                    jos.putNextEntry(jarEntry);

                    try (FileInputStream fis = new FileInputStream(file)) {
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = fis.read(buffer)) != -1) {
                            jos.write(buffer, 0, bytesRead);
                        }
                    }

                    jos.closeEntry();
                }
            }
        }
    }

    private List<String> compileOptions() {
        List<String> options = new ArrayList<>();

        options.add("-d");
        options.add(path.getPath() + "/Build");

        File[] libs = new File(path, "Libraries").listFiles();
        if (libs != null) {
            options.add("-classpath");
            StringBuilder jarLibs = new StringBuilder();
            for (int i = 0; i < libs.length; i++) {
                jarLibs.append(libs[i].getAbsolutePath());
                if (i < libs.length - 1) jarLibs.append(File.pathSeparator);
            }
            options.add(jarLibs.toString());
        }
        return options;
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
