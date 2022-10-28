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

package com.xebisco.yield;

@Deprecated
public interface SampleGraphics
{
    /**
     * Sets the rotation of all the other objects.
     *
     * @param point The point to rotate.
     * @param angle The angle to rotate the by.
     */
    void setRotation(Vector2 point, float angle);

    /**
     * Draws a line between two points
     *
     * @param point1 The first point of the line.
     * @param point2 The second point of the line.
     * @param color The color of the line.
     */
    void drawLine(Vector2 point1, Vector2 point2, Color color);

    /**
     * Draws a rectangle.
     *
     * @param pos The position of the rectangle.
     * @param size The size of the rectangle.
     * @param color The color of the rectangle.
     * @param filled If true, the rectangle will be filled with the color. If false, it will be outlined.
     */
    void drawRect(Vector2 pos, Vector2 size, Color color, boolean filled);

    /**
     * Draws a rounded rectangle.
     *
     * @param pos The position of the rectangle.
     * @param size The size of the rectangle
     * @param color The color of the rectangle
     * @param filled If true, the rectangle will be filled with the color. If false, it will be outlined.
     * @param arcWidth The width of the arc.
     * @param arcHeight The height of the arc.
     */
    void drawRoundRect(Vector2 pos, Vector2 size, Color color, boolean filled, int arcWidth, int arcHeight);

    /**
     * Draws an oval at the given position with the given size and color
     *
     * @param pos The position of the oval.
     * @param size The size of the oval.
     * @param color The color of the shape.
     * @param filled If true, the oval will be filled with the color. If false, it will be an outline.
     */
    void drawOval(Vector2 pos, Vector2 size, Color color, boolean filled);

    /**
     * Draws an arc.
     *
     * @param pos The position of the arc.
     * @param size The size of the arc.
     * @param color The color of the arc.
     * @param filled If true, the arc will be filled with the color. If false, it will be outlined.
     * @param startAngle The angle at which the arc starts.
     * @param arcAngle The angle of the arc in degrees.
     */
    void drawArc(Vector2 pos, Vector2 size, Color color, boolean filled, int startAngle, int arcAngle);

    /**
     * Draws a string of text to the screen.
     *
     * @param str The string to draw
     * @param color The color of the text
     * @param pos The position of the text.
     * @param scale The scale of the text.
     * @param fontName The name of the font you want to use.
     */
    void drawString(String str, Color color, Vector2 pos, Vector2 scale, String fontName);

    /**
     * Draws a texture at a position with a size.
     *
     * @param texture The texture to draw.
     * @param pos The position of the texture.
     * @param size The size of the texture.
     */
    void drawTexture(Texture texture, Vector2 pos, Vector2 size);

    /**
     * Sets the filter to use in the render.
     *
     * @param filter The filter to be applied to the window.
     */
    void setFilter(Filter filter);

    /**
     * Sets the font of the text
     *
     * @param font The font to use.
     */
    void setFont(String font);

    /**
     * Returns the width of the given string in pixels.
     *
     * @param str The string to get the width of.
     * @return The width of the string.
     */
    float getStringWidth(String str);

    /**
     * Returns the width of the given string in pixels, using the given font.
     *
     * @param str The string to get the width of.
     * @param font The font to use.
     * @return The width of the string in pixels.
     */
    float getStringWidth(String str, String font);

    /**
     * Returns the height of the given string in pixels.
     *
     * @param str The string to get the height of.
     * @return The height of the string.
     */
    float getStringHeight(String str);

    /**
     * Returns the height of the given string in pixels, using the given font
     *
     * @param str The string to get the height of
     * @param font The font to use.
     * @return The height of the string in pixels.
     */
    float getStringHeight(String str, String font);

    /**
     * It allows you to execute any instruction you want
     *
     * @param instruction The instruction to be executed.
     */
    void custom(String instruction, Object... args);
}
