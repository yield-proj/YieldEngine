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

package com.xebisco.yield.glimpl.shader;

import com.xebisco.yield.core.IBindUnbind;
import com.xebisco.yield.core.IDispose;
import com.xebisco.yield.logger.Logger;

import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram implements IBindUnbind, IDispose {
    private final int programID, vertexShaderID, fragmentShaderID;

    private ShaderProgram(int programID, int vertexShaderID, int fragmentShaderID) {
        this.programID = programID;
        this.vertexShaderID = vertexShaderID;
        this.fragmentShaderID = fragmentShaderID;
    }

    public static ShaderProgram create(String vertexShaderCode, String fragmentShaderCode) throws ShaderCreationException {
        int programID = glCreateProgram();

        if (programID == 0) {
            throw new ShaderCreationException("Failed to create shader program");
        }

        int vertexShaderID = ((vertexShaderCode == null || vertexShaderCode.isEmpty()) ? 0 : createShader(vertexShaderCode, GL_VERTEX_SHADER));
        if (vertexShaderID != 0)
            glAttachShader(programID, vertexShaderID);
        int fragmentShaderID = ((fragmentShaderCode == null || fragmentShaderCode.isEmpty()) ? 0 : createShader(fragmentShaderCode, GL_FRAGMENT_SHADER));
        if (fragmentShaderID != 0)
            glAttachShader(programID, fragmentShaderID);

        return new ShaderProgram(programID, vertexShaderID, fragmentShaderID);
    }

    public static ShaderProgram create() throws ShaderCreationException {
        return create(null, null);
    }

    private static int createShader(String shaderCode, int shaderType) throws ShaderCreationException {
        int shaderID = glCreateShader(shaderType);
        if (shaderID == 0) {
            throw new ShaderCreationException("Failed to create shader. Shader type " + shaderType + ".");
        }

        glShaderSource(shaderID, shaderCode);
        glCompileShader(shaderID);

        if (glGetShaderi(shaderID, GL_COMPILE_STATUS) == GL_FALSE) {
            throw new ShaderCreationException("Failed to compile shader." + glGetShaderInfoLog(shaderID, 1024));
        }

        return shaderID;
    }

    public void link() throws Exception {
        glLinkProgram(programID);
        if (glGetProgrami(programID, GL_LINK_STATUS) == 0) {
            throw new ShaderLinkException("Error linking. " + glGetProgramInfoLog(programID, 1024));
        }

        if (vertexShaderID != 0) {
            glDetachShader(programID, vertexShaderID);
        }
        if (fragmentShaderID != 0) {
            glDetachShader(programID, fragmentShaderID);
        }

        glValidateProgram(programID);
        if (glGetProgrami(programID, GL_VALIDATE_STATUS) == 0) {
            Logger.debug("VALIDATION WARNING: " + glGetProgramInfoLog(programID, 1024));
        }

    }

    @Override
    public void bind() {
        glUseProgram(programID);
    }

    @Override
    public void unbind() {
        glUseProgram(0);
    }

    @Override
    public void dispose() {
        unbind();
        if (programID != 0) {
            glDeleteProgram(programID);
        }
    }
}
