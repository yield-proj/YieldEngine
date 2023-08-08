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

import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;

public class Assets {
    public static final Map<String, ImageIcon> images = new HashMap<>();
    public static List<Project> projects;
    public static List<EngineInstall> engineInstalls;

    public static void init() {
        Utils.EDITOR_DIR.mkdir();
        File jre = new File(Utils.EDITOR_DIR, "lang-rt.jar");
        if(!jre.exists()) {
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
    }
}
