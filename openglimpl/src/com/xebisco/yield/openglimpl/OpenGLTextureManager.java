package com.xebisco.yield.openglimpl;

import com.xebisco.yield.Color;
import com.xebisco.yield.rendering.Form;
import com.xebisco.yield.texture.SpritesheetTexture;
import com.xebisco.yield.texture.Texture;
import com.xebisco.yield.texture.TextureFilter;
import com.xebisco.yield.manager.TextureManager;
import com.xebisco.yield.Vector2D;
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
    public Object loadTexture(Texture texture, Vector2D size) throws IOException {
        ByteBuffer image;
        IntBuffer w = BufferUtils.createIntBuffer(1);
        IntBuffer h = BufferUtils.createIntBuffer(1);
        IntBuffer c = BufferUtils.createIntBuffer(1);
        image = stbi_load(texture.path(), w, h, c, 4);
        if (image == null) throw new IOException("Could not load image: " + texture.path());

        int width = w.get(0);
        int height = h.get(0);
        int comp = c.get(0);

        int handler = loadTexture(image, width, height, comp, texture.filter());

        size.set(width, height);

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
    public void unloadTexture(Texture texture) {
        glDeleteTextures((int) texture.imageRef());
    }

    @Override
    public Object loadSpritesheetTexture(SpritesheetTexture spritesheetTexture, Vector2D size) throws IOException {
        BufferedImage image = ImageIO.read(new File(spritesheetTexture.path()));
        size.set(image.getWidth(), image.getHeight());
        return image;
    }

    @Override
    public void unloadSpritesheetTexture(SpritesheetTexture spritesheetTexture) {
        ((BufferedImage) spritesheetTexture.imageRef()).flush();
        spritesheetTexture.setImageRef(null);
    }

    @Override
    public Texture getTextureFromRegion(int x, int y, int width, int height, TextureFilter textureFilter, SpritesheetTexture spritesheetTexture) {
        BufferedImage image = ((BufferedImage) spritesheetTexture.imageRef()).getSubimage(x, y, width, height);
        try (MemoryStack stack = MemoryStack.stackPush()) {
            ByteBuffer imageBuffer = stack.malloc(image.getWidth() * image.getHeight() * 4);
            for(int iy = 0; iy < image.getHeight(); iy++) {
                for(int ix = 0; ix < image.getWidth(); ix++) {
                    Color color = new Color(image.getRGB(ix, iy), Color.Format.ARGB);
                    imageBuffer.put((byte) (color.red() * 255));
                    imageBuffer.put((byte) (color.green() * 255));
                    imageBuffer.put((byte) (color.blue() * 255));
                    imageBuffer.put((byte) (color.alpha() * 255));
                }
            }
            imageBuffer.flip();
            int handler = loadTexture(imageBuffer, width, height, 4, textureFilter);
            return new Texture(handler, new Vector2D(width, height), spritesheetTexture.path(), textureFilter, this);
        }
    }
}
