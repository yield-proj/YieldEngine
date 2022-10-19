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
import com.xebisco.yield.Vector;
import com.xebisco.yield.Vector2;

public class UXText extends UXComponent {

    private String contents, font;
    private Vector2 position, scale = new Vector2(1, 1);
    private Color color;

    private boolean middle;

    public UXText(Boolean middle, String contents, Vector2 position, UXMain uxMain) {
        super(uxMain);
        this.position = position;
        this.contents = contents;
        color = uxMain.getPalette().text1;
        font = uxMain.getPalette().font1;
        this.middle = middle;
    }

    @Override
    public void render(SampleGraphics graphics, float delta) {
        Vector2 pos = getPosition().get(), d2size = new Vector2(graphics.getStringWidth(contents, font) / 2f, graphics.getStringHeight(contents, font) / 2f);
        if (middle) {
            setPosition(new Vector2(getPanel().getSize().x / 2f + getPosition().x, getPanel().getSize().y / 2f + getPosition().y));
        } else {
            if (getPosition().x < 0)
                getPosition().x = getPanel().getSize().x + getPosition().x - d2size.x * 2f;
            if (getPosition().y < 0)
                getPosition().y = getPanel().getSize().y + getPosition().y - d2size.y * 2f;
        }
        graphics.drawString(contents, color, position.sum(d2size), scale, font);
        setPosition(pos.get());
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

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public Vector2 getScale() {
        return scale;
    }

    public void setScale(Vector2 scale) {
        this.scale = scale;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isMiddle() {
        return middle;
    }

    public void setMiddle(boolean middle) {
        this.middle = middle;
    }
}
