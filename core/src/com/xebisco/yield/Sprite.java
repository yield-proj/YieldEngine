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

import com.xebisco.yield.render.Renderable;
import com.xebisco.yield.render.RenderableType;

import java.util.Set;

/**
 * A Sprite is a NonFillShape that renders a texture
 */
public class Sprite extends NonFillShape {

    private boolean smartRender = true;

    public Sprite() {
    }

    public Sprite(Vector2 size) {
        setSize(size);
    }

    public Sprite(Texture size) {
        setSizeAsTexture(size);
    }

    @Override
    public void render(Set<Renderable> renderables) {
        super.render(renderables);
        getRenderable().setType(RenderableType.IMAGE);
        if (smartRender) {
            if (drawPosition.x + drawSize.x / 2f >= 0 && drawPosition.x - drawSize.x / 2f <= scene.getView().getWidth() && drawPosition.y + drawSize.y / 2f >= 0 && drawPosition.y - drawSize.y / 2f <= scene.getView().getHeight()) {
                Texture tex = getMaterial().getTexture();
                if (tex == null)
                    tex = game.getYieldLogo();
                getRenderable().setSpecific(getMaterial().getTexture().getSpecificImage());
            }
        } else {
            Texture tex = getMaterial().getTexture();
            if (tex == null)
                tex = game.getYieldLogo();
            getRenderable().setSpecific(getMaterial().getTexture().getSpecificImage());
        }
    }

    @Override
    public boolean colliding(float x, float y) {
        Transform t = getTransform();
        return x >= t.position.x - getSize().x * t.scale.x / 2f && x <= t.position.x + getSize().x * t.scale.y / 2f &&
                y >= t.position.y - getSize().y * t.scale.y / 2f && y <= t.position.y + getSize().y * t.scale.y / 2f;
    }

    /**
     * Returns whether the smart render feature is enabled
     *
     * @return The value of the smartRender variable.
     */
    public boolean isSmartRender() {
        return smartRender;
    }

    /**
     * Sets the smartRender property
     *
     * @param smartRender The smartRender value to set.
     */
    public void setSmartRender(boolean smartRender) {
        this.smartRender = smartRender;
    }
}
