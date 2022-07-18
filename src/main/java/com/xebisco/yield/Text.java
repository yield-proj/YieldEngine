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

public class Text extends NonFillShape {
    private String contents = "Sample Text", font = "arial";
    private Vector2 textSize = new Vector2();

    public Text() {
    }

    public Text(String contents) {
        this.contents = contents;
    }

    public Text(String contents, String font) {
        this.contents = contents;
        this.font = font;
    }

    @Override
    public void render(SampleGraphics graphics) {
        super.render(graphics);
        Transform t = getEntity().getTransform();
        graphics.drawString(contents, getColor(), drawPosition, textSize.mul(t.scale), font);
        getSize().x = graphics.getStringWidth(contents, font) * t.scale.x;
        getSize().y = graphics.getStringHeight(contents, font) * t.scale.y;
    }

    @Override
    public boolean colliding(float x, float y) {
        return x >= drawPosition.x - getSize().x / 2f && x <= getSize().x / 2f &&
                y >= drawPosition.y - getSize().y / 2f && y <= drawPosition.y + getSize().y / 2f;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public Vector2 getTextSize() {
        return textSize;
    }

    public void setTextSize(Vector2 textSize) {
        this.textSize = textSize;
    }
}
