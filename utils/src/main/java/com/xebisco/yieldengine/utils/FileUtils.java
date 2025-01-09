package com.xebisco.yieldengine.utils;

import java.io.File;

public class FileUtils {
    public static final String OS = (System.getProperty("os.name")).toUpperCase();

    public static File getWorkingDirectory() {
        String workingDirectory;
        if (OS.contains("WIN")) {
            workingDirectory = System.getenv("AppData");
        } else {
            workingDirectory = System.getProperty("user.home");
        }
        return new File(workingDirectory);
    }

}
