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

public class RelativeFile {
    private InputStream inputStream;
    private URL url;

    private boolean flushAfterLoad = true;

    private final String cachedPath;

    public RelativeFile(String relativePath) {
        cachedPath = relativePath;
        if (relativePath != null) {
            if (!relativePath.startsWith("/"))
                relativePath = "/" + relativePath;
            inputStream = Yld.class.getResourceAsStream(relativePath);
            url = Yld.class.getResource(relativePath);
        }
    }

    public void flush() {
        try {
            getInputStream().close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        setInputStream(null);
        setUrl(null);
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public String getCachedPath() {
        return cachedPath;
    }

    public boolean isFlushAfterLoad() {
        return flushAfterLoad;
    }

    public void setFlushAfterLoad(boolean flushAfterLoad) {
        this.flushAfterLoad = flushAfterLoad;
    }
}
