package com.xebisco.yield.uiutils.props;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class MeshProp extends PathProp {

    public MeshProp(String name, String value) {
        super(name, value, new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.getName().endsWith(".lmx2d");
            }

            @Override
            public String getDescription() {
                return "LaranxaGE 2D Mesh Files (.lmx2d)";
            }
        });
    }
}
