package com.xebisco.yieldengine.core.io;

import java.io.File;

public class DefaultAbsolutePathGetter implements IAbsolutePathGetter{
    @Override
    public String getAbsolutePath(String path) {
        return new File(System.getProperty("user.dir"), path).getAbsolutePath();
    }
}
