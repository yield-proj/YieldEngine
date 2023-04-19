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
 * This is a class for rendering text with customizable font, color, and content.
 */
public class Text extends ComponentBehavior {
    private final DrawInstruction drawInstruction = new DrawInstruction();
    @VisibleOnInspector
    private Color color = Colors.LIGHT_BLUE.brighter();
    @VisibleOnInspector
    private String contents = "";
    @VisibleOnInspector
    private Font font;

    private double width, height;

    public Text() {
    }

    public Text(String contents) {
        this.contents = contents;
    }

    @Override
    public void onStart() {
        if(font == null) font = getApplication().getDefaultFont();
        drawInstruction.setSize(new Size2D(0, 0));
        drawInstruction.setType(DrawInstruction.Type.TEXT);
    }

    @Override
    public void render(PlatformGraphics graphics) {
        if (font != null) {
            drawInstruction.setRenderRef(contents);
            drawInstruction.setFont(font);
            drawInstruction.setRotation(getEntity().getTransform().getzRotation());
            drawInstruction.setInnerColor(color);
            drawInstruction.setPosition(getEntity().getTransform().getPosition());
            width = ((FontLoader) graphics).getStringWidth(contents, font.getFontRef());
            height = ((FontLoader) graphics).getStringHeight(contents, font.getFontRef());
            if (drawInstruction.getSize() != null) {
                drawInstruction.getSize().set(width, height);
                graphics.draw(drawInstruction);
            }
        }
    }

    /**
     * The function returns a DrawInstruction object.
     *
     * @return The method is returning an object of type `DrawInstruction`.
     */
    public DrawInstruction getDrawInstruction() {
        return drawInstruction;
    }

    /**
     * This function returns the color.
     *
     * @return The method `getColor()` is returning a `Color` object.
     */
    public Color getColor() {
        return color;
    }

    /**
     * This function sets the color of an object.
     *
     * @param color The "this.color" refers to the color variable of the current object instance.
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * The function returns the contents of a string.
     *
     * @return The method `getContents()` is returning a `String` variable named `contents`.
     */
    public String getContents() {
        return contents;
    }

    /**
     * This function sets the contents of this text.
     *
     * @param contents The parameter "contents" is a String that represents the new contents to be set for a variable in
     * the class.
     */
    public void setContents(String contents) {
        this.contents = contents;
    }

    /**
     * The function returns the font.
     *
     * @return The method is returning a Font object.
     */
    public Font getFont() {
        return font;
    }

    /**
     * This function sets the font of an object.
     *
     * @param font The "font" parameter is an object of the "Font" class.
     */
    public void setFont(Font font) {
        this.font = font;
    }

    /**
     * The function returns the value of the width variable as a double.
     *
     * @return The method `getWidth()` is returning a `double` value which represents the width.
     */
    public double getWidth() {
        return width;
    }

    /**
     * The function returns the value of the "height" variable as a double data type.
     *
     * @return The method `getHeight()` is returning a `double` value which represents the height.
     */
    public double getHeight() {
        return height;
    }
}
