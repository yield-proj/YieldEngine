package com.xebisco.yield.openglimpl;

import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL30.*;

public class Mesh2D implements AutoCloseable {
    public final int vertexCount, vao;
    private final List<Integer> vbos = new ArrayList<>();

    public Mesh2D(float[] positions, float[] texCoords, int[] indices) {
        vertexCount = indices.length;

        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        int vbo = glGenBuffers();
        vbos.add(vbo);
        FloatBuffer positionsBuffer = MemoryUtil.memCallocFloat(positions.length);
        positionsBuffer.put(positions);
        positionsBuffer.flip();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, positionsBuffer, GL_STATIC_DRAW);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);
        MemoryUtil.memFree(positionsBuffer);

        vbo = glGenBuffers();
        vbos.add(vbo);
        FloatBuffer textCoordsBuffer = MemoryUtil.memCallocFloat(texCoords.length);
        textCoordsBuffer.put(texCoords);
        textCoordsBuffer.flip();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, textCoordsBuffer, GL_STATIC_DRAW);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
        MemoryUtil.memFree(textCoordsBuffer);


        vbo = glGenBuffers();
        vbos.add(vbo);
        IntBuffer indicesBuffer = MemoryUtil.memCallocInt(vertexCount);
        indicesBuffer.put(indices).flip();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vbo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);
        MemoryUtil.memFree(indicesBuffer);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);

        System.gc();
    }

    @Override
    public void close() {
        glDisableVertexAttribArray(0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        vbos.forEach(GL20::glDeleteBuffers);

        glBindVertexArray(0);
        glDeleteVertexArrays(vao);
    }
}
