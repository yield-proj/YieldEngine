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

/**
 * It's a NonFillShape that renders text
 */
public class Text extends NonFillShape {
    private String contents = "Sample Text", font = "arial";
    private Vector2 textScale = new Vector2(1, 1);

    public Text() {
    }

    public Text(String contents) {
        this.contents = contents;
    }

    public Text(String contents, Color color) {
        this.contents = contents;
        setColor(color);
    }

    public Text(String contents, String font) {
        this.contents = contents;
        this.font = font;
    }

    public Text(String contents, String font, Color color) {
        this.contents = contents;
        this.font = font;
        setColor(color);
    }

    @Override
    public void render(SampleGraphics graphics) {
        super.render(graphics);
        Transform t = getEntity().getTransform();
        graphics.drawString(contents, getColor(), drawPosition, textScale.mul(t.scale), font);
        getSize().x = graphics.getStringWidth(contents, font) * t.scale.x;
        getSize().y = graphics.getStringHeight(contents, font) * t.scale.y;
    }

    @Override
    public boolean colliding(float x, float y) {
        Transform t = getTransform();
        return x >= t.position.x - getSize().x * t.scale.x / 2f && x <= t.position.x + getSize().x * t.scale.y / 2f &&
                y >= t.position.y - getSize().y * t.scale.y / 2f && y <= t.position.y + getSize().y * t.scale.y / 2f;
    }

    /**
     * This function returns the contents of the current object.
     *
     * @return The contents of the file.
     */
    public String getContents() {
        return contents;
    }

    /**
     * This function sets the contents of the current object to the contents of the parameter.
     *
     * @param contents The contents of the text.
     */
    public void setContents(String contents) {
        this.contents = contents;
    }

    /**
     * This function returns the font of the text.
     *
     * @return The font of the text.
     */
    public String getFont() {
        return font;
    }

    /**
     * This function sets the font of the text.
     *
     * @param font The font to use for the text.
     */
    public void setFont(String font) {
        this.font = font;
    }

    /**
     * Returns the scale of the text
     *
     * @return The textScale variable is being returned.
     */
    public Vector2 getTextScale() {
        return textScale;
    }

    /**
     * Sets the scale of the text
     *
     * @param textScale The scale of the text.
     */
    public void setTextScale(Vector2 textScale) {
        this.textScale = textScale;
    }
}
