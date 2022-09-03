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

    public Texture get() {
        return visualUtils.duplicate(this);
    }

    public void onLoad() {
        updatePixels();
    }

    public void updatePixels() {
        Color[][] colors = visualUtils.getTextureColors(this);
        pixels = new Pixel[colors.length][colors[0].length];
        for (int x = 0; x < colors.length; x++)
            for (int y = 0; y < colors[0].length; y++) {
                Pixel pixel = new Pixel();
                pixel.setColor(colors[x][y]);
                pixel.setTextureColor(Colors.TRANSPARENT.get());
                pixel.setTextureLocation(new Vector2(x, y));
                pixel.setLocation(new Vector2(x, y));
                pixels[x][y] = pixel;
            }
    }

    public void clear() {
        visualUtils.clearTexture(this);
    }

    public void setColor(Color color, Vector2 pos) {
        visualUtils.setPixel(this, color, pos);
    }

    public void setColors(Color[][] colors) {
        visualUtils.setTextureColors(this, colors);
    }

    public Color getColor(Vector2 pos) {
        return getColors()[(int) pos.x][(int) pos.y];
    }

    public Color[][] getColors() {
        return visualUtils.getTextureColors(this);
    }

    public Texture cut(Vector2 pos, Vector2 size) {
        return visualUtils.cutTexture(this, (int) pos.x, (int) pos.y, (int) size.x, (int) size.y);
    }

    public Texture scale(Vector2 size) {
        return visualUtils.scaleTexture(this, (int) size.x, (int) size.y);
    }

    public void processFilters() {
        //clear();
        int width = getWidth() - 1, height = getHeight() - 1;
        for (int x = 0; x < pixels.length; x++) {
            for (int y = 0; y < pixels[0].length; y++) {
                    Pixel pixel = pixels[x][y];
                    filter.process(pixel);
                    while (pixel.getTextureLocation().x > width)
                        pixel.getTextureLocation().x -= width + 1;
                    while (pixel.getTextureLocation().x < 0)
                        pixel.getTextureLocation().x += width + 1;
                    while (pixel.getTextureLocation().y > height)
                        pixel.getTextureLocation().y -= height + 1;
                    while (pixel.getTextureLocation().y < 0)
                        pixel.getTextureLocation().y += height + 1;
                    setColor(pixel.getTextureColor(), pixel.getTextureLocation());
            }
        }
        //setColors(colors);
    }

    public YldFilter getFilter() {
        return filter;
    }

    public void setFilter(YldFilter filter) {
        this.filter = filter;
    }

    public static int getTextures() {
        return textures;
    }

    public static void setTextures(int textures) {
        Texture.textures = textures;
    }

    public int getTextureID() {
        return textureID;
    }

    public VisualUtils getVisualUtils() {
        return visualUtils;
    }

    public void setVisualUtils(VisualUtils visualUtils) {
        this.visualUtils = visualUtils;
    }

    public int getWidth() {
        return visualUtils.getTextureWidth(textureID);
    }

    public int getHeight() {
        return visualUtils.getTextureHeight(textureID);
    }

    public Vector2 getSize() {
        return new Vector2(visualUtils.getTextureWidth(textureID), visualUtils.getTextureHeight(textureID));
    }

    public Texture getInvertedX() {
        return invertedX;
    }

    public void setInvertedX(Texture invertedX) {
        this.invertedX = invertedX;
    }

    public Texture getInvertedY() {
        return invertedY;
    }

    public void setInvertedY(Texture invertedY) {
        this.invertedY = invertedY;
    }

    public Texture getInvertedXY() {
        return invertedXY;
    }

    public void setInvertedXY(Texture invertedXY) {
        this.invertedXY = invertedXY;
    }

    public TexType getTextureType() {
        return textureType;
    }

    public Pixel[][] getPixels() {
        return pixels;
    }

    public void setPixels(Pixel[][] pixels) {
        this.pixels = pixels;
    }
}
