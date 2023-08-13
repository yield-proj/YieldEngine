/*
 * Copyright [2022-2023] [Xebisco]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xebisco.yield;

/**
 * This is a class that extends the Rectangle class and adds functionality for rendering textures with options for
 * scaling and processing pixels.
 */
@ComponentIcon(iconType = ComponentIconType.GRAPHICAL)
public class TextureRectangle extends Rectangle {
    @VisibleOnEditor
    private Texture texture;

    @VisibleOnEditor
    private boolean textureSized;

    @VisibleOnEditor
    private boolean disposeTexture;

    @VisibleOnEditor
    private Vector2D textureScale = null;

    @Override
    public void onStart() {
        super.onStart();
        if (texture == null) texture = getApplication().getDefaultTexture();
    }

    @Override
    public void setupX(float[] verticesX) {
        super.setupX(verticesX);
        drawInstruction().setImageRef(texture.getImageRef());
    }

    @Override
    public DrawInstruction render() {
        if (textureSized) {
            if (textureScale == null)
                size().set(texture.getSize());
            else
                size().set(texture.getSize().multiply(textureScale));
        }
        return super.render();
    }

    @Override
    public void dispose() {
        if (disposeTexture && texture != null) texture.dispose();
    }

    /**
     * The function returns a texture object.
     *
     * @return The method is returning a Texture object.
     */
    public Texture getTexture() {
        return texture;
    }

    /**
     * This function sets the texture of this object.
     *
     * @param texture The "texture" parameter is an object of the class "Texture". The method "setTexture" sets the value
     * of the instance variable "texture" to the value passed as the parameter.
     */
    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    /**
     * The function returns a boolean value indicating whether the TextureRectangle is texture sized or not.
     *
     * @return A boolean value indicating whether the TextureRectangle is texture sized or not.
     */
    public boolean isTextureSized() {
        return textureSized;
    }

    /**
     * This function sets the value of the textureSized variable.
     *
     * @param textureSized A boolean variable that sets whether the TextureRectangle is texture sized or not.
     */
    public void setTextureSized(boolean textureSized) {
        this.textureSized = textureSized;
    }

    /**
     * This function returns a TwoAnchorRepresentation object representing the texture scale.
     *
     * @return A variable of type `TwoAnchorRepresentation` named `textureScale` is being returned.
     */
    public Vector2D getTextureScale() {
        return textureScale;
    }

    /**
     * This function sets the texture scale of a TwoAnchorRepresentation object.
     *
     * @param textureScale The parameter texture scale of a TwoAnchorRepresentation object to set.
     */
    public void setTextureScale(Vector2D textureScale) {
        this.textureScale = textureScale;
    }

    /**
     * The function returns a boolean value indicating whether the texture should be disposed.
     *
     * @return The method is returning a boolean value, specifically the value of the variable `disposeTexture`.
     */
    public boolean isDisposeTexture() {
        return disposeTexture;
    }

    /**
     * This function sets a boolean value to determine whether a texture should be disposed or not.
     *
     * @param disposeTexture disposeTexture is a boolean parameter that determines whether the texture associated with an
     * object should be disposed of when the object is no longer needed. If disposeTexture is set to true, the texture will
     * be disposed of, freeing up memory. If it is set to false, the texture will not be disposed of
     */
    public void setDisposeTexture(boolean disposeTexture) {
        this.disposeTexture = disposeTexture;
    }
}
