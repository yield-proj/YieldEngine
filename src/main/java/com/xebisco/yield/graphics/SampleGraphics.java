/*
 * Copyright [2022] [Xebisco]
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

package com.xebisco.yield.graphics;

import com.xebisco.yield.Color;
import com.xebisco.yield.Texture;
import com.xebisco.yield.slick.SlickGame;

import java.awt.*;

public interface SampleGraphics
{
    Color getColor();

    void setColor(Color c);

    Font getFont();

    void setFont(Font font);

    void drawLine(int x1, int y1, int x2, int y2);

    void fillRect(int x, int y, int width, int height);

    void drawRect(int x, int y, int width, int height);

    void clearRect(int x, int y, int width, int height);

    void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight);

    void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight);

    void drawOval(int x, int y, int width, int height);

    void fillOval(int x, int y, int width, int height);

    void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle);

    void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle);

    void drawTexture(Texture texture, int x, int y, int width, int height);

    void drawString(String str, int x, int y);

    void dispose();
}
