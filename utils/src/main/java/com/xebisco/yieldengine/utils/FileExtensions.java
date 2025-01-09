package com.xebisco.yieldengine.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FileExtensions {
    String[] IMAGE_EXTENSIONS = new String[]{"PNG", "JPEG", "JPG", "GIF", "TIFF", "WBMP", "BMP"};

    String name() default "Custom Files";

    String[] value();

    boolean acceptDirectories() default false;

    boolean acceptFiles() default true;
}
