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

import com.xebisco.yield.*;

public class UXButton extends UXRect {

    private Color textColor, selectedTextColor, selectedBackground;
    private String contents, font;
    private YldAction action;
    private float clickAnimation;

    private boolean middle;

    public UXButton(Boolean middle, String contents, Vector2 position, Vector2 size, UXMain uxMain) {
        super(position, size, uxMain);
        this.middle = middle;
        textColor = uxMain.getPalette().text1;
        selectedTextColor = uxMain.getPalette().text2;
        selectedBackground = uxMain.getPalette().foreground1;
        setBackground(uxMain.getPalette().foreground2);
        this.contents = contents;
        font = uxMain.getPalette().font1;
    }

    @Override
    public void render(SampleGraphics graphics, float delta) {
        Color textColor = getTextColor();
        Color bkg = getBackground();
        Vector2 pos = getPosition().get();
        if (middle) {
            setPosition(new Vector2(getPanel().getSize().x / 2f + getPosition().x, getPanel().getSize().y / 2f + getPosition().y));
        } else {
            if (getPosition().x < 0)
                getPosition().x = getPanel().getSize().x + getPosition().x - getSize().x;
            if (getPosition().y < 0)
                getPosition().y = getPanel().getSize().y + getPosition().y - getSize().y;
        }
        if (clickAnimation > 0f)
            clickAnimation += delta;
        if (getUxMain().getMouse().x >= getPosition().x && getUxMain().getMouse().x <= getPosition().x + getSize().x
                && getUxMain().getMouse().y >= getPosition().y && getUxMain().getMouse().y <= getPosition().y + getSize().y) {
            textColor = getSelectedTextColor();
            setBackground(selectedBackground);
            if (getUxMain().isJustPressedLeftMouse() && clickAnimation == 0) {
                clickAnimation += delta;
            }
        }
        if (clickAnimation > 0) {
            Color c = selectedBackground.get();
            c.setA(1 - clickAnimation * 2f);
            graphics.drawRoundRect(getPosition().sum(getSize().div(2)), getSize().sum(clickAnimation * 100), c, false, getArcWidth(), getArcHeight());
            if (c.getA() == 0) {
                clickAnimation = 0;
                if (action != null) action.onAction();
            }
        }
        super.render(graphics, delta);
        graphics.drawString(contents, textColor, getPosition().sum(getSize().div(2)), new Vector2(1, 1), font);
        setBackground(bkg);
        setPosition(pos.get());
    }

    public Color getTextColor() {
        return textColor;
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }

    public Color getSelectedTextColor() {
        return selectedTextColor;
    }

    public void setSelectedTextColor(Color selectedTextColor) {
        this.selectedTextColor = selectedTextColor;
    }

    public Color getSelectedBackground() {
        return selectedBackground;
    }

    public void setSelectedBackground(Color selectedBackground) {
        this.selectedBackground = selectedBackground;
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

    public YldAction getAction() {
        return action;
    }

    public void setAction(YldAction action) {
        this.action = action;
    }

    public float getClickAnimation() {
        return clickAnimation;
    }

    public void setClickAnimation(float clickAnimation) {
        this.clickAnimation = clickAnimation;
    }

    public boolean isMiddle() {
        return middle;
    }

    public void setMiddle(boolean middle) {
        this.middle = middle;
    }
}
