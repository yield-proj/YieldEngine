/*
 * Copyright [2022-2024] [Xebisco]
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

package com.xebisco.yield.rendering;

import com.xebisco.yield.Color;
import com.xebisco.yield.Colors;
import com.xebisco.yield.Transform2D;
import com.xebisco.yield.Vector2D;
import com.xebisco.yield.font.Font;

public class Paint {
    private Object drawObj;
    private Vector2D rectSize;
    private Transform2D transformation;
    private String text;
    private Font font;
    private boolean hasImage;
    private Color color = new Color(Colors.WHITE);

    public Object drawObj() {
        return drawObj;
    }

    public Paint setDrawObj(Object drawObj) {
        this.drawObj = drawObj;
        return this;
    }

    public Transform2D transformation() {
        return transformation;
    }

    public Paint setTransformation(Transform2D transformation) {
        this.transformation = transformation;
        return this;
    }

    public boolean hasImage() {
        return hasImage;
    }

    public Paint setHasImage(boolean hasImage) {
        this.hasImage = hasImage;
        return this;
    }

    public Color color() {
        return color;
    }

    public Paint setColor(Color color) {
        this.color = color;
        return this;
    }

    public String text() {
        return text;
    }

    public Paint setText(String text) {
        this.text = text;
        return this;
    }

    public Font font() {
        return font;
    }

    public Paint setFont(Font font) {
        this.font = font;
        return this;
    }

    public Vector2D rectSize() {
        return rectSize;
    }

    public Paint setRectSize(Vector2D rectSize) {
        this.rectSize = rectSize;
        return this;
    }
}
