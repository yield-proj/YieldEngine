package com.xebisco.yieldengine.uiutils;

import com.xebisco.yieldengine.uiutils.fields.FileExtensions;
import sun.reflect.annotation.AnnotationParser;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class Utils {
    public static final FileExtensions IMAGE_FILE_EXTENSIONS = getFileExtensionsInstance(new String[]{"PNG", "JPG", "JPEG", "BMP", "WBMP", "GIF"}, "Image Files");

    public static <T extends Annotation> T getAnnotationInstance(Class<T> annotationClass, Map<String, Object> values) {
        //noinspection unchecked
        return (T) AnnotationParser.annotationForMap(annotationClass, values);
    }

    public static FileExtensions getFileExtensionsInstance(String[] extensions, String description) {
        Map<String, Object> values = new HashMap<>();
        values.put("extensions", extensions);
        values.put("description", description);
        return getAnnotationInstance(FileExtensions.class, values);
    }

    public static File getFile(JRootPane rootPane, File currentDir, FileExtensions fileExtensions) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if (fileExtensions != null) {
            chooser.setFileFilter(new FileFilter() {
                @Override
                public boolean accept(File f) {
                    if (f.isDirectory()) {
                        return true;
                    }
                    for (String ext : fileExtensions.extensions()) {
                        if (f.getName().toUpperCase().endsWith(ext.toUpperCase())) {
                            return true;
                        }
                    }
                    return false;
                }

                @Override
                public String getDescription() {
                    return fileExtensions.description();
                }
            });
        }

        chooser.setCurrentDirectory(currentDir);

        if (chooser.showOpenDialog(rootPane) == JFileChooser.APPROVE_OPTION)
            return chooser.getSelectedFile();
        return null;
    }
}
