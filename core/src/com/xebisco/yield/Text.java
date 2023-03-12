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

import com.xebisco.yield.editoruse.VisibleOnInspector;

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
    public void dispose() {

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

    public DrawInstruction getDrawInstruction() {
        return drawInstruction;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }
}
