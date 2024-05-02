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

import java.io.IOException;

/**
 * An abstract class representing a system. It contains methods for managing scenes.
 */
public abstract class SystemBehavior extends AbstractBehavior {
    private Scene scene;

    @Override
    public void onStart() {

    }

    @Override
    public void onUpdate(ContextTime time) {

    }

    @Override
    public void close() throws IOException {

    }

    /**
     * Returns the current scene reference.
     *
     * @return The current scene reference.
     */
    public Scene scene() {
        return scene;
    }

    /**
     * This function sets the scene for this {@link SystemBehavior}. It does not change the application's scene, to change the scene, use the {@link Application#setScene} method.
     *
     * @param scene The scene reference to be set.
     * @return The current instance of SystemBehavior for method chaining.
     */
    public SystemBehavior setScene(Scene scene) {
        this.scene = scene;
        return this;
    }
}
