/*
 * Copyright [2022-2023] [Xebisco]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.xebisco.yield;

import java.lang.reflect.InvocationTargetException;

public interface PlatformGraphics extends Disposable {

    static PlatformGraphics swingGraphics() throws ClassNotFoundException {
        //noinspection unchecked
        Class<? extends PlatformGraphics> swingGraphicsImplClass = (Class<? extends PlatformGraphics>) Class.forName("com.xebisco.yield.swingimpl.SwingPlatformGraphics");
        try {
            return swingGraphicsImplClass.getConstructor().newInstance();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    void init(PlatformInit platformInit);
    void frame();
    void draw(DrawInstruction drawInstruction);
    void resetRotation();
    boolean shouldClose();
    void conclude();
}
