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

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.IntelliJTheme;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.util.SystemInfo;
import com.xebisco.yield.uiutils.Srd;

import javax.swing.*;
import java.io.*;
import java.util.Properties;

import static com.xebisco.yield.editor.app.ProjectEditor.createWorkspace;

public class Entry {
    public static void main(String[] args) throws IOException {
        System.setProperty("sun.java2d.opengl", "True");
        if (SystemInfo.isLinux) {
            JFrame.setDefaultLookAndFeelDecorated(true);
            JDialog.setDefaultLookAndFeelDecorated(true);
        }
        IntelliJTheme.setup(Entry.class.getResourceAsStream("/octagon.theme.json"));

        Srd.LANG.load(Entry.class.getResourceAsStream("/lang/en.properties"));

        File saveFile = new File(System.getProperty("user.home"), ".yeditor");

        if (!saveFile.exists()) {
            File w = createWorkspace();
            if (w == null) System.exit(0);
            saveFile.createNewFile();
        } else {
            try (ObjectInputStream oi = new ObjectInputStream(new FileInputStream(saveFile))) {
                Global.appProps = (Properties) oi.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try (ObjectOutputStream oo = new ObjectOutputStream(new FileOutputStream(saveFile))) {
                oo.writeObject(Global.appProps);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }));

        new ProjectEditor(new File(Global.appProps.getProperty("lastWorkspace"), "workspace.ser"));
    }
}
