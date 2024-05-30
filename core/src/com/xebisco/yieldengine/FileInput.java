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

package com.xebisco.yieldengine;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class represents a file input with its path and format.
 */
public class FileInput {
    private final String fileFormat, path;
    private static final Pattern FORMAT_PATTERN = Pattern.compile(".([^.]+)$");

    /**
     * Constructs a new {@link FileInput} object with the given path.
     *
     * @param path the path of the file
     */
    public FileInput(String path) {
        this.path = path;
        Matcher matcher = FORMAT_PATTERN.matcher(path);
        if (matcher.find())
            fileFormat = matcher.group(1).toUpperCase();
        else fileFormat = null;
    }

    /**
     * Returns the path of the file.
     *
     * @return the path of the file
     */
    public String path() {
        return path;
    }

    /**
     * Returns the format of the file.
     *
     * @return the format of the file {@code (e.g., "TXT", "JPG", "WAV")} or null if the format could not be determined
     */
    public String fileFormat() {
        return fileFormat;
    }
}
