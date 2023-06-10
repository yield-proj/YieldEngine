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

package com.xebisco.yield.editor;

import java.awt.*;
import java.io.Serializable;

public class EditorScene implements Serializable {
    private String name;
    private Color backgroundColor = Color.GRAY.darker();
    private Dimension viewSceneSize = new Dimension(10000, 10000);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Dimension getViewSceneSize() {
        return viewSceneSize;
    }

    public void setViewSceneSize(Dimension viewSceneSize) {
        this.viewSceneSize = viewSceneSize;
    }
}
