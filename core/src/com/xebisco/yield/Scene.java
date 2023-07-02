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

import com.xebisco.yield.physics.PhysicsSystem;
import com.xebisco.yield.ui.InputUI;

import java.util.HashSet;
import java.util.Set;

/**
 * The Scene class is an abstract class that extends Entity2DContainer and implements Behavior, and contains various
 * systems and properties for managing a scene.
 */
public abstract class Scene extends Entity2DContainer implements Behavior {
    private int frames;
    private final Vector2D camera = new Vector2D(), zoomScale = new Vector2D(1, 1);
    private Color backGroundColor = Colors.GRAY.darker();
    private Set<SystemBehavior> systems = new HashSet<>();
    private final PhysicsSystem physicsSystem = new PhysicsSystem();
    public static final Entity2DPrefab INPUT_UI_PREFAB = new Entity2DPrefab(new ComponentCreation(AudioPlayer.class), new ComponentCreation(InputUI.class));

    public Scene(Application application) {
        super(application);
        getSystems().add(physicsSystem);
        instantiate(INPUT_UI_PREFAB).setIndex(-1);
    }

    @Override
    public void onStart() {
        
    }

    @Override
    public void onUpdate() {

    }

    @Override
    public void dispose() {

    }

    /**
     * The function returns the value of the frames variable.
     *
     * @return The method is returning an integer value of the variable "frames".
     */
    public int getFrames() {
        return frames;
    }

    /**
     * The function sets the number of frames for this scene.
     *
     * @param frames The "frames" parameter is an integer value that represents the number of frames on this scene.
     */
    public void setFrames(int frames) {
        this.frames = frames;
    }

    /**
     * This function returns the background color.
     *
     * @return The method is returning the value of the `backGroundColor` variable.
     */
    public Color getBackGroundColor() {
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
    public Set<SystemBehavior> getSystems() {
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

    /**
     * The function returns the physics system.
     *
     * @return The method is returning an object of type `PhysicsSystem`.
     */
    public PhysicsSystem getPhysicsSystem() {
        return physicsSystem;
    }

    /**
     * The function returns a Vector2D representing the camera position.
     *
     * @return A Vector2D object representing the camera position.
     */
    public Vector2D getCamera() {
        return camera;
    }

    /**
     * The function returns the zoom scale as a Vector2D.
     *
     * @return The method is returning a Vector2D object named zoomScale.
     */
    public Vector2D getZoomScale() {
        return zoomScale;
    }

    public ContextTime getTime() {
        return getApplication().getApplicationManager().getManagerContext().getContextTime();
    }
}
