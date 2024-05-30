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

package com.xebisco.yieldengine.openglimpl;

import com.xebisco.yieldengine.openglimpl.shader.types.*;
import org.joml.Matrix4f;
import org.joml.Vector3fc;
import org.joml.Vector4fc;
import org.lwjgl.system.MemoryStack;

import java.io.InputStream;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;

public class OpenGLShaderProgram implements AutoCloseable {
    private final int vertexShader, fragmentShader;
    private final int programHandler;
    private final Map<String, Integer> uniforms = new HashMap<>();

    public OpenGLShaderProgram(String vertexShaderCode, String fragmentShaderCode) {
        programHandler = glCreateProgram();
        vertexShader = createShader(vertexShaderCode, GL_VERTEX_SHADER);
        fragmentShader = createShader(fragmentShaderCode, GL_FRAGMENT_SHADER);
        link();
    }

    public OpenGLShaderProgram(InputStream vertexShader, InputStream fragmentShader) {
        this(GLUtils.inputStreamToString(vertexShader), GLUtils.inputStreamToString(fragmentShader));
    }

    private void createUniform(String uniformName) {
        int uniformLocation = glGetUniformLocation(programHandler, uniformName);
        if (uniformLocation < 0) {
            throw new IllegalArgumentException("Could not find uniform: '" + uniformName + "'");
        }
        uniforms.put(uniformName, uniformLocation);
    }

    private void checkUniform(String uniformName) {
        if (uniforms.containsKey(uniformName)) return;
        createUniform(uniformName);
    }

    public void setUniform(String uniformName, Matrix4f value) {
        checkUniform(uniformName);
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer fb = stack.mallocFloat(16);
            value.get(fb);
            glUniformMatrix4fv(uniforms.get(uniformName), false, fb);
        }
    }

    public void setUniform(String uniformName, Mat4 value) {
        checkUniform(uniformName);
        glUniformMatrix4fv(uniforms.get(uniformName), false, value.data());
    }

    public void setUniform(String uniformName, Mat3 value) {
        checkUniform(uniformName);
        glUniformMatrix3fv(uniforms.get(uniformName), false, value.data());
    }

    public void setUniform(String uniformName, Mat2 value) {
        checkUniform(uniformName);
        glUniformMatrix2fv(uniforms.get(uniformName), false, value.data());
    }

    public void setUniform(String uniformName, Vector3fc value) {
        checkUniform(uniformName);
        glUniform3f(uniforms.get(uniformName), value.x(), value.y(), value.z());
    }

    public void setUniform(String uniformName, Vec3 value) {
        checkUniform(uniformName);
        glUniform3f(uniforms.get(uniformName), value.x(), value.y(), value.z());
    }

    public void setUniform(String uniformName, Vec2 value) {
        checkUniform(uniformName);
        glUniform2f(uniforms.get(uniformName), value.x(), value.y());
    }

    public void setUniform(String uniformName, Vector4fc value) {
        checkUniform(uniformName);
        glUniform4f(uniforms.get(uniformName), value.x(), value.y(), value.z(), value.w());
    }

    public void setUniform(String uniformName, Vec4 value) {
        checkUniform(uniformName);
        glUniform4f(uniforms.get(uniformName), value.x(), value.y(), value.z(), value.w());
    }

    public void setUniform(String uniformName, int value) {
        checkUniform(uniformName);
        glUniform1i(uniforms.get(uniformName), value);
    }

    public void setUniform(String uniformName, float value) {
        checkUniform(uniformName);
        glUniform1f(uniforms.get(uniformName), value);
    }

    public void setUniform(String uniformName, Sampler2D value) {
        checkUniform(uniformName);
        glUniform1i(uniforms.get(uniformName), value.id());
    }

    private int createShader(String shaderCode, int shaderType) {
        int shaderId = glCreateShader(shaderType);
        if (shaderId == 0) throw new IllegalStateException("Could not create shader");

        glShaderSource(shaderId, shaderCode);
        glCompileShader(shaderId);

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0)
            throw new IllegalStateException(glGetShaderInfoLog(shaderId, 1024));

        glAttachShader(programHandler, shaderId);

        return shaderId;
    }

    public void link() {
        glLinkProgram(programHandler);

        if (glGetProgrami(programHandler, GL_LINK_STATUS) == 0) {
            throw new IllegalStateException("Could not link shader code. " + glGetProgramInfoLog(programHandler, 1024));
        }

        if (vertexShader != 0) {
            glDetachShader(programHandler, vertexShader);
        }
        if (fragmentShader != 0) {
            glDetachShader(programHandler, fragmentShader);
        }

        glValidateProgram(programHandler);
        if (glGetProgrami(programHandler, GL_VALIDATE_STATUS) == 0) {
            throw new IllegalStateException("Could not validate shader code. " + glGetProgramInfoLog(programHandler, 1024));
        }
    }

    public void bind() {
        glUseProgram(programHandler);
    }

    public void unbind() {
        glUseProgram(0);
    }

    @Override
    public void close() {
        unbind();
        if (programHandler != 0) glDeleteProgram(programHandler);
    }

    public int vertexShader() {
        return vertexShader;
    }

    public int fragmentShader() {
        return fragmentShader;
    }

    public int programHandler() {
        return programHandler;
    }

    public Map<String, Integer> uniforms() {
        return uniforms;
    }
}
