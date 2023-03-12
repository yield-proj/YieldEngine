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

package com.xebisco.yield;

import java.io.InputStream;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

public class Texture extends FileInput {

    private final ImmutableSize2D size;
    private final TextureManager textureManager;
    private Object imageRef;
    private int[] originalPixels;

    public Texture(String relativePath, TextureManager textureManager) {
        super(relativePath);
        this.textureManager = textureManager;
        imageRef = textureManager.loadTexture(this);
        size = new ImmutableSize2D(textureManager.getImageWidth(imageRef), textureManager.getImageHeight(imageRef));
        originalPixels = textureManager.getPixels(imageRef);
    }

    public Texture(InputStream inputStream, TextureManager textureManager) {
        super(inputStream);
        this.textureManager = textureManager;
        imageRef = textureManager.loadTexture(this);
        size = new ImmutableSize2D(textureManager.getImageWidth(imageRef), textureManager.getImageHeight(imageRef));
        originalPixels = textureManager.getPixels(imageRef);
    }

    public void processPixels(PixelProcessor pixelProcessor) {
        Pixel actPixel = new Pixel();
        int width = (int) size.getWidth();
        int height = (int) size.getHeight();
        int[] toSetPixels = new int[originalPixels.length];
        IntStream.range(0, width).parallel().forEach(y ->
                IntStream.range(0, height).parallel().forEach(x -> {
                    actPixel.setColor(new Color(originalPixels[x + y * width], Color.Format.ARGB));
                    actPixel.setX(x);
                    actPixel.setY(y);
                    Pixel pixel = pixelProcessor.process(actPixel);
                    int ax = pixel.getX();
                    int ay = pixel.getY();
                    while (ax >= width)
                        ax -= width;
                    while (ax < 0)
                        ax += width;
                    while (ay >= height)
                        ay -= height;
                    while (ay < 0)
                        ay += height;
                    toSetPixels[ax + ay * width] = pixel.getColor().getARGB();
                })
        );
    }

    public void asyncProcessPixels(PixelProcessor pixelProcessor) {
        CompletableFuture.runAsync(() -> processPixels(pixelProcessor)).exceptionally(e -> {
            throw new TextureProcessException(e);
        });
    }

    public Object getImageRef() {
        return imageRef;
    }

    public void setImageRef(Object imageRef) {
        this.imageRef = imageRef;
    }

    public ImmutableSize2D getSize() {
        return size;
    }

    public TextureManager getTextureManager() {
        return textureManager;
    }

    public int[] getOriginalPixels() {
        return originalPixels;
    }

    public void setOriginalPixels(int[] originalPixels) {
        this.originalPixels = originalPixels;
    }
}
