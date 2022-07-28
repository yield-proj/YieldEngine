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

public class YldGraphics {
    private Entity e;
    private Color color = Colors.CYAN;
    private YldB toConcurrent;
    private String font = "arial";

    public YldGraphics(Entity e, YldB yldB) {
        this.e = e;
        this.toConcurrent = yldB;
    }

    /**
     * Returns an empty rectangle entity.
     *
     * @return The created rectangle.
     */
    public Entity sample() {
        return rect(0, 0, 0, 0, false);
    }

    /**
     * Creates a rectangle entity.
     *
     * @param x      The x position of the rectangle
     * @param y      The y position of the rectangle
     * @param width  The width of the rectangle
     * @param height The height of the rectangle
     * @param filled If the rectangle is filled.
     * @return The created rectangle.
     */
    public Entity rect(float x, float y, float width, float height, boolean filled) {
        return e.instantiate((e) -> {
            Shape r = new Rectangle();
            e.addComponent(r);
            r.setSize(new Vector2(width, height));
            r.setFilled(filled);
            r.setColor(color);
            e.getSelfTransform().goTo(new Vector2(x, y));
        }, toConcurrent);
    }

    /**
     * Creates a round rectangle entity.
     *
     * @param x      The x position of the rectangle
     * @param y      The y position of the rectangle
     * @param width  The width of the rectangle
     * @param height The height of the rectangle
     * @param filled If the rectangle is filled.
     * @param arc The arc size of the rectangle.
     * @return The created rectangle.
     */
    public Entity roundRect(float x, float y, float width, float height, boolean filled, Vector2 arc) {
        return e.instantiate((e) -> {
            RoundedRectangle r = new RoundedRectangle();
            e.addComponent(r);
            r.setArc(arc);
            r.setSize(new Vector2(width, height));
            r.setFilled(filled);
            r.setColor(color);
            e.getSelfTransform().goTo(new Vector2(x, y));
        }, toConcurrent);
    }

    /**
     * Creates a round rectangle entity.
     *
     * @param x      The x position of the rectangle
     * @param y      The y position of the rectangle
     * @param width  The width of the rectangle
     * @param height The height of the rectangle
     * @param arc The arc size of the rectangle.
     * @return The created rectangle.
     */
    public Entity roundRect(float x, float y, float width, float height, Vector2 arc) {
        return roundRect(x, y, width, height, true, arc);
    }

    /**
     * Creates a round rectangle entity.
     *
     * @param x      The x position of the rectangle
     * @param y      The y position of the rectangle
     * @param width  The width of the rectangle
     * @param height The height of the rectangle
     * @return The created rectangle.
     */
    public Entity roundRect(float x, float y, float width, float height) {
        return roundRect(x, y, width, height, true, new Vector2(20, 20));
    }

    /**
     * Creates a round rectangle entity.
     *
     * @param x      The x position of the rectangle
     * @param y      The y position of the rectangle
     * @param width  The width of the rectangle
     * @param height The height of the rectangle
     * @return The created rectangle.
     */
    public Entity roundRect(float x, float y, float width, float height, boolean filled) {
        return roundRect(x, y, width, height, filled, new Vector2(20, 20));
    }

    /**
     * Creates an oval entity.
     *
     * @param x      The x position of the oval
     * @param y      The y position of the oval
     * @param width  The width of the oval
     * @param height The height of the oval
     * @param filled If the oval is filled.
     * @return The created oval.
     */
    public Entity oval(float x, float y, float width, float height, boolean filled) {
        return e.instantiate((e) -> {
            Shape r = new Oval();
            e.addComponent(r);
            r.setSize(new Vector2(width, height));
            r.setFilled(filled);
            r.setColor(color);
            e.getSelfTransform().goTo(new Vector2(x, y));
        }, toConcurrent);
    }

    /**
     * Creates an oval entity.
     *
     * @param width  The width of the oval
     * @param height The height of the oval
     * @return The created oval.
     */
    public Entity oval(float width, float height) {
        Entity e = oval(0, 0, width, height);
        e.center();
        return e;
    }

    /**
     * Creates a rectangle entity.
     *
     * @param width  The width of the oval
     * @param height The height of the oval
     * @return The created rect.
     */
    public Entity rect(float width, float height) {
        Entity e = rect(0, 0, width, height);
        e.center();
        return e;
    }

    /**
     * Creates an image entity.
     *
     * @param width  The width of the oval
     * @param height The height of the oval
     * @return The created image.
     */
    public Entity img(Texture texture, float width, float height) {
        Entity e = img(texture, 0, 0, width, height);
        e.center();
        return e;
    }


    /**
     * Creates a pixel entity.
     *
     * @param x The x position of the pixel.
     * @param y The y position of the pixel.
     * @return The created pixel.
     */
    public Entity tint(float x, float y) {
        return rect(x, y, 1, 1, true);
    }

    /**
     * Create a line entity.
     *
     * @param x1 The x1 position of the line.
     * @param y1 The y1 position of the line.
     * @param x2 The x2 position of the line.
     * @param y2 The y2 position of the line.
     * @return The created line.
     */
    public Entity line(float x1, float y1, float x2, float y2) {
        return e.instantiate((e) -> {
            Line r = new Line();
            r.setSize(new Vector2(x2, y2));
            e.addComponent(r);
            r.setColor(color);
            e.getSelfTransform().goTo(new Vector2(x2 / 2, y2 / 2));
        }, toConcurrent);
    }

    /**
     * Creates a text entity.
     *
     * @param text The contents of the text.
     * @param x    The x position of the text.
     * @param y    The y position of the text.
     * @return The created text.
     */
    public Entity text(String text, float x, float y) {
        return e.instantiate((e) -> {
            Text r = new Text();
            e.addComponent(r);
            r.setContents(text);
            r.setFont(font);
            r.setColor(color);
            e.getSelfTransform().goTo(new Vector2(x, y));
        }, toConcurrent);
    }

    /**
     * Creates a text entity.
     *
     * @param text The contents of the text.
     * @return The created text.
     */
    public Entity text(String text) {
        return text(text, 0, 0);
    }

    /**
     * Creates an image entity.
     *
     * @param texture the texture of the image.
     * @param x       The x position of the rectangle
     * @param y       The y position of the rectangle
     * @param width   The width of the rectangle
     * @param height  The height of the rectangle
     * @return The created image entity.
     */
    public Entity img(Texture texture, float x, float y, float width, float height) {
        return e.instantiate((e) -> {
            NonFillShape r = new Sprite();
            e.addComponent(r);
            r.setSize(new Vector2(width, height));
            r.setColor(color);
            e.getMaterial().setTexture(texture);
            e.getSelfTransform().goTo(new Vector2(x, y));
        }, toConcurrent);
    }

    /**
     * Creates an image entity.
     *
     * @param texture the texture of the image.
     * @return The created image entity.
     */
    public Entity img(Texture texture) {
        return e.instantiate((e) -> {
            NonFillShape r = new Sprite();
            e.addComponent(r);
            r.setSizeAsTexture(texture);
            r.setColor(color);
            e.getMaterial().setTexture(texture);
            e.getSelfTransform().goTo(0, 0);
        }, toConcurrent);
    }

    /**
     * Creates a filled rectangle entity.
     *
     * @param x      The x position of the rectangle
     * @param y      The y position of the rectangle
     * @param width  The width of the rectangle
     * @param height The height of the rectangle
     * @return The created rectangle.
     */
    public Entity rect(float x, float y, float width, float height) {
        return rect(x, y, width, height, true);
    }

    /**
     * Creates a oval entity.
     *
     * @param x      The x position of the oval
     * @param y      The y position of the oval
     * @param width  The width of the oval
     * @param height The height of the oval
     * @return The created oval.
     */
    public Entity oval(float x, float y, float width, float height) {
        return oval(x, y, width, height, true);
    }

    /**
     * Getter for the actual color.
     *
     * @return The actual color.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Setter for the actual color.
     * This setter will determine all the next graphical objects color.
     */
    public void setColor(Color color) {
        this.color = color;
    }

    public Entity getE() {
        return e;
    }

    public void setE(Entity e) {
        this.e = e;
    }

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public YldB getToConcurrent() {
        return toConcurrent;
    }

    public void setToConcurrent(YldB toConcurrent) {
        this.toConcurrent = toConcurrent;
    }
}
