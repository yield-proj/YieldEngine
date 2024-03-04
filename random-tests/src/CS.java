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

import com.xebisco.yield.AbstractTexture;
import com.xebisco.yield.ContextTime;
import com.xebisco.yield.EmptyRenderable;
import com.xebisco.yield.openglimpl.shader.*;
import com.xebisco.yield.openglimpl.shader.types.Sampler2D;
import com.xebisco.yield.openglimpl.shader.types.Vec2;

@BlendFunc
@ConnectToShader(vert = "shader.vert", frag = "shader.frag")
public class CS extends EmptyRenderable {
    @AttribArray
    static final Vec2[] positions = Utils.SQUARE_POSITIONS, texCoords = Utils.SQUARE_TEX_COORDS;
    @Element
    static final int[] indices = Utils.SQUARE_INDICES;
    @Uniform
    Sampler2D texture_sampler = new Sampler2D(0);
    @Texture2D
    AbstractTexture tex = texture("yieldIcon.png");

    @Uniform
    float a;

    @Override
    public void onUpdate(ContextTime time) {
        a+= (float) time.deltaTime();
        application().scene().backGroundColor().setGreen(Math.abs(Math.sin(a)));
    }
}
