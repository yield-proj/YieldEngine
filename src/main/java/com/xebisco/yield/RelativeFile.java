package com.xebisco.yield;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class RelativeFile {
    private InputStream stream;
    private String path;

    public RelativeFile(final String relativePath) {
        if(relativePath.hashCode() != "".hashCode()) {
            try {
                path = new File(relativePath).getCanonicalPath();
            } catch (IOException e) {
                e.printStackTrace();
            }
            stream = RelativeFile.class.getResourceAsStream(relativePath);
        }
    }

    public InputStream getStream() {
        return stream;
    }

    public void setStream(InputStream stream) {
        this.stream = stream;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
