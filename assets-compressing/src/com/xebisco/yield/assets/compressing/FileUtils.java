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

package com.xebisco.yield.assets.compressing;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

public final class FileUtils {
    public static void doCopy(InputStream is, OutputStream os) throws IOException {
        int oneByte;
        while ((oneByte = is.read()) != -1) {
            os.write(oneByte);
        }
        os.close();
        is.close();
    }

    public static boolean containsFile(File dir, String name) {
        if (!dir.isDirectory()) {
            throw new IllegalStateException("Not a directory: " + dir);
        }
        for(File f : Objects.requireNonNull(dir.listFiles())) {
            if (f.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public static void deleteDir(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                deleteDir(f);
            }
        }
        file.delete();
    }
}
