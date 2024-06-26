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

package com.xebisco.yieldengine.editor.app;

import com.xebisco.yieldengine.assets.compressing.AssetsCompressing;
import com.xebisco.yieldengine.editor.annotations.Config;
import com.xebisco.yieldengine.editor.annotations.Visible;
import com.xebisco.yieldengine.editor.runtime.pack.EditorProject;
import com.xebisco.yieldengine.editor.runtime.pack.EditorScene;

import javax.tools.*;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

import static com.xebisco.yieldengine.editor.app.Global.deleteDir;

public class Project implements Serializable {
    @Serial
    private static final long serialVersionUID = 4908578292511938243L;
    private Date lastModified = new Date();
    private final String ID = UUID.randomUUID().toString();
    private transient File path;
    private EditorProject editorProject = new EditorProject();
    private String packageName;
    private final ArrayList<EditorScene> scenes = new ArrayList<>();

    private HashMap<String, HashMap<String, Serializable>> projectSettings;

    private Project() {
    }

    public boolean updatePropsToLatest(boolean ignoreCheck) throws IOException {
        if (!scriptsDirectory().exists()) {
            if (ignoreCheck) scriptsDirectory().mkdir();
            else return true;
        }

        if (!scriptsPackage().exists()) {
            if (ignoreCheck) scriptsPackage().mkdirs();
            else return true;
        }

        if (!assetsDirectory().exists()) {
            if (ignoreCheck) assetsDirectory().mkdir();
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

        if (!librariesDirectory().exists()) {
            if (ignoreCheck) librariesDirectory().mkdir();
            else return true;
        }

        if (!new File(path, "Libraries/yield-core.jar").exists()) {
            if (ignoreCheck)
                Files.copy(Objects.requireNonNull(Project.class.getResourceAsStream("/engine/yield-core.jar")), new File(path, "Libraries/yield-core.jar").toPath(), StandardCopyOption.REPLACE_EXISTING);
            else return true;
        }
        if (!new File(path, "Libraries/yield-editor-runtime.jar").exists()) {
            if (ignoreCheck)
                Files.copy(Objects.requireNonNull(Project.class.getResourceAsStream("/engine/yield-editor-runtime.jar")), new File(path, "Libraries/yield-editor-runtime.jar").toPath(), StandardCopyOption.REPLACE_EXISTING);
            else return true;
        }
        if (!new File(path, "Libraries/yield-openalimpl.jar").exists()) {
            if (ignoreCheck)
                Files.copy(Objects.requireNonNull(Project.class.getResourceAsStream("/engine/yield-openalimpl.jar")), new File(path, "Libraries/yield-openalimpl.jar").toPath(), StandardCopyOption.REPLACE_EXISTING);
            else return true;
        }

        if (!new File(path, "Libraries/yield-openglimpl.jar").exists()) {
            if (ignoreCheck)
                Files.copy(Objects.requireNonNull(Project.class.getResourceAsStream("/engine/yield-openglimpl.jar")), new File(path, "Libraries/yield-openglimpl.jar").toPath(), StandardCopyOption.REPLACE_EXISTING);
            else return true;
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

    public static Project createProject(String name, String packageName, File dir) throws IOException, URISyntaxException {
        dir.mkdir();

        Project p = new Project();
        p.setPath(dir).setPackageName(packageName);
        p.updatePropsToLatest(true);
        p.editorProject().setName(name);

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

        List<File> files = Global.listf(scriptsDirectory());

        if (files.isEmpty()) return null;

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
            File[] libs = librariesDirectory().listFiles();
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
        try (JarOutputStream jos = new JarOutputStream(new FileOutputStream(new File(path, "Output/" + editorProject().name().replaceAll("\\s", "_") + ".jar")))) {
            addFilesToJar("", new File(path, "Output"), buildDirectory(), jos);
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

    public String packScenes(String folder) {
        if (new File(path, folder).exists()) deleteDir(new File(path, folder));
        new File(path, folder).mkdir();

        try (AssetsCompressing ac = new AssetsCompressing(new File(path, folder))) {
            for (EditorScene scene : scenes) {
                if(!scene.addToBuild()) continue;
                File tempSceneFile = File.createTempFile("yieldbuild", "scene");
                try (ObjectOutputStream oo = new ObjectOutputStream(new FileOutputStream(tempSceneFile))) {
                    oo.writeObject(scene);
                } catch (IOException e) {
                    return e.getMessage();
                }
                ac.addFile(tempSceneFile, scene.name());
                tempSceneFile.delete();
            }
        } catch (IOException e) {
            return e.getMessage();
        }

        return null;
    }

    public String copyLibraries() {
        if (new File(path, "Output/libs").exists()) deleteDir(new File(path, "Output/libs"));
        new File(path, "Output/libs").mkdir();

        for (File lib : Global.listf(librariesDirectory())) {
            try {
                Files.copy(lib.toPath(), new File(path, "Output/libs/" + lib.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                return e.getMessage();
            }
        }


        return null;
    }

    public void clearBuild() {
        deleteDir(buildDirectory());
        buildDirectory().mkdir();
    }

    public void clearOutput() {
        deleteDir(new File(path, "Output"));
        new File(path, "Output").mkdir();
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
        options.add(buildDirectory().getAbsolutePath());

        File[] libs = librariesDirectory().listFiles();
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

    public String createConfigFile(HashMap<String, HashMap<String, Serializable>> contents, String fileName, String folder) {
        if (new File(path, folder).exists()) deleteDir(new File(path, folder));
        new File(path, folder).mkdir();

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(path, folder + "/" + fileName)))) {
            oos.writeObject(contents);
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
        return null;
    }

    public File assetsDirectory() {
        return new File(path, "Assets");
    }

    public File scriptsDirectory() {
        return new File(path, "Scripts");
    }

    public File librariesDirectory() {
        return new File(path, "Libraries");
    }

    public File buildDirectory() {
        return new File(path, "Build");
    }

    public File scriptsPackage() {
        return new File(scriptsDirectory(), packageName.replace(".", File.separator));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return Objects.equals(ID, project.ID);
    }

    public List<EditorScene> scenes() {
        return scenes;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID);
    }

    public HashMap<String, HashMap<String, Serializable>> projectSettings() {
        return projectSettings;
    }

    public Project setProjectSettings(HashMap<String, HashMap<String, Serializable>> projectSettings) {
        this.projectSettings = projectSettings;
        return this;
    }

    public EditorProject editorProject() {
        return editorProject;
    }

    public Project setEditorProject(EditorProject editorProject) {
        this.editorProject = editorProject;
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

    public String packageName() {
        return packageName;
    }

    public Project setPackageName(String packageName) {
        this.packageName = packageName;
        return this;
    }
}
