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

import com.xebisco.yield.Vector2D;
import com.xebisco.yield.font.Font;
import com.xebisco.yield.font.FontCharacter;
import com.xebisco.yield.manager.FontManager;
import com.xebisco.yield.texture.Texture;
import com.xebisco.yield.texture.TextureFilter;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.util.freetype.FT_Bitmap;
import org.lwjgl.util.freetype.FT_Face;
import org.lwjgl.util.freetype.FT_GlyphSlot;

import java.nio.IntBuffer;
import java.util.Objects;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.util.freetype.FreeType.*;

public class OpenGLFontManager implements FontManager {

    private PointerBuffer ftLib;
    private long ftLibL;

    public PointerBuffer ftLib() {
        return ftLib;
    }

    public OpenGLFontManager setFtLib(PointerBuffer ftLib) {
        this.ftLib = ftLib;
        return this;
    }

    public long ftLibL() {
        return ftLibL;
    }

    public OpenGLFontManager setFtLibL(long ftLibL) {
        this.ftLibL = ftLibL;
        return this;
    }

    @Override
    public Object loadFont(Font font) {
        if (ftLib == null) {
            FT_Init_FreeType(ftLib = MemoryUtil.memAllocPointer(1));
            ftLibL = ftLib.get();
        }


        PointerBuffer fontPointer;

        FT_New_Face(ftLibL, font.path(), 0, fontPointer = MemoryUtil.memAllocPointer(1));

        FT_Face face = FT_Face.create(fontPointer.get());

        FT_Set_Pixel_Sizes(face, 0, (int) font.size());

        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

        IntBuffer index = MemoryUtil.memAllocInt(1);

        long c = FT_Get_First_Char(face, index);

        while (true) {
            FT_Load_Char(face, c, FT_LOAD_RENDER);
            int texture = glGenTextures();
            //noinspection resource
            FT_Bitmap bitmap = Objects.requireNonNull(face.glyph()).bitmap();
            glBindTexture(GL_TEXTURE_2D, texture);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RED, bitmap.width(), bitmap.rows(), 0, GL_RED, GL_UNSIGNED_BYTE, bitmap.buffer(bitmap.width() * bitmap.rows()));
            // set texture options
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

            FT_GlyphSlot glyph = Objects.requireNonNull(face.glyph());


            Texture t = new Texture(texture, new Vector2D(bitmap.width(), bitmap.rows()), font.path(), TextureFilter.LINEAR, null);

            //noinspection resource
            font.characterMap().put((char) (int) c, new FontCharacter(t, glyph.bitmap_top(), (int) glyph.advance().x() >> 6));
            MemoryUtil.memFree(index);
            index = MemoryUtil.memAllocInt(1);
            c = FT_Get_Next_Char(face, c, index);
            if(index.get() == 0) {
                MemoryUtil.memFree(index);
                break;
            }
        }

        FT_Done_Face(face);
        return null;
    }

    @Override
    public void unloadFont(Font font) {

    }
}
