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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class FileUtils {
    public static void copyJarResourcesRecursively(Path destination, JarURLConnection jarConnection) throws IOException {
        JarFile jarFile = jarConnection.getJarFile();
        for (Iterator<JarEntry> it = jarFile.entries().asIterator(); it.hasNext(); ) {
            JarEntry entry = it.next();
            if (entry.getName().startsWith(jarConnection.getEntryName())) {
                if (!entry.isDirectory()) {
                    try (InputStream entryInputStream = jarFile.getInputStream(entry)) {
                        Files.copy(entryInputStream, Paths.get(destination.toString(), entry.getName()));
                    }
                } else {
                    Files.createDirectories(Paths.get(destination.toString(), entry.getName()));
                }
            }
        }
    }
}
