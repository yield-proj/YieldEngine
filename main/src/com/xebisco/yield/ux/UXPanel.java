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

import com.xebisco.yield.Color;

import java.awt.*;
import java.util.ConcurrentModificationException;

public class UXPanel extends UXCompartment {
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

    public void repaint() {
        UXGraphics graphics = new UXGraphics() {
            @Override
            public void drawRect(int x, int y, int width, int height) {

            }

            @Override
            public void drawOval(int x, int y, int width, int height) {

            }

            @Override
            public void fillRect(int x, int y, int width, int height) {

            }

            @Override
            public void fillOval(int x, int y, int width, int height) {

            }

            @Override
            public void drawLine(int x1, int y1, int x2, int y2) {

            }

            @Override
            public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {

            }

            @Override
            public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {

            }

            @Override
            public void drawString(String text, int x, int y) {

            }

            @Override
            public UXFontMetrics getFontMetrics() {
                return null;
            }

            @Override
            public void setColor(Color color) {

            }

            @Override
            public Color getColor() {
                return null;
            }

            @Override
            public void setFont(UXFont font) {

            }

            @Override
            public UXFont getFont() {
                return null;
            }
        };
    }
}
