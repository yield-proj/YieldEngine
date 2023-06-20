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
 * The StandardPrefabs class provides methods for creating 2D entity prefabs with text, rectangles, and texture rectangles
 * components.
 */
public class StandardPrefabs {

    /**
     * This function creates a 2D entity prefab with text, color, font, and tags.
     *
     * @param contents The text contents that will be displayed by the Text component.
     * @param color    The color parameter is a Color object that represents the color of the text. It can be used to set the
     *                 color of the text to any desired color.
     * @param font     The font parameter is of type Font and represents the font to be used for the text.
     * @param tags     An array of strings that can be used to tag and identify the entity.
     * @return An instance of the class Entity2DPrefab is being returned.
     */
    /*public static Entity2DPrefab text(String contents, Color color, Font font, String[] tags) {
        return new Entity2DPrefab(
                tags,
                new ComponentCreation(Text.class, c -> {
                    if (contents != null)
                        ((Text) c).setContents(contents);
                    if (color != null)
                        ((Text) c).setColor(color);
                    if (font != null)
                        ((Text) c).setFont(font);
                })
        );
    }*/

    /**
     * This Java function returns a 2D entity prefab for text with specified contents, color, and font.
     *
     * @param contents The text contents that will be displayed by the Text component.
     * @param color    The color parameter is a Color object that represents the color of the text. It can be used to set the
     *                 color of the text to any desired color.
     * @param font     The font parameter is of type Font and represents the font to be used for the text.
     * @return The method is returning an instance of the Entity2DPrefab class.
     */
    /*public static Entity2DPrefab text(String contents, Color color, Font font) {
        return text(contents, color, font, new String[0]);
    }*/

    /**
     * This Java function returns a 2D entity prefab for text with specified contents and color.
     *
     * @param contents The text contents that will be displayed by the Text component.
     * @param color    The color parameter is a Color object that represents the color of the text. It can be used to set the
     *                 color of the text to any desired color.
     * @return The method is returning an instance of the Entity2DPrefab class.
     */
    /*public static Entity2DPrefab text(String contents, Color color) {
        return text(contents, color, null, new String[0]);
    }*/

    /**
     * This Java function returns a 2D entity prefab for text with specified contents.
     *
     * @param contents The text contents that will be displayed by the Text component.
     * @return The method is returning an instance of the Entity2DPrefab class.
     */
    /*public static Entity2DPrefab text(String contents) {
        return text(contents, null, null, new String[0]);
    }*/

    /**
     * This function creates a rectangle entity with specified properties.
     *
     * @param rectangleType   The class type of the rectangle component that will be created.
     * @param size            The size of the rectangle, represented by a Size2D object.
     * @param color           The color parameter is of type Color and represents the fill color of the rectangle.
     * @param filled          A boolean value indicating whether the rectangle should be filled with color or not. If set to true,
     *                        the rectangle will be filled with the specified color. If set to false, only the border of the rectangle will be
     *                        drawn with the specified border color and thickness.
     * @param borderThickness borderThickness is a Double parameter that represents the thickness of the border of the
     *                        rectangle. It is used in the creation of a new Entity2DPrefab object that contains a Rectangle component. If a value
     *                        is provided for borderThickness, it will be set for the Rectangle component. If no value is
     * @param tags            An array of strings that can be used to tag and identify the entity in the game engine.
     * @return The method is returning an instance of the Entity2DPrefab class.
     */
    public static Entity2DPrefab rectangleShape(Class<? extends Rectangle> rectangleType, Size2D size, Color color, Boolean filled, Double borderThickness, String[] tags) {
        return new Entity2DPrefab(
                tags,
                new ComponentCreation(rectangleType, c -> {
                    if (size != null)
                        ((Rectangle) c).setSize(size);
                    if (color != null)
                        ((Rectangle) c).setColor(color);
                    if (filled != null)
                        ((Rectangle) c).setFilled(filled);
                    if (borderThickness != null)
                        ((Rectangle) c).setBorderThickness(borderThickness);
                })
        );
    }

    /**
     * This function returns a 2D rectangle entity.
     *
     * @param size            The size of the rectangle, represented by a Size2D object.
     * @param color           The color parameter is of type Color and represents the fill color of the rectangle.
     * @param filled          A boolean value indicating whether the rectangle should be filled with color or not. If set to true,
     *                        the rectangle will be filled with the specified color. If set to false, only the border of the rectangle will be
     *                        drawn with the specified border color and thickness.
     * @param borderThickness borderThickness is a Double parameter that represents the thickness of the border of the
     *                        rectangle.
     * @param tags            An array of strings that can be used to tag and identify the entity.
     * @return The method is returning an instance of the Entity2DPrefab class.
     */
    public static Entity2DPrefab rectangle(Size2D size, Color color, Boolean filled, Double borderThickness, String[] tags) {
        return rectangleShape(Rectangle.class, size, color, filled, borderThickness, tags);
    }

    /**
     * This function returns a 2D rectangle entity.
     *
     * @param size            The size of the rectangle, represented by a Size2D object.
     * @param color           The color parameter is of type Color and represents the fill color of the rectangle.
     * @param filled          A boolean value indicating whether the rectangle should be filled with color or not. If set to true,
     *                        the rectangle will be filled with the specified color. If set to false, only the border of the rectangle will be
     *                        drawn with the specified border color and thickness.
     * @param borderThickness borderThickness is a Double parameter that represents the thickness of the border of the
     *                        rectangle.
     * @return The method is returning an instance of the Entity2DPrefab class.
     */
    public static Entity2DPrefab rectangle(Size2D size, Color color, Boolean filled, Double borderThickness) {
        return rectangle(size, color, filled, borderThickness, new String[0]);
    }

    /**
     * This function returns a 2D rectangle entity.
     *
     * @param size  The size of the rectangle, represented by a Size2D object.
     * @param color The color parameter is of type Color and represents the fill color of the rectangle.
     * @return The method is returning an instance of the Entity2DPrefab class.
     */
    public static Entity2DPrefab rectangle(Size2D size, Color color) {
        return rectangle(size, color, null, null, null);
    }

    /**
     * This function returns a 2D rectangle entity.
     *
     * @param size The size of the rectangle, represented by a Size2D object.
     * @return The method is returning an instance of the Entity2DPrefab class.
     */
    public static Entity2DPrefab rectangle(Size2D size) {
        return rectangle(size, null);
    }

    /**
     * This function creates a 2D entity prefab with a texture rectangle component of a specified size, texture path,
     * and tags.
     *
     * @param size        The size parameter is of type Size2D, which is a class representing a 2D size with width and height
     *                    values. It is used to set the size of the TextureRectangle component in the Entity2DPrefab.
     * @param texturePath The texturePath parameter is a String that represents the file path or URL of the texture image
     *                    that will be used for the TextureRectangle component.
     * @param tags        An array of strings that can be used to tag and identify the entity.
     * @return A `Entity2DPrefab` object is being returned.
     */
    public static Entity2DPrefab texRectangle(Size2D size, String texturePath, String[] tags) {
        return new Entity2DPrefab(
                tags,
                new ComponentCreation(TextureRectangle.class, c -> {
                    if (size != null)
                        ((TextureRectangle) c).setSize(size);
                }),
                new ComponentCreation(TextureRectangleLoader.class, c -> {
                    ((TextureRectangleLoader) c).setTexturePath(texturePath);
                })
        );
    }

    /**
     * This function creates a 2D entity prefab with a texture rectangle component of a specified size and texture path.
     *
     * @param size        The size parameter is of type Size2D, which is a class representing a 2D size with width and height
     *                    values. It is used to set the size of the TextureRectangle component in the Entity2DPrefab.
     * @param texturePath The texturePath parameter is a String that represents the file path or URL of the texture image
     *                    that will be used for the TextureRectangle component.
     * @return A `Entity2DPrefab` object is being returned.
     */
    public static Entity2DPrefab texRectangle(Size2D size, String texturePath) {
        return new Entity2DPrefab(
                new ComponentCreation(TextureRectangle.class, c -> {
                    if (size != null)
                        ((TextureRectangle) c).setSize(size);
                }),
                new ComponentCreation(TextureRectangleLoader.class, c -> {
                    ((TextureRectangleLoader) c).setTexturePath(texturePath);
                })
        );
    }

    /**
     * This function creates a 2D entity prefab with a texture rectangle component of a texture path
     * and tags.
     *
     * @param texturePath The texturePath parameter is a String that represents the file path or URL of the texture image
     *                    that will be used for the TextureRectangle component.
     * @param tags        An array of strings that can be used to tag and identify the entity.
     * @return A `Entity2DPrefab` object is being returned.
     */
    public static Entity2DPrefab texRectangle(String texturePath, String[] tags) {
        return new Entity2DPrefab(
                tags,
                new ComponentCreation(TextureRectangle.class, c -> {
                    ((TextureRectangle) c).setTextureSized(true);
                }),
                new ComponentCreation(TextureRectangleLoader.class, c -> {
                    ((TextureRectangleLoader) c).setTexturePath(texturePath);
                })
        );
    }

    /**
     * This function creates a 2D entity prefab with a texture rectangle component of a texture path.
     *
     * @param texturePath The texturePath parameter is a String that represents the file path or URL of the texture image
     *                    that will be used for the TextureRectangle component.
     * @return A `Entity2DPrefab` object is being returned.
     */
    public static Entity2DPrefab texRectangle(String texturePath) {
        return new Entity2DPrefab(
                new ComponentCreation(TextureRectangle.class, c -> {
                    ((TextureRectangle) c).setTextureSized(true);
                }),
                new ComponentCreation(TextureRectangleLoader.class, c -> {
                    ((TextureRectangleLoader) c).setTexturePath(texturePath);
                })
        );
    }

}
