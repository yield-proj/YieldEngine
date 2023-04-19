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

/**
 * The Texture class represents an image texture and provides methods for processing and manipulating it.
 */
public class Texture extends FileInput implements Disposable {

    private final ImmutableSize2D size;
    private final TextureManager textureManager;
    private Object imageRef;

    public Texture(String relativePath, TextureManager textureManager) {
        super(relativePath);
        this.textureManager = textureManager;
        imageRef = textureManager.loadTexture(this);
        size = new ImmutableSize2D(textureManager.getImageWidth(imageRef), textureManager.getImageHeight(imageRef));
    }

    public Texture(InputStream inputStream, TextureManager textureManager) {
        super(inputStream);
        this.textureManager = textureManager;
        imageRef = textureManager.loadTexture(this);
        size = new ImmutableSize2D(textureManager.getImageWidth(imageRef), textureManager.getImageHeight(imageRef));
    }

    public Texture(Object imageRef, InputStream inputStream, TextureManager textureManager) {
        super(inputStream);
        this.textureManager = textureManager;
        this.imageRef = imageRef;
        size = new ImmutableSize2D(textureManager.getImageWidth(imageRef), textureManager.getImageHeight(imageRef));
    }

    @Deprecated
    public void process(GPUPixelProcessor gpuPixelProcessor) {
        gpuPixelProcessor.originalPixels = getTextureManager().getPixels(getImageRef());
        if (gpuPixelProcessor.pixels == null || gpuPixelProcessor.pixels.length != gpuPixelProcessor.originalPixels.length)
            gpuPixelProcessor.pixels = new int[gpuPixelProcessor.originalPixels.length];
        gpuPixelProcessor.execute(gpuPixelProcessor.originalPixels.length / 4);

        setPixels(gpuPixelProcessor.pixels);
    }

    /**
     * The function processes pixels by getting their color values, processing them, and setting the resulting color values
     * for each pixel.
     *
     * @param pixelProcessor The parameter `pixelProcessor` is an object of the `PixelProcessor` class, which is
     * responsible for processing each pixel of an image. It contains a method `process` that takes a `Pixel` object as
     * input and returns a modified `Pixel` object. The `process` method is called
     */
    public void process(PixelProcessor pixelProcessor) {
        Pixel actPixel = new Pixel();
        int[] toSetPixels = new int[(int) getSize().getWidth() * (int) getSize().getHeight() * 4];
        for (int i = 0; i < toSetPixels.length / 4; i++) {
            int x = i, y = 0;
            while (x > getSize().getWidth() - 1) {
                x -= getSize().getWidth();
                y++;
            }

            int[] pixel = getTextureManager().getPixel(getImageRef(), x, y);
            actPixel.setColor(new Color(pixel[0] / 255.0, pixel[1] / 255.0, pixel[2] / 255.0, pixel[3] / 255.0));
            actPixel.setIndex(i);
            Pixel p = pixelProcessor.process(actPixel);
            int index = p.getIndex() * 4;
            toSetPixels[index] = (int) (p.getColor().getRed() * 255.0);
            toSetPixels[index + 1] = (int) (p.getColor().getGreen() * 255.0);
            toSetPixels[index + 2] = (int) (p.getColor().getBlue() * 255.0);
            toSetPixels[index + 3] = (int) (p.getColor().getAlpha() * 255.0);
        }
        setPixels(toSetPixels);
    }

    /**
     * This function sets the pixels of an image asynchronously using a provided array of integers.
     *
     * @param toSetPixels toSetPixels is an integer array that contains the pixel values to be set for the image referenced
     * by imageRef. Each element of the array represents the color of a single pixel in the image. The order of the
     * elements in the array corresponds to the order of the pixels in the image, typically from
     */
    public void setPixels(int[] toSetPixels) {
        CompletableFuture.runAsync(() -> getTextureManager().setPixels(imageRef, toSetPixels));
    }

    /**
     * This function crops a texture image based on the given x, y, width, and height parameters.
     *
     * @param x The x-coordinate of the top-left corner of the rectangular area to be cropped from the original texture.
     * @param y The "y" parameter in the "crop" method is the vertical coordinate of the top-left corner of the rectangular
     * area to be cropped from the original image.
     * @param w w stands for width. It is the width of the rectangular area that will be cropped from the original image.
     * @param h h stands for height. It is the height of the rectangular area that will be cropped from the original image.
     * @return A Texture object is being returned.
     */
    public Texture crop(int x, int y, int w, int h) {
        return getTextureManager().cropTexture(imageRef, x, y, w, h);
    }

    /**
     * This function returns a scaled texture with the specified width and height.
     *
     * @param w The width of the scaled texture in pixels.
     * @param h The parameter "h" represents the desired height of the scaled texture.
     * @return A Texture object is being returned. The method `scaled` takes in two parameters `w` and `h` which represent
     * the width and height of the scaled texture. The method then calls the `scaledTexture` method of the texture manager,
     * passing in the `imageRef` and the width and height parameters. The `scaledTexture` method returns a new Texture
     * object that is a scaled version of
     */
    public Texture scaled(int w, int h) {
        return getTextureManager().scaledTexture(imageRef, w, h);
    }

    /**
     * The `dispose()` method is used to release any resources or memory used by the `Texture` object.
     */
    @Override
    public void dispose() {
        getTextureManager().unloadTexture(this);
    }

    /**
     * The function returns the reference to an image object.
     *
     * @return The method is returning the value of the variable `imageRef`, which is of type `Object`.
     */
    public Object getImageRef() {
        return imageRef;
    }

    /**
     * This function sets the image reference for an object.
     *
     * @param imageRef The parameter "imageRef" is an object that represents a reference to an image.
     */
    public void setImageRef(Object imageRef) {
        this.imageRef = imageRef;
    }

    /**
     * The function returns an immutable 2D size object.
     *
     * @return The method is returning an object of type `ImmutableSize2D`.
     */
    public ImmutableSize2D getSize() {
        return size;
    }

    /**
     * The function returns the texture manager object.
     *
     * @return The method is returning an object of type `TextureManager`.
     */
    public TextureManager getTextureManager() {
        return textureManager;
    }

}
