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

import java.io.*;
import java.nio.file.Files;
import java.util.Properties;
import java.util.Random;
import java.util.zip.DeflaterOutputStream;

import static com.xebisco.yield.assets.FileUtils.*;

public class AssetsCompressing implements AutoCloseable {

    public static final long VERSION = 1;

    private final File outDir;
    private final boolean isTemporary;
    private final Properties fileIds = new Properties();

    public AssetsCompressing(File outDir) throws IOException {
        if (outDir == null) {
            this.outDir = Files.createTempDirectory("yield_file_compression").toFile();
            isTemporary = true;
        } else {
            this.outDir = outDir;
            isTemporary = false;
        }
    }

    public void addFile(File file, String id) throws IOException {
        String ins;
        do {
            ins = generateRandomHexString(12);
        } while (containsFile(outDir, ins + '.' + DEFLATED_SUFFIX));

        File deflatedFile = new File(outDir, ins + '.' + DEFLATED_SUFFIX);

        try (DeflaterOutputStream out = new DeflaterOutputStream(new FileOutputStream(deflatedFile))) {
            doCopy(new FileInputStream(file), out);
        }
        fileIds.put(id, ins);
    }

    public void generateZip(File zipFile) throws IOException {
        pack(outDir, zipFile);
    }

    private String generateRandomHexString(int length) {
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        while (sb.length() < length) {
            sb.append(Integer.toHexString(r.nextInt()));
        }

        return sb.toString();
    }

    public void storeFileIds() throws IOException {
        fileIds.store(new FileWriter(new File(outDir, "ids.properties")), "Generated with Yield AssetsCompressing v. " + VERSION);
    }

    @Override
    public void close() {
        if (isTemporary) deleteDir(outDir);
    }
}
