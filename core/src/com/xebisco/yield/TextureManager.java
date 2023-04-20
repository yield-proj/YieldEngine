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

/**
 * The TextureManager interface should be implemented by the specific platform running the application, it contains function for manipulation image files.
 */
public interface TextureManager {
    /**
     * This function loads a texture and returns the specific image reference.
     *
     * @param texture The "texture" parameter in the "loadTexture" method is an object of the "Texture" class.
     */
    Object loadTexture(Texture texture);

    /**
     * The function unloads a texture.
     *
     * @param texture The parameter "texture" is a variable of type "Texture" that represents a texture object. This
     * function "unloadTexture" is used to free up memory and resources associated with the texture object.
     */
    void unloadTexture(Texture texture);

    /**
     * The function sets the color of a pixel at a specific location in an image.
     *
     * @param imageRef The image reference is a variable that holds the reference to the image object that you want to
     * modify.
     * @param color The color parameter is of type Color and represents the color that will be used to set the pixel at the
     * specified x and y coordinates in the image.
     * @param x x is an integer value that represents the horizontal position of the pixel in the image.
     * @param y x is an integer value that represents the vertical position of the pixel in the image.
     */
    void setPixel(Object imageRef, Color color, int x, int y);

    /**
     * The function "getPixels" takes an image reference as input and returns an array of integers representing the pixels
     * of the image.
     *
     * @param imageRef The imageRef parameter is an object that represents a reference to an image.
     * @return An array of integers representing the pixels of the `imageRef`.
     */
    int[] getPixels(Object imageRef);

    /**
     * The function returns an array of integers representing the pixel values at a specified (x,y) coordinate of an image.
     *
     * @param imageRef The image reference is an object that represents the image from which we want to retrieve the pixel
     * value. It could be an instance of a class that represents an image file format, such as BufferedImage in Java or
     * PIL.Image in Python.
     * @param x The x-coordinate of the pixel you want to retrieve from the image. This represents the horizontal position
     * of the pixel in the image.
     * @param y The y-coordinate of the pixel you want to retrieve from the image. This represents the vertical position
     * of the pixel in the image.
     * @return An array of integers representing the color values of the pixel at the specified coordinates (x,y) in the
     * image referenced by the object imageRef.
     */
    int[] getPixel(Object imageRef, int x, int y);

    /**
     * Calculates the texture width.
     * @param imageRef The image.
     * @return The texture width.
     */
    int getImageWidth(Object imageRef);

    /**
     * Calculates the texture height.
     * @param imageRef The image.
     * @return The texture height.
     */
    int getImageHeight(Object imageRef);

    /**
     * This function crops a texture from an image reference at the specified x, y coordinates with the specified width and
     * height.
     *
     * @param imageRef The image reference or object that contains the texture to be cropped.
     * @param x The x-coordinate of the top-left corner of the rectangular area to be cropped from the texture.
     * @param y The parameter "y" in the method "cropTexture" represents the vertical starting position of the area to be
     * cropped from the texture. It is the distance in pixels from the top edge of the texture to the top edge of the
     * cropped area.
     * @param w w stands for the width of the cropped texture. It determines how many pixels wide the cropped texture will
     * be.
     * @param h h stands for height. It is the height of the rectangular area that needs to be cropped from the texture.
     * @return The method is returning a cropped texture of the input imageRef object, with the specified dimensions (x, y,
     * w, h) for the cropped area. The returned value is of type Texture.
     */
    Texture cropTexture(Object imageRef, int x, int y, int w, int h);

    /**
     * The function scales an image to a specified width and height and returns a texture object.
     *
     * @param imageRef The image reference or object that needs to be scaled. It could be an image file or an object
     * representing an image in memory.
     * @param w The parameter "w" in the method signature represents the desired width of the scaled texture. It is an
     * integer value that specifies the width in pixels.
     * @param h The parameter "h" in the method signature "Texture scaledTexture(Object imageRef, int w, int h)" represents
     * the desired height of the scaled texture. The method takes an image reference as input and returns a new texture
     * object that is scaled to the specified width and height.
     * @return The method `scaledTexture` is returning a `Texture` object.
     */
    Texture scaledTexture(Object imageRef, int w, int h);

    /**
     * This function returns a texture representing the current screen.
     *
     * @return A texture object is being returned.
     */
    Texture printScreenTexture();
}
