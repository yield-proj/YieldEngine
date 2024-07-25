package com.xebisco.yieldengine.glimpl.mem;

import com.xebisco.yieldengine.core.io.texture.ITextureLoader;
import com.xebisco.yieldengine.core.io.texture.Texture;
import com.xebisco.yieldengine.core.io.texture.TextureFilter;
import com.xebisco.yieldengine.core.io.texture.TextureMap;
import com.xebisco.yieldengine.glimpl.window.OGLWindow;
import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.lwjgl.opengl.GL30.*;

public class OGLTextureLoader implements ITextureLoader {

    private final OGLWindow window;
    private final Map<String, BufferedImage> maps = new HashMap<>();

    public OGLTextureLoader(OGLWindow window) {
        this.window = window;
    }

    @Override
    public Texture loadTexture(String absolutePath, TextureFilter filter) {
        BufferedImage image;
        try {
            image = ImageIO.read(new File(absolutePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return loadTextureFromBufferedImage(image, filter, window);
    }

    public static Texture loadTextureFromBufferedImage(BufferedImage image, TextureFilter filter, OGLWindow window) {
        AtomicInteger atomicTexId = new AtomicInteger(-1);

        int[] pixels = new int[image.getWidth() * image.getHeight()];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

        image.flush();

        window.getCallInOpenGLThread().add(() -> {
            ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4);

            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    int pixel = pixels[y * image.getWidth() + x];
                    buffer.put((byte) ((pixel >> 16) & 0xFF));
                    buffer.put((byte) ((pixel >> 8) & 0xFF));
                    buffer.put((byte) (pixel & 0xFF));
                    buffer.put((byte) ((pixel >> 24) & 0xFF));
                }
            }

            buffer.flip();

            int textureID = glGenTextures();

            glBindTexture(GL_TEXTURE_2D, textureID);

            glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

            int textureFilter = filter == TextureFilter.LINEAR ? GL_LINEAR : GL_NEAREST;

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, textureFilter);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, textureFilter);

            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

            glGenerateMipmap(GL_TEXTURE_2D);

            atomicTexId.set(textureID);
        });

        return Texture.create((OGLTextureIDGetter) atomicTexId::get, image.getWidth(), image.getHeight());
    }

    @Override
    public void unloadTexture(Serializable imageReference) {
        window.getCallInOpenGLThread().add(() -> {
            glDeleteTextures(((OGLTextureIDGetter) imageReference).getTextureID());
        });
    }

    @Override
    public TextureMap loadTextureMap(String absolutePath) {
        BufferedImage image;
        try {
            image = ImageIO.read(new File(absolutePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        maps.put(absolutePath, image);
        return TextureMap.create(absolutePath, image.getWidth(), image.getHeight());
    }

    @Override
    public void unloadTextureMap(Serializable imageReference) {
        maps.get((String) imageReference).flush();
    }

    @Override
    public Texture loadTexture(int x, int y, int width, int height, TextureMap textureMap, TextureFilter filter) {
        return loadTextureFromBufferedImage(maps.get((String) textureMap.getImageReference()).getSubimage(x, y, width, height), filter, window);
    }
}
