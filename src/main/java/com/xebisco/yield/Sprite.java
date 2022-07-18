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

public class Sprite extends NonFillShape {

    private boolean smartRender;
    private boolean considerSelfTransform;

    @Override
    public void render(SampleGraphics graphics) {
        super.render(graphics);
        Vector2 pos, size = getSize().mul(getEntity().getTransform().scale);

        if (considerSelfTransform)
            pos = getEntity().getSelfTransform().position.subt(scene.getView().getTransform().position);
        else pos = getEntity().getTransform().position.subt(scene.getView().getTransform().position);

        if (smartRender) {
            if (pos.x + size.x / 2f >= 0 && pos.x - size.x / 2f <= scene.getView().getWidth() && pos.y + size.y / 2f >= 0 && pos.y - size.y / 2f <= scene.getView().getHeight()) {
                Texture tex = getMaterial().getTexture();
                if (tex == null)
                    tex = game.getYieldLogo();
                graphics.drawTexture(tex, pos, size);
            }
        } else {
            Texture tex = getMaterial().getTexture();
            if (tex == null)
                tex = game.getYieldLogo();
            graphics.drawTexture(tex, pos, size);
        }
    }

    @Override
    public boolean colliding(float x, float y) {
        return x >= getTransform().position.x - getSize().x * getTransform().scale.x / 2f && x <= getTransform().position.x + getSize().x * getTransform().scale.y / 2f &&
                y >= getTransform().position.y - getSize().y * getTransform().scale.y / 2f && y <= getTransform().position.y + getSize().y * getTransform().scale.y / 2f;
    }

    public boolean isConsiderSelfTransform() {
        return considerSelfTransform;
    }

    public void setConsiderSelfTransform(boolean considerSelfTransform) {
        this.considerSelfTransform = considerSelfTransform;
    }

    public boolean isSmartRender() {
        return smartRender;
    }

    public void setSmartRender(boolean smartRender) {
        this.smartRender = smartRender;
    }
}
