package com.xebisco.yield;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class RelativeFile {
    private URL url;
    private String path, relativePath;

    public RelativeFile(final String relativePath) {
        this.relativePath = relativePath;
        if(relativePath.hashCode() != "".hashCode()) {
            try {
                path = new File(".", relativePath).getCanonicalPath();
            } catch (IOException e) {
                e.printStackTrace();
            }
            url = getClass().getResource(relativePath);
        }
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }
}
