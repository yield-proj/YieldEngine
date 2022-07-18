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

public abstract class NonFillShape extends SimpleRenderable implements Spacial {
    private Vector2 size = new Vector2(64, 64);

    protected Vector2 drawPosition, drawSize;

    @Override
    public void render(SampleGraphics graphics) {
        drawPosition = getEntity().getTransform().position;
        drawSize = getSize().mul(getEntity().getTransform().scale);
        if(isIgnoreViewPosition()) {
            drawPosition = drawPosition.subt(scene.getView().getTransform().position);
        }
    }

    public void setSizeAsTexture(Texture texture) {
        size = texture.getSize().get();
    }

    public Vector2 getSize() {
        return size;
    }

    public void setSize(Vector2 size) {
        this.size = size;
    }

    public Vector2 getDrawPosition() {
        return drawPosition;
    }

    public void setDrawPosition(Vector2 drawPosition) {
        this.drawPosition = drawPosition;
    }

    public Vector2 getDrawSize() {
        return drawSize;
    }

    public void setDrawSize(Vector2 drawSize) {
        this.drawSize = drawSize;
    }
}
