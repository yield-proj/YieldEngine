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

public class Texture extends FileInput implements Disposable {

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

    public Texture(Object imageRef, InputStream inputStream, TextureManager textureManager) {
        super(inputStream);
        this.textureManager = textureManager;
        this.imageRef = imageRef;
        size = new ImmutableSize2D(textureManager.getImageWidth(imageRef), textureManager.getImageHeight(imageRef));
        originalPixels = textureManager.getPixels(imageRef);
    }

    public void process(GPUPixelProcessor gpuPixelProcessor) {
        gpuPixelProcessor.originalPixels = originalPixels;
        if(gpuPixelProcessor.pixels == null || gpuPixelProcessor.pixels.length != gpuPixelProcessor.originalPixels.length)
            gpuPixelProcessor.pixels = new int[gpuPixelProcessor.originalPixels.length];
        gpuPixelProcessor.execute(originalPixels.length / 4);

        setPixels(gpuPixelProcessor.pixels);
    }

    public void process(PixelProcessor pixelProcessor) {
        Pixel actPixel = new Pixel();
        int[] toSetPixels = new int[originalPixels.length];
        for(int i = 0; i < originalPixels.length / 4; i++) {
            int index = i * 4;
            actPixel.setColor(new Color(originalPixels[index] / 255.0, originalPixels[index + 1] / 255.0, originalPixels[index + 2] / 255.0, originalPixels[index + 3] / 255.0));
            actPixel.setIndex(i);
            Pixel p = pixelProcessor.process(actPixel);
            index = p.getIndex() * 4;
            toSetPixels[index] = (int) (p.getColor().getRed() * 255.0);
            toSetPixels[index + 1] = (int) (p.getColor().getGreen() * 255.0);
            toSetPixels[index + 2] = (int) (p.getColor().getBlue() * 255.0);
            toSetPixels[index + 3] = (int) (p.getColor().getAlpha() * 255.0);
        }
        setPixels(toSetPixels);
    }

    public void setPixels(int[] toSetPixels) {
        CompletableFuture.runAsync(() -> getTextureManager().setPixels(imageRef, toSetPixels));
    }

    public Texture crop(int x, int y, int w, int h) {
        return getTextureManager().cropTexture(imageRef, x, y, w, h);
    }

    public Texture scaled(int w, int h) {
        return getTextureManager().scaledTexture(imageRef, w, h);
    }

    @Override
    public void dispose() {
        getTextureManager().unloadTexture(this);
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
