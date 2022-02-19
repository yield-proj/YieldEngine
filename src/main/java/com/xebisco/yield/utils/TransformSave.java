package com.xebisco.yield.utils;

import com.xebisco.yield.RelativeFile;

public class TransformSave extends RelativeFile {
    public TransformSave(String relativePath) {
        super(relativePath + ".ylds");
    }
}
