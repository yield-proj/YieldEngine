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

import com.formdev.flatlaf.IntelliJTheme;
import com.formdev.flatlaf.util.SystemInfo;
import com.xebisco.yieldengine.uiutils.Srd;

import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.util.*;

import static com.xebisco.yieldengine.editor.app.ProjectEditor.createWorkspace;

public class Entry {

    public static final File saveDir = new File(System.getProperty("user.home"), ".yeditor");

    public static void main(String[] args) throws IOException {
        System.setProperty("sun.java2d.opengl", "True");
        Locale.setDefault(Locale.US);

        //FlatDarkLaf.setup();
        IntelliJTheme.setup(Entry.class.getResourceAsStream("/octagon.theme.json"));
        //IntelliJTheme.setup(Entry.class.getResourceAsStream("/Xcode-Dark.theme.json"));

        Splash splash = new Splash(false);

        if (SystemInfo.isLinux) {
            JFrame.setDefaultLookAndFeelDecorated(true);
            JDialog.setDefaultLookAndFeelDecorated(true);
        }

        Srd.LANG.load(Entry.class.getResourceAsStream("/lang/en.properties"));

        if(!saveDir.isDirectory()) {
            saveDir.mkdir();
        }

        File saveFile = new File(saveDir, "globalprops.ser");

        if (!saveFile.exists()) {
            File w = createWorkspace();
            if (w == null) System.exit(0);
            saveFile.createNewFile();

            File javaBase = new File(saveDir, "java_base.jar");
            Files.copy(Objects.requireNonNull(Entry.class.getResourceAsStream("/java21_base.jar")), javaBase.toPath());
        } else {
            try (ObjectInputStream oi = new ObjectInputStream(new FileInputStream(saveFile))) {
                Global.appProps = (Properties) oi.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        if(!new File(Global.appProps.getProperty("lastWorkspace"), "workspace.ser").exists()) createWorkspace();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try (ObjectOutputStream oo = new ObjectOutputStream(new FileOutputStream(saveFile))) {
                oo.writeObject(Global.appProps);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }));

        new ProjectEditor(new File(Global.appProps.getProperty("lastWorkspace"), "workspace.ser"), splash);
    }

    public static String convertToString(final Serializable object) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(object);
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        }
    }

    public static Object convertFrom(final String objectAsString) throws IOException, ClassNotFoundException {
        final byte[] data = Base64.getDecoder().decode(objectAsString);
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data))) {
            return ois.readObject();
        }
    }
}
