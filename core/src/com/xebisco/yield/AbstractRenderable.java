/*
 * Copyright [2022-2024] [Xebisco]
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

import com.xebisco.yield.rendering.Form;
import com.xebisco.yield.rendering.Paint;
import com.xebisco.yield.rendering.Renderer;

/**
 * Abstract class representing a renderable component in the application.
 * This class provides methods for rendering the component using a provided renderer.
 */
public abstract class AbstractRenderable extends ComponentBehavior {

    private final Form form;

    private final Paint paint = new Paint();

    /**
     * Constructs a new AbstractRenderable with the given form.
     *
     * @param form The form of the renderable component.
     */
    public AbstractRenderable(Form form) {
        this.form = form;
    }

    @Override
    public void render(Renderer renderer) {
        paint.setTransformation(entity().hierarchyTransform());
        renderer.draw(form, paint, this);
    }

    /**
     * Returns the form of the renderable component.
     *
     * @return The form of the renderable component.
     */
    public Form form() {
        return form;
    }

    /**
     * Returns the paint object used for rendering the component.
     *
     * @return The paint object used for rendering the component.
     */
    public Paint paint() {
        return paint;
    }
}
