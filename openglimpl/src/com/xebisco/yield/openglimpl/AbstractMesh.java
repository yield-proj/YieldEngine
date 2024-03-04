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

package com.xebisco.yield.openglimpl;

import org.lwjgl.opengl.GL20;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL30.*;

public abstract class AbstractMesh implements AutoCloseable {
    private final int vao, vertexCount;
    protected final List<Integer> vbos = new ArrayList<>();

    public AbstractMesh(int vertexCount) {
        this.vertexCount = vertexCount;
        vao = glGenVertexArrays();
        glBindVertexArray(vao);
    }

    public abstract void init();

    @Override
    public void close() {
        glDisableVertexAttribArray(0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        vbos.forEach(GL20::glDeleteBuffers);

        glBindVertexArray(0);
        glDeleteVertexArrays(vao);
    }

    public int vao() {
        return vao;
    }

    public List<Integer> vbos() {
        return vbos;
    }

    public int vertexCount() {
        return vertexCount;
    }
}
