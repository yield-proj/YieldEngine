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

package com.xebisco.yieldengine.openglimpl.custom;

import com.xebisco.yieldengine.AbstractRenderable;
import com.xebisco.yieldengine.ContextTime;
import com.xebisco.yieldengine.Vector2D;
import com.xebisco.yieldengine.openglimpl.shader.*;
import com.xebisco.yieldengine.openglimpl.shader.types.Mat4;
import com.xebisco.yieldengine.openglimpl.shader.types.Vec2;

@ConnectToShader(vert = "com/xebisco/yieldengine/openglimpl/default2d.vert", frag = "com/xebisco/yieldengine/openglimpl/default2d.frag")
public class CustomRectangleMesh extends AbstractRenderable {
    @AttribArray
    static final Vec2[] positions = new Vec2[]{
            new Vec2(-1, 1),
            new Vec2(1, 1),
            new Vec2(1, -1),
            new Vec2(-1, -1)
    };

    @AttribArray
    static final Vec2[] texCoord = new Vec2[]{
            new Vec2(0, 0),
            new Vec2(1, 0),
            new Vec2(1, 1),
            new Vec2(0, 1)
    };

    @Element
    static final int[] indices = new int[]{0, 1, 2, 2, 3, 0};

    @Uniform
    Mat4 viewMatrix, transformationMatrix;

    public CustomRectangleMesh() {
        super(null);
    }

    @Override
    public void onUpdate(ContextTime time) {
        viewMatrix = Utils.viewMatrix(application().scene().camera(), application().viewportSize());
        transformationMatrix = Utils.transformationMatrix(transform(), new Vector2D(100, 100));
    }
}
