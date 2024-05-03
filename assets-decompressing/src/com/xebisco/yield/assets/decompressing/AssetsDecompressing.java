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

package com.xebisco.yield.assets.decompressing;

import java.io.*;
import java.nio.file.Files;
import java.util.Properties;
import java.util.zip.InflaterInputStream;

import static com.xebisco.yield.assets.FileUtils.*;

public class AssetsDecompressing implements AutoCloseable {

    public static final long VERSION = 1;

    private final File assetsDir, operationsDir;
    private final boolean isTemporaryDir;
    private final Properties fileIds = new Properties();

    public AssetsDecompressing(File assetsDir) throws IOException {
        if (!assetsDir.exists()) {
            throw new IllegalArgumentException("Assets directory does not exist: " + assetsDir);
        }

        if (assetsDir.isFile()) {
            if (assetsDir.getName().endsWith(".zip")) {
                this.assetsDir = Files.createTempDirectory("yield_file_decompression:" + assetsDir).toFile();
                unpack(assetsDir, this.assetsDir);
                isTemporaryDir = true;
            } else {
                throw new IllegalArgumentException("assetsDir is not a directory: " + assetsDir);
            }
        } else {
            this.assetsDir = assetsDir;
            isTemporaryDir = false;
        }

        operationsDir = Files.createTempDirectory("yield_file_decompression_ops").toFile();
        fileIds.load(new FileInputStream(new File(this.assetsDir, "ids.properties")));
    }

    public File getFile(String id) throws IOException {
        if(!fileIds.containsKey(id)) throw new FileNotFoundException(id);
        File newFile = new File(operationsDir, fileIds.getProperty(id) + '.' + INFLATED_SUFFIX);
        newFile.createNewFile();
        doCopy(new InflaterInputStream(new FileInputStream(new File(assetsDir, fileIds.getProperty(id) + '.' + DEFLATED_SUFFIX))), new FileOutputStream(newFile));
        return newFile;
    }

    public void releaseFile(String id) throws IOException {
        if(!fileIds.containsKey(id)) throw new FileNotFoundException(id);
        new File(operationsDir, fileIds.getProperty(id) + '.' + INFLATED_SUFFIX).delete();
    }

    @Override
    public void close() {
        if (isTemporaryDir) {
            deleteDir(assetsDir);
        }
        deleteDir(operationsDir);
    }
}
