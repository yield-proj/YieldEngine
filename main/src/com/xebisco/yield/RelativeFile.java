/*
 * Copyright [2022] [Xebisco]
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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * It's a wrapper for an input stream that can be closed
 */
public class RelativeFile {
    private InputStream inputStream;

    private boolean flushAfterLoad = true;

    private final String cachedPath;

    public RelativeFile(String relativePath) {
        cachedPath = relativePath;
        if (relativePath != null) {
            if (!relativePath.startsWith("/"))
                relativePath = "/" + relativePath;
            inputStream = Yld.class.getResourceAsStream(relativePath);
        }
    }

    /**
     * If the input stream is not null, close it and set it to null.
     * The first thing the function does is check if the input stream is null. If it is, the function returns immediately.
     * If it's not, the function tries to close the input stream. If the close operation fails, the function throws a
     * RuntimeException.
     */
    public void flush() {
        if (getInputStream() != null) {
            try {
                getInputStream().close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        setInputStream(null);
    }

    /**
     * This function returns the inputStream variable.
     *
     * @return The inputStream variable is being returned.
     */
    public InputStream getInputStream() {
        return inputStream;
    }

    /**
     * This function sets the inputStream variable to the inputStream parameter.
     *
     * @param inputStream The input stream to read the file from.
     */
    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    /**
     * This function returns the path of the cached file
     *
     * @return The cachedPath variable is being returned.
     */
    public String getCachedPath() {
        return cachedPath;
    }

    /**
     * Returns true if the file will be flushed after use.
     *
     * @return The flushAfterLoad property.
     */
    public boolean isFlushAfterLoad() {
        return flushAfterLoad;
    }

    /**
     * Sets if the file will be flushed after use.
     *
     * @param flushAfterLoad The flushAfterLoad value to set.
     */
    public void setFlushAfterLoad(boolean flushAfterLoad) {
        this.flushAfterLoad = flushAfterLoad;
    }
}
