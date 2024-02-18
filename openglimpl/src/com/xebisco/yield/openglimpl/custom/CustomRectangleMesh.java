package com.xebisco.yield.openglimpl.custom;

import com.xebisco.yield.FileInput;
import com.xebisco.yield.RectangleMesh;

public class CustomRectangleMesh extends RectangleMesh {
    static final class ShaderFile extends FileInput {
        public ShaderFile(String path) {
            super(path);
        }

        public static String[] extensions() {
            return new String[]{"glsl", "shader", "frag", "vert", "fs", "vs"};
        }
    }

    private ShaderFile vertexShader = new ShaderFile("com/xebisco/yield/openglimpl/default2d.vert"), fragmentShader = new ShaderFile("com/xebisco/yield/openglimpl/default2d.frag");


}
