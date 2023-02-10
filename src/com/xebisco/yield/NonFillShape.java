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

import java.util.Set;

/**
 * It's a shape that can't be filled.
 */
public abstract class NonFillShape extends SimpleRenderable implements Spacial {
    private Vector2 size = new Vector2(64, 64);

    protected Vector2 drawPosition, drawSize, offset = new Vector2();
    private int thickness = 1;

    @Override
    public void render(Set<Renderable> renderables) {
        super.render(renderables);
        Transform t = getEntity().getTransform();
        drawSize = getSize().mul(t.scale);
        if (isIgnoreViewPosition()) {
            drawPosition = t.position;
        } else {
            drawPosition = t.position.subt(scene.getView().getTransform().position);
        }
        drawPosition = drawPosition.sum(offset);
        getRenderable().setX((int) drawPosition.x);
        getRenderable().setY((int) drawPosition.y);
        getRenderable().setWidth((int) drawSize.x);
        getRenderable().setHeight((int) drawSize.y);
        getRenderable().setThickness(thickness);
    }

    /**
     * Sets the size of the object to the size of the texture.
     *
     * @param texture The texture to use for the sprite.
     */
    public void setSizeAsTexture(Texture texture) {
        size = texture.getSize().get();
    }

    /**
     * This function returns the size of the object.
     *
     * @return The size of the object.
     */
    public Vector2 getSize() {
        return size;
    }

    /**
     * This function sets the size of the object to the size passed in.
     *
     * @param size The size of the object.
     */
    public void setSize(Vector2 size) {
        this.size = size;
    }

    /**
     * This function returns the drawPosition variable.
     *
     * @return The drawPosition variable.
     */
    public Vector2 getDrawPosition() {
        return drawPosition;
    }

    /**
     * This function sets the draw position of the object to the given draw position.
     *
     * @param drawPosition The position of the object on the screen.
     */
    public void setDrawPosition(Vector2 drawPosition) {
        this.drawPosition = drawPosition;
    }

    /**
     * This function returns the size of the object when it is drawn on the screen.
     *
     * @return The drawSize variable is being returned.
     */
    public Vector2 getDrawSize() {
        return drawSize;
    }

    /**
     * This function sets the draw size of the object to the given draw size.
     *
     * @param drawSize The size of the object to be drawn.
     */
    public void setDrawSize(Vector2 drawSize) {
        this.drawSize = drawSize;
    }

    /**
     * This function returns the offset of the current object.
     *
     * @return The offset of the camera.
     */
    public Vector2 getOffset() {
        return offset;
    }

    /**
     * This function sets the offset of the sprite.
     *
     * @param offset The offset of the sprite from the center of the entity.
     */
    public void setOffset(Vector2 offset) {
        this.offset = offset;
    }

    public int getThickness() {
        return thickness;
    }

    public void setThickness(int thickness) {
        this.thickness = thickness;
    }
}
