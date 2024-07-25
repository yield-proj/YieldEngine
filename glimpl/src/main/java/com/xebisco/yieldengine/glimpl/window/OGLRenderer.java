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

package com.xebisco.yieldengine.glimpl.window;

import com.xebisco.yieldengine.concurrency.LockProcess;
import com.xebisco.yieldengine.core.render.*;

import java.util.List;

public class OGLRenderer implements IRenderer {
    private final OGLWindow window;

    public OGLRenderer(OGLWindow window) {
        this.window = window;
    }

    @Override
    public void render(List<DrawInstruction> instructionsList, LockProcess lockProcess) {
        window.setInstructions(instructionsList);
        window.setLogicLock(lockProcess);
        if(window.getRepaintLock() != null)
            window.getRepaintLock().unlock();
    }

    /*public void render(Object program, ArrayContext arrayContext) {
        ShaderProgram shaderProgram = (ShaderProgram) program;
        shaderProgram.bind();

        glBindVertexArray((int) arrayContext.getContextObject());glEnableVertexAttribArray(0);

        glDrawElements(GL_TRIANGLES, arrayContext.getVertexCount(), GL_UNSIGNED_INT, 0);

        shaderProgram.unbind();
    }*/

    @Override
    public void dispose() {

    }
}
