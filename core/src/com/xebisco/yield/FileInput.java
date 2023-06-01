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

package com.xebisco.yield;

import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The FileInput class provides methods for obtaining an InputStream from a file path or an existing InputStream.
 */
public class FileInput {
    private final InputStream inputStream;
    private final String fileFormat;
    private static final Pattern FORMAT_PATTERN = Pattern.compile(".([^.]+)$");

    public FileInput(String relativePath) {
        if (!relativePath.startsWith("/")) relativePath = "/" + relativePath;
        Matcher matcher = FORMAT_PATTERN.matcher(relativePath);
        if (matcher.find())
            fileFormat = matcher.group(1).toUpperCase();
        else fileFormat = null;
        inputStream = FileInput.class.getResourceAsStream(relativePath);
    }

    public FileInput(InputStream inputStream) {
        this.inputStream = inputStream;
        fileFormat = null;
    }

    /**
     * The function returns an InputStream object.
     *
     * @return The method `getInputStream()` is returning an `InputStream` object.
     */
    public InputStream getInputStream() {
        return inputStream;
    }

    /**
     * The function returns the file format as a string.
     *
     * @return The method is returning a String value which is the file format.
     */
    public String getFileFormat() {
        return fileFormat;
    }
}
