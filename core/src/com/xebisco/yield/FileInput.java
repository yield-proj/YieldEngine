package com.xebisco.yield;

import java.io.InputStream;

public class FileInput {
    private final InputStream inputStream;

    public FileInput(String relativePath) {
        if (!relativePath.startsWith("/")) relativePath = "/" + relativePath;
        inputStream = FileInput.class.getResourceAsStream(relativePath);
    }

    public FileInput(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public InputStream getInputStream() {
        return inputStream;
    }
}
