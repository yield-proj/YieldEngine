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

public interface UXGraphics {
    void drawRect(int x, int y, int width, int height);
    void drawOval(int x, int y, int width, int height);
    void fillRect(int x, int y, int width, int height);
    void fillOval(int x, int y, int width, int height);
    void drawLine(int x1, int y1, int x2, int y2);
    void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight);
    void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight);
    void drawString(String text, int x, int y);
    UXFontMetrics getFontMetrics();
    void setColor(Color color);
    Color getColor();
    void setFont(UXFont font);
    UXFont getFont();
    void setThickness(int thickness);
    int getThickness();
    void setZIndex(int zIndex);
    int getZIndex();
    void setRotation(int degrees);
    int getRotation();
}
