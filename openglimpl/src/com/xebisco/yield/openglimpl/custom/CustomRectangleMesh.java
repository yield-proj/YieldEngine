/*
 * Copyright [2022-2024] [Xebisco]
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
