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

package com.xebisco.yieldengine.glimpl.mem;

import com.xebisco.yieldengine.glimpl.shader.abstractions.ArrayContext;
import com.xebisco.yieldengine.glimpl.shader.abstractions.IArrayMemory;
import com.xebisco.yieldengine.glimpl.shader.abstractions.VertexArray;
import org.joml.Vector2fc;
import org.joml.Vector3fc;
import org.joml.Vector4fc;

import static org.lwjgl.opengl.GL30.*;

public class OGLArrayMemory implements IArrayMemory {

    @Override
    public ArrayContext createArrayContext(int vertexCount) {
        return new ArrayContext(glGenVertexArrays(), vertexCount);
    }

    @Override
    public ArrayContext createArrayContext(int[] elementsArray) {
        int vao = glGenVertexArrays();

        glBindVertexArray(vao);

        int indicesVbo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indicesVbo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementsArray, GL_STATIC_DRAW);

        return new ArrayContext(vao, elementsArray.length);
    }

    @Override
    public void disposeArrayContext(ArrayContext arrayContext) {
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);
        glDeleteVertexArrays((int) arrayContext.getContextObject());
    }

    @Override
    public VertexArray createVertexArray(Vector4fc[] vectors, int index, ArrayContext arrayContext) {
        glBindVertexArray((int) arrayContext.getContextObject());
        int vboId = glGenBuffers();
        float[] floats = new float[vectors.length * 4];
        for (int i = 0; i < vectors.length; i++) {
            floats[i * 4] = vectors[i].x();
            floats[i * 4 + 1] = vectors[i].y();
            floats[i * 4 + 2] = vectors[i].z();
            floats[i * 4 + 3] = vectors[i].w();
        }
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, floats, GL_STATIC_DRAW);
        glVertexAttribPointer(index, 4, GL_FLOAT, false, 0, 0);

        glBindVertexArray(0);
        return new VertexArray(vboId, 4);
    }

    @Override
    public VertexArray createVertexArray(Vector3fc[] vectors, int index, ArrayContext arrayContext) {
        glBindVertexArray((int) arrayContext.getContextObject());
        int vboId = glGenBuffers();
        float[] floats = new float[vectors.length * 3];
        for (int i = 0; i < vectors.length; i++) {
            floats[i * 3] = vectors[i].x();
            floats[i * 3 + 1] = vectors[i].y();
            floats[i * 3 + 2] = vectors[i].z();
        }
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, floats, GL_STATIC_DRAW);
        glVertexAttribPointer(index, 3, GL_FLOAT, false, 0, 0);

        glBindVertexArray(0);
        return new VertexArray(vboId, 3);
    }

    @Override
    public VertexArray createVertexArray(Vector2fc[] vectors, int index, ArrayContext arrayContext) {
        glBindVertexArray((int) arrayContext.getContextObject());
        int vboId = glGenBuffers();
        float[] floats = new float[vectors.length * 2];
        for (int i = 0; i < vectors.length; i++) {
            floats[i * 2] = vectors[i].x();
            floats[i * 2 + 1] = vectors[i].y();
        }
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, floats, GL_STATIC_DRAW);
        glVertexAttribPointer(index, 2, GL_FLOAT, false, 0, 0);

        glBindVertexArray(0);
        return new VertexArray(vboId, 2);
    }

    @Override
    public void disposeVertexArray(VertexArray vertexArray) {
        glBindVertexArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers((int) vertexArray.getId());
    }
}
