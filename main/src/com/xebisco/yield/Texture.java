/*
 * Copyright [2022] [Xebisco]
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

package com.xebisco.yield;

import com.xebisco.yield.render.VisualUtils;

/**
 * A Texture is an image for a game, can be added to graphical objects to display images.
 */
public class Texture extends RelativeFile {
    private static int textures;
    private VisualUtils visualUtils;
    private Pixel[][] pixels;
    private final TexType textureType;
    private final int textureID;
    private Texture invertedX, invertedY, invertedXY;

    private YldFilter filter;

    public Texture(String relativePath) {
        super(relativePath);
        textureType = TexType.SIMULATED;
        textures++;
        textureID = textures;
    }

    public Texture(String relativePath, TexType textureType) {
        super(relativePath);
        this.textureType = textureType;
        textures++;
        textureID = textures;
    }

    public Texture(RelativeFile relativeFile, TexType textureType) {
        super(relativeFile.getCachedPath());
        textures++;
        textureID = textures;
        setInputStream(relativeFile.getInputStream());
        setFlushAfterLoad(relativeFile.isFlushAfterLoad());
        this.textureType = textureType;
    }

    public Texture(RelativeFile relativeFile) {
        super(relativeFile.getCachedPath());
        textures++;
        textureID = textures;
        setInputStream(relativeFile.getInputStream());
        setFlushAfterLoad(relativeFile.isFlushAfterLoad());
        textureType = TexType.SIMULATED;
    }

    /**
     * This function returns a duplicate of the texture.
     *
     * @return A duplicate of the texture.
     */
    public Texture get() {
        return visualUtils.duplicate(this);
    }

    /**
     * When the texture loads, update the pixels.
     */
    public void onLoad() {
        updatePixels();
    }

    /**
     * It takes the texture of the sprite, converts it into a 2D array of pixels and instantiate all pixel objects.
     */
    public void updatePixels() {
        final Color[][] colors = visualUtils.getTextureColors(this);
        pixels = new Pixel[colors.length][colors[0].length];
        final PixelGrid pixelGrid = new PixelGrid(pixels);
        for (int x = 0; x < colors.length; x++)
            for (int y = 0; y < colors[0].length; y++) {
                Pixel pixel = new Pixel(x, y, pixelGrid);
                pixel.setColor(colors[x][y]);
                pixel.setOutColor(Colors.TRANSPARENT.get());
                pixel.setOutLocation(new Vector2(x, y));
                pixel.setLocation(new Vector2(x, y));
                pixels[x][y] = pixel;
            }
    }

    /**
     * Clear the texture by setting all pixels to transparent black.
     */
    public void clear() {
        visualUtils.clearTexture(this);
    }

    /**
     * Set the color of the pixel at the given position to the given color.
     *
     * @param color The color you want to set the pixel to.
     * @param pos The position of the pixel to set.
     */
    public void setColor(Color color, Vector2 pos) {
        visualUtils.setPixel(this, color, pos);
    }

    /**
     * Set the colors of the texture to the given 2D array of colors.
     *
     * @param colors The 2D array of colors to set the texture to.
     */
    public void setColors(Color[][] colors) {
        visualUtils.setTextureColors(this, colors);
    }

    /**
     * This function returns the color of the pixel at the given position.
     *
     * @param pos The position of the pixel you want to get the color of.
     * @return A color object.
     */
    public Color getColor(Vector2 pos) {
        return getColors()[(int) pos.x][(int) pos.y];
    }

    /**
     * This function returns a 2D array of colors that represents the texture.
     *
     * @return The colors of the texture.
     */
    public Color[][] getColors() {
        return visualUtils.getTextureColors(this);
    }

    /**
     * This function cuts a part of the current texture and returns it as a new texture.
     *
     * @param pos The position of the top left corner of the texture to cut out.
     * @param size The size of the texture to be cut.
     * @return A new Texture object.
     */
    public Texture cut(Vector2 pos, Vector2 size) {
        return visualUtils.cutTexture(this, (int) pos.x, (int) pos.y, (int) size.x, (int) size.y);
    }

    /**
     * This function takes a texture and returns a new texture that is the same as the original texture, but scaled to the
     * specified size.
     *
     * @param size The size to scale the texture to.
     * @return A new texture that is the same as the original texture, but scaled to the size specified.
     */
    public Texture scale(Vector2 size) {
        return visualUtils.scaleTexture(this, (int) size.x, (int) size.y);
    }

    /**
     * For each pixel, apply the filter, then make sure the pixel's new location is within the bounds of the image.
     */
    public void processFilters() {
        int width = getWidth() - 1, height = getHeight() - 1;
        for (int x = 0; x < pixels.length; x++) {
            for (int y = 0; y < pixels[0].length; y++) {
                    Pixel pixel = pixels[x][y];
                    filter.process(pixel);
                    while (pixel.getOutLocation().x > width)
                        pixel.getOutLocation().x -= width + 1;
                    while (pixel.getOutLocation().x < 0)
                        pixel.getOutLocation().x += width + 1;
                    while (pixel.getOutLocation().y > height)
                        pixel.getOutLocation().y -= height + 1;
                    while (pixel.getOutLocation().y < 0)
                        pixel.getOutLocation().y += height + 1;
                    setColor(pixel.getOutColor(), pixel.getOutLocation());
            }
        }
        //setColors(colors);
    }

    /**
     * Returns the filter used by this Texture
     *
     * @return The filter object.
     */
    public YldFilter getFilter() {
        return filter;
    }

    /**
     * This function sets the filter to the filter passed in.
     *
     * @param filter The filter to use for the query.
     */
    public void setFilter(YldFilter filter) {
        this.filter = filter;
    }

    /**
     * This function returns the number of textures that have been loaded.
     *
     * @return The number of textures.
     */
    public static int getTextures() {
        return textures;
    }

    /**
     * This function sets the value of the static variable textures to the value of the parameter textures.
     *
     * @param textures The number of textures to be loaded.
     */
    public static void setTextures(int textures) {
        Texture.textures = textures;
    }

    /**
     * This function returns the texture ID of the texture.
     *
     * @return The textureID is being returned.
     */
    public int getTextureID() {
        return textureID;
    }

    /**
     * This function returns the visualUtils object.
     *
     * @return The visualUtils object.
     */
    public VisualUtils getVisualUtils() {
        return visualUtils;
    }

    /**
     * This function sets the visualUtils variable to the value of the visualUtils parameter.
     *
     * @param visualUtils This is the VisualUtils value to set.
     */
    public void setVisualUtils(VisualUtils visualUtils) {
        this.visualUtils = visualUtils;
    }

    /**
     * Get the width of the texture.
     *
     * @return The width of the texture.
     */
    public int getWidth() {
        return visualUtils.getTextureWidth(textureID);
    }

    /**
     * Returns the height of the texture.
     *
     * @return The height of the texture.
     */
    public int getHeight() {
        return visualUtils.getTextureHeight(textureID);
    }

    /**
     * Returns the size of the texture in pixels.
     *
     * @return A new Vector2 object with the width and height of the texture.
     */
    public Vector2 getSize() {
        return new Vector2(visualUtils.getTextureWidth(textureID), visualUtils.getTextureHeight(textureID));
    }

    /**
     * This function returns the invertedX variable.
     *
     * @return The texture inverted in the X axis.
     */
    public Texture getInvertedX() {
        return invertedX;
    }

    /**
     * This function sets the invertedX variable to the value of the invertedX parameter.
     *
     * @param invertedX The texture that will be used for the inverted X axis.
     */
    public void setInvertedX(Texture invertedX) {
        this.invertedX = invertedX;
    }

    /**
     * Returns the inverted Y texture.
     *
     * @return The texture inverted in the Y axis.
     */
    public Texture getInvertedY() {
        return invertedY;
    }

    /**
     * This function sets the invertedY variable to the value of the invertedY parameter.
     *
     * @param invertedY The texture that will be used for the inverted Y axis.
     */
    public void setInvertedY(Texture invertedY) {
        this.invertedY = invertedY;
    }

    /**
     * This function returns the invertedXY texture.
     *
     * @return The texture inverted in the X axis and in the Y axis.
     */
    public Texture getInvertedXY() {
        return invertedXY;
    }

    /**
     * This function sets the invertedXY variable to the value of the invertedXY parameter.
     *
     * @param invertedXY The texture that will be used for the inverted XY axis.
     */
    public void setInvertedXY(Texture invertedXY) {
        this.invertedXY = invertedXY;
    }

    /**
     * This function returns the texture type of the current object.
     *
     * @return The texture type.
     */
    public TexType getTextureType() {
        return textureType;
    }

    /**
     * This function returns the pixels array.
     *
     * @return The pixels array.
     */
    public Pixel[][] getPixels() {
        return pixels;
    }

    /**
     * This function sets the pixels of the image to the pixels passed in as a parameter.
     *
     * @param pixels The 2D array of pixels that make up the image.
     */
    public void setPixels(Pixel[][] pixels) {
        this.pixels = pixels;
    }
}
