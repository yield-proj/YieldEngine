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

import com.xebisco.yield.rendering.Form;
import com.xebisco.yield.rendering.Paint;
import com.xebisco.yield.rendering.Renderer;

@HideComponent
public abstract class AbstractRenderable extends ComponentBehavior {

    private final Form form;
    private final Paint paint = new Paint();

    public AbstractRenderable(Form form) {
        this.form = form;
    }

    @Override
    public void render(Renderer renderer) {
        paint.setTransformation(transform());
        renderer.draw(form, paint);
    }

    public Form form() {
        return form;
    }

    public Paint paint() {
        return paint;
    }
}
