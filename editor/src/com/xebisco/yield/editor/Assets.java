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

import com.xebisco.yield.editor.code.CompilationException;
import com.xebisco.yield.editor.prop.*;

import javax.swing.*;
import javax.tools.ToolProvider;
import java.awt.event.ActionEvent;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

public class Assets {
    public static final Map<String, ImageIcon> images = new HashMap<>();
    public static List<Project> projects;
    public static List<EngineInstall> engineInstalls;
    public static List<Editor> openedEditors = new ArrayList<>();
    public static Map<String, Prop[]> editorSettings;
    public static Project lastOpenedProject;
    public static Properties language = new Properties();

    public static void init() {
        Utils.EDITOR_DIR.mkdir();
        new File(Utils.EDITOR_DIR, "out").mkdir();
        File jre = new File(Utils.EDITOR_DIR, "lang-rt.jar");
        if (!jre.exists()) {
            try {
                InputStream is = Objects.requireNonNull(Assets.class.getResourceAsStream("/lang-rt.jar"));
                jre.createNewFile();
                Files.copy(is, jre.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (NullPointerException e) {
                Utils.error(Entry.splashDialog, new IllegalStateException("Missing custom java runtime jar (lang-rt.jar) in resources"));
                System.exit(1);
            }
        }
        File projectsFile = new File(Utils.EDITOR_DIR, "projects.ser");
        if (!projectsFile.exists()) {
            try {
                projectsFile.createNewFile();
            } catch (IOException e) {
                Utils.error(null, e);
                throw new RuntimeException(e);
            }
            projects = new ArrayList<>();
        } else {
            try (ObjectInputStream oi = new ObjectInputStream(new FileInputStream(projectsFile))) {
                //noinspection unchecked
                projects = (List<Project>) oi.readObject();
            } catch (EOFException e) {
                projects = new ArrayList<>();
            } catch (IOException | ClassNotFoundException e) {
                Utils.error(null, e);
                throw new RuntimeException(e);
            }
        }

        File installs = new File(Utils.EDITOR_DIR, "installs.ser");

        if (!installs.exists()) {
            try {
                installs.createNewFile();
            } catch (IOException e) {
                Utils.error(null, e);
                throw new RuntimeException(e);
            }
            engineInstalls = new ArrayList<>();
        } else {
            try (ObjectInputStream oi = new ObjectInputStream(new FileInputStream(installs))) {
                //noinspection unchecked
                engineInstalls = (List<EngineInstall>) oi.readObject();
            } catch (EOFException e) {
                engineInstalls = new ArrayList<>();
            } catch (IOException | ClassNotFoundException e) {
                Utils.error(null, e);
                throw new RuntimeException(e);
            }
        }
        language.clear();
        try {
            language.load(Assets.class.getResourceAsStream("/lang/en.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        loadSettings();
        if ((boolean) Props.get(editorSettings.get("behavior"), "reopen_project_on_startup").getValue()) {
            File lastOpenedProjectFile = new File(Utils.EDITOR_DIR, "lastOpenedProject.ser");

            if (lastOpenedProjectFile.exists()) {
                try (ObjectInputStream oi = new ObjectInputStream(new FileInputStream(lastOpenedProjectFile))) {
                    lastOpenedProject = (Project) oi.readObject();
                } catch (IOException | ClassNotFoundException e) {
                    Utils.error(null, e);
                    throw new RuntimeException(e);
                }
            }
        }
        File f = new File(Utils.EDITOR_DIR, "AppEntry.java");
        try {
            f.createNewFile();
            Files.copy(Objects.requireNonNull(Assets.class.getResourceAsStream("/AppEntry.java")), f.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        f = new File(Utils.EDITOR_DIR, "editor_overhead.jar");
        try {
            f.createNewFile();
            Files.copy(Objects.requireNonNull(Assets.class.getResourceAsStream("/editor_overhead.jar")), f.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void loadSettings() {
        File settings = new File(Utils.EDITOR_DIR, "settings.ser");

        if (settings.exists()) {
            try (ObjectInputStream oi = new ObjectInputStream(new FileInputStream(settings))) {
                //noinspection unchecked
                editorSettings = (Map<String, Prop[]>) oi.readObject();
            } catch (ClassNotFoundException | InvalidClassException e) {
                Entry.launchError(e);
            } catch (IOException e) {
                Utils.error(null, e);
                throw new RuntimeException(e);
            }
        } else editorSettings = new HashMap<>();
        try {
            settings.createNewFile();
        } catch (IOException e) {
            Utils.error(null, e);
            throw new RuntimeException(e);
        }
        putSettings(editorSettings, "behavior", new Prop[]{
                new BooleanProp("confirm_close_before_exiting_the_editor", true),
                new TextShowProp("project", true),
                new BooleanProp("reopen_project_on_startup", false),
                new PathProp("default_directory_for_new_projects", System.getProperty("user.home"), JFileChooser.DIRECTORIES_ONLY)
        });
        putSettings(editorSettings, "editor", new Prop[]{
                new OptionsProp("language", new Serializable[]{"en"}),
                new TextShowProp("fix_editor", true),
                new ButtonProp("delete_editor_settings", new AbstractAction(Assets.language.getProperty("delete")) {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        settings.delete();
                        loadSettings();
                        Projects.saveSettings();
                    }
                })
        });
        putSettings(editorSettings, "code_editor", new Prop[]{
                new FontProp("code_editor_font")
        });
        putSettings(editorSettings, "java_options", new Prop[]{
                new PathProp("preferred_jre", "", JFileChooser.DIRECTORIES_ONLY)
        });
        Projects.saveSettings();


        language.clear();
        try {
            language.load(Assets.class.getResourceAsStream("/lang/" + Objects.requireNonNull(Props.get(editorSettings.get("editor"), "language")).getValue() + ".properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static String getJRE() {
        String v = (String) Objects.requireNonNull(Props.get(Assets.editorSettings.get("java_options"), "preferred_jre")).getValue();
        if (v.equals("")) {
            return System.getProperty("java.home");
        } else {
            return v;
        }
    }

    private static void putSettings(Map<String, Prop[]> map, String s, Prop[] props) {
        if (map.containsKey(s)) {
            Prop[] mprops = map.get(s);
            List<Prop> nprops = new ArrayList<>();
            for (Prop p : props) {
                Prop p1;
                if ((p1 = Props.get(mprops, p.getName())) != null) {
                    nprops.add(p1);
                } else nprops.add(p);
            }
        } else {
            map.put(s, props);
        }
    }
}
