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
 * It draws a line from the top left corner of the shape to the bottom right corner of the shape
 */
public class Line extends NonFillShape {
    @Override
    public void render(SampleGraphics graphics) {
        graphics.drawLine(drawPosition, drawPosition.sum(drawSize), getColor());
    }

    @Override
    public boolean colliding(float x, float y) {
        Transform t = getTransform();
        return x >= t.position.x - getSize().x * t.scale.x / 2f && x <= t.position.x + getSize().x * t.scale.y / 2f &&
                y >= t.position.y - getSize().y * t.scale.y / 2f && y <= t.position.y + getSize().y * t.scale.y / 2f;
    }
}
