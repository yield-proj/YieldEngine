package com.xebisco.yield.utils;

import com.xebisco.yield.RelativeFile;

public class SaveFile extends RelativeFile {
    public SaveFile(String relativePath) {
        super(relativePath + ".ylds");
    }
    public SaveFile() {
        super("/com/xebisco/yield/assets/DefaultSave.ylds");
    }
}
