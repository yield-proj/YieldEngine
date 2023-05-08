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

import com.xebisco.yield.Texture;
import com.xebisco.yield.TextureManager;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;

public class ImageLoader implements TextureManager {
    public Image load(InputStream inputStream) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer width = stack.mallocInt(1), height = stack.mallocInt(1), channels = stack.mallocInt(1);

            ByteBuffer imageBuffer = IOUtil.fromInputStream(inputStream);

            if (!stbi_info_from_memory(imageBuffer, width, height, channels)) {
                throw new ResourceException("Failed to read image information: " + stbi_failure_reason());
            }

            ByteBuffer image = stbi_load_from_memory(imageBuffer, width, height, channels, 4);
            if (image == null)
                throw new ResourceException("Failed to load image: " + stbi_failure_reason());

            int id = glGenTextures();

            glBindTexture(GL_TEXTURE_2D, id);

            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0), height.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);

            return new Image(image, id, width.get(0), height.get(0), channels.get(0));
        } catch (IOException e) {
            throw new ResourceException(e.getMessage());
        }
    }

    @Override
    public Object loadTexture(Texture texture) {
        return load(texture.getInputStream());
    }

    @Override
    public void unloadTexture(Texture texture) {
        glDeleteTextures(((Image) texture.getImageRef()).getId());
    }

    @Override
    public int getImageWidth(Object imageRef) {
        return ((Image) imageRef).getWidth();
    }

    @Override
    public int getImageHeight(Object imageRef) {
        return ((Image) imageRef).getHeight();
    }
}
