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

import com.xebisco.yieldengine.AbstractTexture;
import com.xebisco.yieldengine.Color;
import com.xebisco.yieldengine.manager.FileIOManager;
import com.xebisco.yieldengine.texture.SpritesheetTexture;
import com.xebisco.yieldengine.texture.Texture;
import com.xebisco.yieldengine.texture.TextureFilter;
import com.xebisco.yieldengine.manager.TextureManager;
import com.xebisco.yieldengine.Vector2D;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryStack;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;

public class OpenGLTextureManager implements TextureManager {
    @Override
    public Object loadTexture(AbstractTexture texture, Vector2D size, FileIOManager ioManager) throws IOException {
        ByteBuffer image;
        IntBuffer w = BufferUtils.createIntBuffer(1);
        IntBuffer h = BufferUtils.createIntBuffer(1);
        IntBuffer c = BufferUtils.createIntBuffer(1);
        image = stbi_load(ioManager.loadPath(texture.path()), w, h, c, 4);
        if (image == null) throw new IOException("Could not load image: " + texture.path());

        int width = w.get(0);
        int height = h.get(0);
        int comp = c.get(0);

        int handler = loadTexture(image, width, height, comp, texture.filter());

        size.set(width, height);

        ioManager.releaseFile(texture.path());
        return handler;
    }

    private int loadTexture(ByteBuffer image, int width, int height, int comp, TextureFilter textureFilter) {

        int handler = glGenTextures();

        glBindTexture(GL_TEXTURE_2D, handler);

        if (comp == 3) {
            if ((width & 3) != 0) {
                glPixelStorei(GL_UNPACK_ALIGNMENT, 2 - (width & 1));
            }
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, image);
        } else {
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
        }

        int filter = textureFilter == TextureFilter.LINEAR ? GL_LINEAR : GL_NEAREST;
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, filter);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, filter);


        stbi_image_free(image);

        return handler;
    }

    @Override
    public void unloadTexture(AbstractTexture texture, FileIOManager ioManager) {
        glDeleteTextures((int) texture.imageRef());
        if (ioManager != null)
            ioManager.releaseFile(texture.path());
    }

    @Override
    public Object loadSpritesheetTexture(SpritesheetTexture spritesheetTexture, Vector2D size, FileIOManager ioManager) throws IOException {
        BufferedImage image = ImageIO.read(new File(ioManager.loadPath(spritesheetTexture.path())));
        size.set(image.getWidth(), image.getHeight());
        return image;
    }

    @Override
    public void unloadSpritesheetTexture(SpritesheetTexture spritesheetTexture, FileIOManager ioManager) {
        ((BufferedImage) spritesheetTexture.imageRef()).flush();
        spritesheetTexture.setImageRef(null);
        if (ioManager != null)
            ioManager.releaseFile(spritesheetTexture.path());
    }

    @Override
    public Texture getTextureFromRegion(int x, int y, int width, int height, TextureFilter textureFilter, SpritesheetTexture spritesheetTexture) {
        BufferedImage image = ((BufferedImage) spritesheetTexture.imageRef()).getSubimage(x, y, width, height);
        try (MemoryStack stack = MemoryStack.stackPush()) {
            ByteBuffer imageBuffer = stack.malloc(image.getWidth() * image.getHeight() * 4);
            for (int iy = 0; iy < image.getHeight(); iy++) {
                for (int ix = 0; ix < image.getWidth(); ix++) {
                    Color color = new Color(image.getRGB(ix, iy), Color.Format.ARGB);
                    imageBuffer.put((byte) (color.red() * 255));
                    imageBuffer.put((byte) (color.green() * 255));
                    imageBuffer.put((byte) (color.blue() * 255));
                    imageBuffer.put((byte) (color.alpha() * 255));
                }
            }
            imageBuffer.flip();
            int handler = loadTexture(imageBuffer, width, height, 4, textureFilter);
            return new Texture(handler, new Vector2D(width, height), spritesheetTexture.path(), textureFilter, this, null);
        }
    }
}
