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

package com.xebisco.yield.glimpl.window;

import com.xebisco.yield.core.rendering.ArrayContext;
import com.xebisco.yield.core.rendering.IRenderer;
import com.xebisco.yield.core.rendering.Uniform;
import com.xebisco.yield.core.rendering.VertexArray;
import com.xebisco.yield.glimpl.shader.ShaderProgram;

import static org.lwjgl.opengl.GL30.*;

public class OGLRenderer implements IRenderer {
    @Override
    public void render(Object program, Uniform[] uniforms, VertexArray[] vertexes, ArrayContext arrayContext) {
        ShaderProgram shaderProgram = (ShaderProgram) program;
        shaderProgram.bind();

        glBindVertexArray((int) arrayContext.getContextObject());

        for(int i = 0; i < vertexes.length; i++) {
            VertexArray vertexArray = vertexes[i];
            glBindBuffer(GL_ARRAY_BUFFER, (int) vertexArray.getId());
            glVertexAttribPointer(i, vertexArray.getDimensions(), GL_FLOAT, false, 0, 0);
            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glEnableVertexAttribArray(i);
        }

        glDrawElements(GL_TRIANGLES, arrayContext.getVertexCount(), GL_UNSIGNED_INT, 0);

        for(int i = 0; i < vertexes.length; i++) {
            glDisableVertexAttribArray(i);
        }
        glBindVertexArray(0);

        shaderProgram.unbind();
    }
}
