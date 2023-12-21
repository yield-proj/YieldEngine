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

import java.util.HashSet;
import java.util.Set;

/**
 * The Scene class is an abstract class that extends Entity2DContainer and implements Behavior, and contains various
 * systems and properties for managing a scene.
 */
public abstract class Scene extends Entity2DContainer {
    private final Transform2D camera = new Transform2D();
    private Color backGroundColor = Colors.GRAY.darker();
    private Set<SystemBehavior> systems = new HashSet<>();

    public Scene(Application application) {
        super(application);
    }

    @Override
    public void onUpdate(ContextTime time) {
        super.onUpdate(time);
        systems.forEach(s -> s.setScene(this).tick(time));
    }

    /**
     * This function returns the background color.
     *
     * @return The method is returning the value of the `backGroundColor` variable.
     */
    public Color backGroundColor() {
        return backGroundColor;
    }

    /**
     * This function sets the background color of the scene.
     *
     * @param backGroundColor The background color to set.
     */
    public void setBackGroundColor(Color backGroundColor) {
        this.backGroundColor = backGroundColor;
    }

    /**
     * The function returns a Set of SystemBehavior objects.
     *
     * @return A Set of SystemBehavior objects is being returned.
     */
    public Set<SystemBehavior> systems() {
        return systems;
    }

    /**
     * This function sets the value of a Set of SystemBehavior objects in the current object.
     *
     * @param systems The parameter "systems" is a Set of objects of type "SystemBehavior". This method sets the value of
     * the instance variable "systems" to the value passed as the parameter.
     */
    public void setSystems(Set<SystemBehavior> systems) {
        this.systems = systems;
    }

    public Transform2D camera() {
        return camera;
    }
}
