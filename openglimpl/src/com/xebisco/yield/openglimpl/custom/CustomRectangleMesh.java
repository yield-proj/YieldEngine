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

import com.xebisco.yield.ContextTime;
import com.xebisco.yield.RectangleMesh;
import com.xebisco.yield.openglimpl.shader.AttribArray;
import com.xebisco.yield.openglimpl.shader.ConnectToShader;
import com.xebisco.yield.openglimpl.shader.Uniform;
import com.xebisco.yield.openglimpl.shader.Utils;
import com.xebisco.yield.openglimpl.shader.types.Mat4;
import com.xebisco.yield.openglimpl.shader.types.Vec2;

@ConnectToShader(vert = "com/xebisco/yield/openglimpl/default2d.vert", frag = "com/xebisco/yield/openglimpl/default2d.frag")
public class CustomRectangleMesh extends RectangleMesh {
    @AttribArray(index = 0)
    Vec2[] positions = new Vec2[]{
            new Vec2(-1, 1),
            new Vec2(1, 1),
            new Vec2(1, -1),
            new Vec2(-1, -1)
    };

    @AttribArray(index = 1)
    Vec2[] texCoord = new Vec2[]{
            new Vec2(0, 0),
            new Vec2(1, 0),
            new Vec2(1, 1),
            new Vec2(0, 1)
    };

    @AttribArray(index = 2)
    int[] indices = new int[]{0, 1, 2, 2, 3, 0};

    @Uniform
    Mat4 viewMatrix, transformationMatrix;

    @Override
    public void onUpdate(ContextTime time) {
        viewMatrix = Utils.viewMatrix(application().scene().camera(), application().viewportSize());
        transformationMatrix = Utils.transformationMatrix(transform());
    }
}
