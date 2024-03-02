package com.xebisco.yield.openglimpl.custom;

import com.xebisco.yield.FileInput;
import com.xebisco.yield.RectangleMesh;
import com.xebisco.yield.openglimpl.shader.AttribArray;
import com.xebisco.yield.openglimpl.shader.ConnectToShader;
import com.xebisco.yield.openglimpl.shader.ShaderType;
import com.xebisco.yield.openglimpl.shader.types.Vec2;

@ConnectToShader(shader = ShaderType.VERTEX_SHADER)
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

    @AttribArray(index = 0)
    private Vec2[] positions = new Vec2[]{
            new Vec2(-1, 1),
            new Vec2(1, 1),
            new Vec2(1, -1),
            new Vec2(-1, -1)
    };

    @AttribArray(index = 1)
    private Vec2[] texCoord = new Vec2[]{
            new Vec2(0, 0),
            new Vec2(1, 0),
            new Vec2(1, 1),
            new Vec2(0, 1)
    };

    @AttribArray(index = 2)
    private int[] indices = new int[] {0, 1, 2, 2, 3, 0};


}
