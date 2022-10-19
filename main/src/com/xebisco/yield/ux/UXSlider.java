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

public class UXSlider extends UXComponent {

    private float progress = .8f;
    private int arcWidth = 10, arcHeight = 10;
    private Vector2 ballSize = new Vector2(40, 40);
    private boolean middle, filled = true;
    private float clickAnimation, clickAnimation2;
    private Color sliderColor, sliderBallColor, internalSliderBallColor, selectedSliderColor;

    public UXSlider(Boolean middle, Float width, Vector2 position, UXMain uxMain) {
        super(uxMain);
        this.middle = middle;
        setSize(new Vector2(width, 10));
        setPosition(position);
        sliderColor = uxMain.getPalette().foreground2;
        sliderBallColor = uxMain.getPalette().foreground2;
        selectedSliderColor = uxMain.getPalette().foreground1;
        internalSliderBallColor = uxMain.getPalette().foreground1;
    }

    @Override
    public void render(SampleGraphics graphics, float delta) {
        Vector2 pos = getPosition().get();
        if(clickAnimation > 0)
            clickAnimation += delta * 8f;
        if(clickAnimation2 > 0)
            clickAnimation2 += delta * 10f;
        if (middle) {
            setPosition(new Vector2(getPanel().getSize().x / 2f + getPosition().x, getPanel().getSize().y / 2f + getPosition().y));
        } else {
            if (getPosition().x < 0)
                getPosition().x = getPanel().getSize().x + getPosition().x - getSize().x;
            if (getPosition().y < 0)
                getPosition().y = getPanel().getSize().y + getPosition().y - getSize().y;
        }
        if (getUxMain().getMouse().x >= getPosition().x - ballSize.x / 2f && getUxMain().getMouse().x <= getPosition().x + getSize().x + ballSize.x / 2f
                && getUxMain().getMouse().y >= getPosition().y - ballSize.y / 2f && getUxMain().getMouse().y <= getPosition().y + getSize().y + ballSize.y / 2f) {
            if(getUxMain().isPressingLeftMouse()) {
                progress = Yld.clamp(getUxMain().getMouse().x - getPosition().x, 0, getSize().x) / getSize().x;
            }
            if(getUxMain().isJustPressedLeftMouse()) {
                clickAnimation += delta;
                clickAnimation2 = 0f;
            }
        }
        if(clickAnimation > .5f && clickAnimation2 == 0)
            clickAnimation2 += delta;
        if(clickAnimation2 > 3f)
            clickAnimation2 = -.0001f;
        if(clickAnimation > 3f)
            clickAnimation = 0f;
        graphics.drawRoundRect(getPosition().sum(new Vector2(getSize().x / 2f, getSize().y / 2f)), new Vector2(getSize().x, getSize().y), sliderColor, filled, arcWidth, arcHeight);
        graphics.drawRoundRect(getPosition().sum(new Vector2(progress * getSize().x / 2f, getSize().y / 2f)), new Vector2(progress * getSize().x, getSize().y), selectedSliderColor, filled, arcWidth, arcHeight);
        graphics.drawOval(new Vector2(getPosition().x + progress * getSize().x, getPosition().y + getSize().y / 2f), ballSize.mul((4 - Yld.mod(Yld.cos(clickAnimation2))) / 3f), sliderBallColor, true);
        graphics.drawOval(new Vector2(getPosition().x + progress * getSize().x, getPosition().y + getSize().y / 2f), ballSize.div(2f).mul(2 - Yld.mod(Yld.cos(clickAnimation))), internalSliderBallColor, true);
        setPosition(pos.get());
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    public boolean isMiddle() {
        return middle;
    }

    public void setMiddle(boolean middle) {
        this.middle = middle;
    }

    public boolean isFilled() {
        return filled;
    }

    public void setFilled(boolean filled) {
        this.filled = filled;
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

    public Color getSliderColor() {
        return sliderColor;
    }

    public void setSliderColor(Color sliderColor) {
        this.sliderColor = sliderColor;
    }

    public Color getSliderBallColor() {
        return sliderBallColor;
    }

    public void setSliderBallColor(Color sliderBallColor) {
        this.sliderBallColor = sliderBallColor;
    }

    public Color getInternalSliderBallColor() {
        return internalSliderBallColor;
    }

    public void setInternalSliderBallColor(Color internalSliderBallColor) {
        this.internalSliderBallColor = internalSliderBallColor;
    }

    public Color getSelectedSliderColor() {
        return selectedSliderColor;
    }

    public void setSelectedSliderColor(Color selectedSliderColor) {
        this.selectedSliderColor = selectedSliderColor;
    }

    public Vector2 getBallSize() {
        return ballSize;
    }

    public void setBallSize(Vector2 ballSize) {
        this.ballSize = ballSize;
    }
}
