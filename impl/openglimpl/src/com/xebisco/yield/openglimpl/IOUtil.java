/*
 * Copyright [2022-2023] [Xebisco]
 *
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

import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class IOUtil {
    public static ByteBuffer fromInputStream(InputStream inputStream) throws OGLImplIOException {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            byte[] bytes = inputStream.readAllBytes();

            ByteBuffer buffer = stack.malloc(bytes.length);
            for (byte b : bytes)
                buffer.put(b);

            buffer.flip();

            return buffer;
        } catch (IOException e) {
            throw new OGLImplIOException(e);
        }
    }
}
