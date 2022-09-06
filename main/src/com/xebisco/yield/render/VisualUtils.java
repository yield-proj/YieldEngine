/*
 * Copyright [2022] [Xebisco]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xebisco.yield.render;

import com.xebisco.yield.*;

import java.io.InputStream;

/**
 * @since 4-1.2
 * @author Xebisco
 */
public interface VisualUtils {
    /**
     * Loads a texture into memory.
     *
     * @param texture The texture to load.
     */
    void loadTexture(Texture texture);
    /**
     * Unloads the texture from memory
     *
     * @param texture The texture to unload.
     */
    void unloadTexture(Texture texture);
    /**
     * Unloads all textures from memory
     */
    void unloadAllTextures();
    /**
     * Clears the specified texture.
     *
     * @param texture The texture to clear.
     */
    void clearTexture(Texture texture);
    /**
     * Loads a font from the file system and saves it to the font cache
     *
     * @param saveName The name you want to save the font as.
     * @param fontName The name of the font you want to load.
     * @param fontSize The size of the font.
     * @param fontStyle Font.PLAIN, Font.BOLD, Font.ITALIC, Font.BOLD and Font. ITALIC
     */
    void loadFont(String saveName, String fontName, float fontSize, int fontStyle);
    /**
     * Loads a font from a file
     *
     * @param fontName The name of the font.
     * @param size The size of the font you want to load.
     * @param sizeToLoad The size of the font to load.
     * @param fontFormat 0 = Bitmap, 1 = Vector
     * @param relativeFile The file to load the font from.
     */
    void loadFont(String fontName, float size, float sizeToLoad, int fontFormat, RelativeFile relativeFile);
    /**
     * Unloads a font from memory
     *
     * @param fontName The name of the font to unload.
     */
    void unloadFont(String fontName);
    /**
     * It returns a 2D array of colors that represent the colors of the pixels in the texture
     *
     * @param texture The texture to get the colors from.
     * @return A 2D array of Color objects.
     */
    Color[][] getTextureColors(Texture texture);
    /**
     * It takes a texture and a 2D array of colors, and sets the colors of the texture to the colors in the array
     *
     * @param texture The texture to set the colors of.
     * @param colors The colors to set the texture to.
     */
    void setTextureColors(Texture texture, Color[][] colors);
    /**
     * Set the pixel at the given position in the given texture to the given color.
     *
     * @param texture The texture to draw to.
     * @param color The color of the pixel.
     * @param position The position of the pixel to set.
     */
    void setPixel(Texture texture, Color color, Vector2 position);
    /**
     * It cuts a part of a texture and returns it as a new texture
     *
     * @param texture The texture to cut from.
     * @param x The x coordinate of the top left corner of the texture to cut out.
     * @param y The y coordinate of the top left corner of the texture to cut out.
     * @param width The width of the texture to be cut.
     * @param height The height of the texture to be cut.
     * @return A texture.
     */
    Texture cutTexture(Texture texture, int x, int y, int width, int height);
    /**
     * Duplicate the given texture.
     *
     * @param texture The texture to duplicate.
     * @return A new texture object that is a duplicate of the texture object passed in.
     */
    Texture duplicate(Texture texture);
    /**
     * It takes two textures and overlays them on top of each other.
     *
     * @param tex1 The first texture to be overlayed.
     * @param tex2 The texture that will be overlayed on top of tex1.
     * @param pos1 The position of the first texture.
     * @param pos2 The position of the second texture.
     * @return A new texture.
     */
    Texture overlayTexture(Texture tex1, Texture tex2, Vector2 pos1, Vector2 pos2);
    /**
     * It takes a texture and returns a new texture that is a scaled version of the original
     *
     * @param texture The texture to scale.
     * @param width The width of the new texture.
     * @param height The height of the new texture.
     * @return A texture.
     */
    Texture scaleTexture(Texture texture, int width, int height);

    /**
     * Returns the width of the texture with the given ID
     *
     * @param textureId The id of the texture to get the width of.
     * @return The width of the texture.
     */
    int getTextureWidth(int textureId);
    /**
     * Returns the height of the texture with the given ID
     *
     * @param textureId The id of the texture to get the width of.
     * @return The height of the texture.
     */
    int getTextureHeight(int textureId);
}
