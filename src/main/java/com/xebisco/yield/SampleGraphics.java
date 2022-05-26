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

public interface SampleGraphics
{
    void setRotation(Vector2 point, float angle);

    void drawLine(Vector2 point1, Vector2 point2, Color color);

    void drawRect(Vector2 pos, Vector2 size, Color color, boolean filled);

    void drawRoundRect(Vector2 pos, Vector2 size, Color color, boolean filled, int arcWidth, int arcHeight);

    void drawOval(Vector2 pos, Vector2 size, Color color, boolean filled);

    void drawArc(Vector2 pos, Vector2 size, Color color, boolean filled, int startAngle, int arcAngle);

    void drawString(String str, Color color, Vector2 pos, String fontName);

    void drawTexture(Texture texture, Vector2 pos, Vector2 size);

    void setFilter(Filter filter);

    void setFont(String font);

    float getStringWidth(String str);

    float getStringWidth(String str, String font);

    float getStringHeight(String str);

    float getStringHeight(String str, String font);

    void custom(String instruction, Object... args);
}
