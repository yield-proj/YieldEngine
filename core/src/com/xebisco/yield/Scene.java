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

import java.util.HashSet;
import java.util.Set;

/**
 * The {@code Scene} class represents a scene in a 2D environment. It can contain various
 * systems and properties for managing an application state.
 */
public abstract class Scene extends Entity2DContainer {
    private final Transform2D camera = new Transform2D();
    private Color backGroundColor = Colors.GRAY.darker();
    private Set<SystemBehavior> systems = new HashSet<>();

    /**
     * Constructs a new {@link Scene} with the given application.
     *
     * @param application The application that the scene belongs to.
     */
    public Scene(Application application) {
        super(application);
    }

    @Override
    public void onUpdate(ContextTime time) {
        super.onUpdate(time);
        systems.forEach(s -> s.setScene(this).tick(time));
    }

    /**
     * Returns a specific {@link SystemBehavior} object from the scene.
     *
     * @param <T> The type of the SystemBehavior object.
     * @param systemType The class of the SystemBehavior object to retrieve.
     * @param index The index of the SystemBehavior object to retrieve.
     * @return The specified {@link SystemBehavior} object, or null if not found.
     */
    public <T extends SystemBehavior> T system(Class<T> systemType, int index) {
        int i = 0;
        for (SystemBehavior c : systems) {
            if (c.getClass().hashCode() == systemType.hashCode() && c.getClass().equals(systemType)) {
                if (index == i) {
                    //noinspection unchecked
                    return (T) c;
                }
                i++;
            }
        }
        return null;
    }

    /**
     * Returns the first found {@link SystemBehavior} object from the scene.
     *
     * @param <T> The type of the {@link SystemBehavior} object.
     * @param systemType The class of the {@link SystemBehavior} object to retrieve.
     * @return The specified {@link SystemBehavior} object, or null if not found.
     */
    public <T extends SystemBehavior> T system(Class<T> systemType) {
        return system(systemType, 0);
    }

    /**
     * Returns the background color of the scene.
     *
     * @return The background color of the scene.
     */
    public Color backGroundColor() {
        return backGroundColor;
    }

    /**
     * Sets the background color of the scene.
     *
     * @param backGroundColor The new background color for the scene.
     * @return This {@link Scene} instance for method chaining.
     */
    public Scene setBackGroundColor(Color backGroundColor) {
        this.backGroundColor = backGroundColor;
        return this;
    }


    public Set<SystemBehavior> systems() {
        return systems;
    }

    /**
     * Sets the {@link SystemBehavior} objects in the scene.
     *
     * @param systems The new set of {@link SystemBehavior} objects for the scene.
     * @return This {@link Scene} instance for method chaining.
     */
    public Scene setSystems(Set<SystemBehavior> systems) {
        this.systems = systems;
        return this;
    }

    /**
     * Retrieves the camera transform of the scene.
     *
     * @return The camera transform of the scene.
     */
    public Transform2D camera() {
        return camera;
    }
}
