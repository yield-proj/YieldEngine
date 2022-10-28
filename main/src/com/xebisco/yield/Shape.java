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

import java.util.TreeSet;

/**
 * Shape is a NonFillShape that can be filled.
 */
public abstract class Shape extends NonFillShape {
    private boolean filled = true;

    @Override
    public void render(TreeSet<Renderable> renderables) {
        super.render(renderables);
        getRenderable().setFilled(filled);
    }

    /**
     * This function returns the value of the filled variable.
     *
     * @return The boolean value of the filled variable.
     */
    public boolean isFilled() {
        return filled;
    }

    /**
     * This function sets the value of the filled variable to the value of the filled parameter.
     *
     * @param filled a boolean value that determines whether the shape is filled or not.
     */
    public void setFilled(boolean filled) {
        this.filled = filled;
    }
}
