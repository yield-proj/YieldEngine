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

package com.xebisco.yield.ux;

import com.xebisco.yield.RelativeFile;
import com.xebisco.yield.render.RenderMaster;
import com.xebisco.yield.render.Renderable;
import com.xebisco.yield.render.RenderableType;
import com.xebisco.yield.Color;
import com.xebisco.yield.Colors;

import java.util.ConcurrentModificationException;
import java.util.LinkedHashSet;
import java.util.Set;

public class UXPanel extends UXCompartment {
    private RenderMaster renderMaster;
    private final Set<Renderable> renderables = new LinkedHashSet<>();
    private Color background = Colors.BLACK.get();

    public UXPanel(int width, int height) {
        setWidth(width);
        setHeight(height);
    }

    @Override
    public void update(UXGraphics graphics) {
        try {
            for (UXComponent component : getComponents()) {
                component.paintComponent(graphics);
            }
        } catch (ConcurrentModificationException ignore) {
        }
        super.update(graphics);
    }

    public int onWidthBounds(int x, int width) {
        if (x < 0)
            x = 0;
        x += getX();
        if (x + width - getX() > getWidth()) {
            return x - (x + width - getWidth() - getX());
        } else return x;
    }

    public int onHeightBounds(int y, int height) {
        if (y < 0)
            y = 0;
        y += getY();
        if (y + height - getY() > getHeight()) {
            return y - (y + height - getHeight() - getY());
        } else return y;
    }

    public void repaint() {
        renderables.clear();
        final Renderable renderable = new Renderable();
        renderable.setColor(background);
        renderable.setType(RenderableType.RECTANGLE);
        renderable.setFilled(true);
        renderable.setX(getX() + getWidth() / 2);
        renderable.setY(getY() + getHeight() / 2);
        renderable.setWidth(getWidth());
        renderable.setHeight(getHeight());
        renderables.add(renderable);
        UXGraphics graphics = new UXGraphics() {

            private Color color = Colors.WHITE.get();
            private UXFont font;
            private UXFontMetrics fontMetrics;
            private int thickness = 1, zIndex, rotation;

            public void drawShape(RenderableType type, boolean filled, String contents, int x, int y, int width, int height, int arcWidth, int arcHeight) {
                x = onWidthBounds(x, width);
                y = onHeightBounds(y, height);
                final Renderable renderable = new Renderable();
                renderable.setType(type);
                renderable.setColor(color);
                renderable.setX(x + width / 2);
                renderable.setY(y + height / 2);
                renderable.setWidth(width);
                renderable.setHeight(height);
                if (contents != null)
                    renderable.setSpecific(contents + '\1' + font.getGenName());
                renderable.setArcWidth(arcWidth);
                renderable.setArcHeight(arcHeight);
                renderable.setThickness(thickness);
                renderable.setzIndex(zIndex);
                renderable.setFilled(filled);
                renderable.setRotation(rotation);
                renderables.add(renderable);
            }

            @Override
            public void drawRect(int x, int y, int width, int height) {
                drawShape(RenderableType.RECTANGLE, false, null, x, y, width, height, 0, 0);
            }

            @Override
            public void drawOval(int x, int y, int width, int height) {
                drawShape(RenderableType.OVAL, false, null, x, y, width, height, 0, 0);
            }

            @Override
            public void fillRect(int x, int y, int width, int height) {
                drawShape(RenderableType.RECTANGLE, true, null, x, y, width, height, 0, 0);
            }

            @Override
            public void fillOval(int x, int y, int width, int height) {
                drawShape(RenderableType.OVAL, true, null, x, y, width, height, 0, 0);
            }

            @Override
            public void drawLine(int x1, int y1, int x2, int y2) {
                drawShape(RenderableType.LINE, false, null, x1, y1, x2, y2, 0, 0);
            }

            @Override
            public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
                drawShape(RenderableType.ROUNDED_RECTANGLE, false, null, x, y, width, height, arcWidth, arcHeight);
            }

            @Override
            public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
                drawShape(RenderableType.ROUNDED_RECTANGLE, true, null, x, y, width, height, arcWidth, arcHeight);
            }

            @Override
            public void drawString(String text, int x, int y) {
                drawShape(RenderableType.TEXT, false, text, x, y, fontMetrics.stringWidth(text), fontMetrics.getHeight(), 0, 0);
            }

            @Override
            public UXFontMetrics getFontMetrics() {
                return fontMetrics;
            }

            @Override
            public void setColor(Color color) {
                this.color = color;
            }

            @Override
            public Color getColor() {
                return color;
            }

            @Override
            public void setFont(UXFont font) {
                if (font == null)
                    fontMetrics = null;
                else {
                    if (fontMetrics == null)
                        fontMetrics = new UXFontMetrics();
                    fontMetrics.setFont(font);
                    fontMetrics.setRenderMaster(renderMaster);
                }
                this.font = font;
            }

            @Override
            public UXFont getFont() {
                return font;
            }

            @Override
            public void setThickness(int thickness) {
                this.thickness = thickness;
            }

            @Override
            public int getThickness() {
                return thickness;
            }

            @Override
            public void setZIndex(int zIndex) {
                this.zIndex = zIndex;
            }

            @Override
            public int getZIndex() {
                return zIndex;
            }

            @Override
            public void setRotation(int rotation) {
                this.rotation = rotation;
            }

            @Override
            public int getRotation() {
                return rotation;
            }
        };
        graphics.setFont(new UXFont("Roboto-Medium", 30f, 0, new RelativeFile("res/com/xebisco/yield/assets/Roboto-Medium.ttf"), renderMaster));
        update(graphics);
    }

    public RenderMaster getRenderMaster() {
        return renderMaster;
    }

    public void setRenderMaster(RenderMaster renderMaster) {
        this.renderMaster = renderMaster;
    }

    public Set<Renderable> getRenderables() {
        return renderables;
    }
}
