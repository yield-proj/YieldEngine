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

/**
 * The Entity2DPrefab class is a blueprint for creating 2D entities with components, children, and tags.
 */
public class Entity2DPrefab {
    private final ComponentCreation[] components;
    private final Entity2DPrefab[] children;
    private final String[] tags;

    /**
     * The constructor that initializes the {@link Entity2DPrefab} with children and components.
     *
     * @param children The array of {@link Entity2DPrefab} objects representing the children of the entity.
     * @param components The array of {@link ComponentCreation} objects representing the components of the entity.
     */
    public Entity2DPrefab(Entity2DPrefab[] children, ComponentCreation... components) {
        this(children, new String[0], components);
    }

    /**
     * The constructor that initializes the {@link Entity2DPrefab} with children, components, and tags.
     *
     * @param children The array of {@link Entity2DPrefab} objects representing the children of the entity.
     * @param tags      The array of strings representing the tags of the entity.
     * @param components The array of {@link ComponentCreation} objects representing the components of the entity.
     */
    public Entity2DPrefab(Entity2DPrefab[] children, String[] tags, ComponentCreation... components) {
        this.components = components;
        this.children = children;
        this.tags = tags;
    }

    public Entity2DPrefab(ComponentCreation... components) {
        this(new Entity2DPrefab[0], new String[0], components);
    }

    /**
     * The constructor that initializes the {@link Entity2DPrefab} with components.
     *
     * @param components The array of ComponentCreation objects representing the components of the entity.
     */
    public Entity2DPrefab(String[] tags, ComponentCreation... components) {
        this(new Entity2DPrefab[0], tags, components);
    }

    /**
     * The function returns an array of {@link ComponentCreation} objects.
     *
     * @return An array of ComponentCreation objects representing the components of the entity.
     */
    public ComponentCreation[] components() {
        return components;
    }

    /**
     * The function returns an array of 2D entity prefabs that are children of a parent entity.
     *
     * @return An array of {@link Entity2DPrefab} objects representing the children of the entity.
     */
    public Entity2DPrefab[] children() {
        return children;
    }

    /**
     * The function returns an array of strings representing tags.
     *
     * @return An array of strings representing the tags of the entity.
     */
    public String[] tags() {
        return tags;
    }
}
