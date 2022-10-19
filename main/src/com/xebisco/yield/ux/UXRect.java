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
import com.xebisco.yield.SampleGraphics;
import com.xebisco.yield.Vector2;

public class UXRect extends UXComponent {
    private int arcWidth = 30, arcHeight = 30;
    private boolean filled = true;
    private Color background;

    public UXRect(Vector2 position, Vector2 size, UXMain uxMain) {
        super(uxMain);
        setPosition(position);
        setSize(size);
        background = getUxMain().getPalette().foreground1;
    }

    @Override
    public void render(SampleGraphics graphics, float delta) {
        graphics.drawRoundRect(getPosition().sum(getSize().div(2f)), getSize(), background, filled, arcWidth, arcHeight);
    }

    public int getArcWidth() {
        return arcWidth;
    }

    public void setArcWidth(int arcWidth) {
        this.arcWidth = arcWidth;
    }

    public int getArcHeight() {
        return arcHeight;
    }

    public void setArcHeight(int arcHeight) {
        this.arcHeight = arcHeight;
    }

    public boolean isFilled() {
        return filled;
    }

    public void setFilled(boolean filled) {
        this.filled = filled;
    }

    public Color getBackground() {
        return background;
    }

    public void setBackground(Color background) {
        this.background = background;
    }
}
