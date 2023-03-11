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

public class Entity2DPrefab {
    private final ComponentBehavior[] components;
    private final Entity2D[] children;

    public Entity2DPrefab(Entity2D[] children, ComponentBehavior... components) {
        this.components = components;
        this.children = children;
    }

    public Entity2DPrefab(ComponentBehavior... components) {
        this(new Entity2D[0], components);
    }

    public ComponentBehavior[] getComponents() {
        return components;
    }

    public Entity2D[] getChildren() {
        return children;
    }
}
