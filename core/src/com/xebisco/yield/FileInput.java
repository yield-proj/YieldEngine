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

package com.xebisco.yield;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The FileInput class provides methods for obtaining an InputStream from a file path or an existing InputStream.
 */
public class FileInput {
    private final String fileFormat, path;
    private static final Pattern FORMAT_PATTERN = Pattern.compile(".([^.]+)$");

    public FileInput(String path) {
        this.path = path;
        Matcher matcher = FORMAT_PATTERN.matcher(path);
        if (matcher.find())
            fileFormat = matcher.group(1).toUpperCase();
        else fileFormat = null;
    }

    public String path() {
        return path;
    }

    /**
     * The function returns the file format as a string.
     *
     * @return The method is returning a String value which is the file format.
     */
    public String fileFormat() {
        return fileFormat;
    }
}
