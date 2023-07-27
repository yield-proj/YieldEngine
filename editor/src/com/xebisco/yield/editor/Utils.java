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
import java.awt.*;
import java.io.File;

public class Utils {
    public static final File EDITOR_DIR = editorDirectory();

    public static void error(Component component, Throwable e) {
        e.printStackTrace();
        errorNoStackTrace(component, e);
    }

    public static void errorNoStackTrace(Component component, Throwable e) {
        JOptionPane.showMessageDialog(component, e.getMessage(), e.getClass().getSimpleName(), JOptionPane.ERROR_MESSAGE);
    }

    private static File editorDirectory()
    {
        String OS = System.getProperty("os.name").toUpperCase();
        if (OS.contains("WIN"))
            return new File(System.getenv("APPDATA"), ".yield_editor");
        else if (OS.contains("MAC"))
            return new File(System.getProperty("user.home") + "/Library/Application Support", ".yield_editor");
        else if (OS.contains("NUX"))
            return new File(System.getProperty("user.home"), ".yield_editor");
        return new File(System.getProperty("user.dir"), ".yield_editor");
    }

    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}
