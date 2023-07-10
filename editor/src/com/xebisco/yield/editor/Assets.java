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
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;

public class Assets {
    public static final Map<String, ImageIcon> images = new HashMap<>();
    public static List<Project> projects;

    public static void init() {
        new File(Utils.defaultDirectory() + "/.yield_editor").mkdir();
        File jre6 = new File(Utils.defaultDirectory() + "/.yield_editor", "jre6-rt.jar");
        if(!jre6.exists()) {
            try {
                jre6.createNewFile();
                Files.copy(Objects.requireNonNull(Assets.class.getResourceAsStream("/jre6-rt.jar")), jre6.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        File projectsFile = new File(Utils.defaultDirectory() + "/.yield_editor", "projects.ser");
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
    }
}
