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
 * It's a behavior that draws a rectangle
 */
@ComponentIcon(iconType = ComponentIconType.GRAPHICAL)
public class Rectangle extends AbstractRenderable {
    @Override
    public int verticesCount() {
        return 4;
    }

    @Override
    public void setupX(float[] verticesX) {
        verticesX[0] = (float) (-size().getWidth() / 2);
        verticesX[1] = (float) (size().getWidth() / 2.);
        verticesX[2] = (float) (size().getWidth() / 2.);
        verticesX[3] = (float) (-size().getWidth() / 2.);
    }

    @Override
    public void setupY(float[] verticesY) {
        verticesY[0] = (float) (-size().getHeight() / 2.);
        verticesY[1] = (float) (-size().getHeight() / 2.);
        verticesY[2] = (float) (size().getHeight() / 2.);
        verticesY[3] = (float) (size().getHeight() / 2.);
    }
}
