/*
 * Copyright [2022] [Xebisco]
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

import com.xebisco.yield.exceptions.MissingPhysicsSystemException;
import com.xebisco.yield.systems.MiddlePointSystem;
import com.xebisco.yield.systems.PhysicsSystem;
import com.xebisco.yield.systems.YldTimeSystem;
import org.jbox2d.dynamics.World;

import java.util.ArrayList;

/**
 * A YldScene is the base of a Yield game, it contains the Entities, YldGraphics and the Systems, it's used to alternate between game moments.
 *
 * @author Xebisco
 * @since 4_alpha1
 */
public class YldScene extends YldB {

    private int frames;
    private Entity masterEntity;

    protected YldGraphics graphics;

    private boolean hadProgressScene;

    private Assets assets;

    private View view = new View(1280, 720);

    private ArrayList<YldSystem> systems = new ArrayList<>();
    /**
     * Contains all the input information about the scene.
     */
    protected YldInput input;
    private boolean callStart = true;
    /**
     * Contains all the time information about the scene.
     */
    protected YldTime time;

    /**
     * Replace the default systems.
     */
    public void defaultSystems() {
        replaceSystem(new YldTimeSystem());
        replaceSystem(new MiddlePointSystem());
        if (game.getConfiguration().startPhysicsSystem)
            replaceSystem(new PhysicsSystem());
    }

    /**
     * This method is the first method called when a YldScene instance is added to a YldGame instance.
     */
    @Override
    public void create() {

    }

    /**
     * Called every time when the scene enters in place.
     */
    public void start() {

    }

    /**
     * This function is called after the render function is called in the game handler.
     *
     * @param delta The time in seconds since the last frame.
     */
    public void afterRender(float delta) {

    }

    @Deprecated
    public void exit() {

    }

    /**
     * Called when the scene is destroyed.
     */
    @Override
    public void onDestroy() {

    }

    /**
     * Destroy this YldScene masterEntity and call onDestroy().
     */
    public final void destroyScene() {
        view.getTransform().reset();
        view.setPosition(new Vector2());
        masterEntity.destroy();
        onDestroy();
    }

    /**
     * Called on every frame of the scene.
     *
     * @param delta The time variation between the last frame and the actual one in seconds.
     */
    @Override
    public void update(float delta) {

    }

    /**
     * Search for all the YldSystem instances in this YldScene.
     *
     * @param system The class type of the system that's being searched.
     * @return The system found (null if not found)
     */
    public <S extends YldSystem> S getSystem(Class<S> system) {
        S system1 = null;
        for (YldSystem system2 : systems) {
            if (system.getName().hashCode() == system2.getClass().getName().hashCode() && system.getName().equals(system2.getClass().getName())) {
                system1 = system.cast(system2);
                break;
            }
        }
        return system1;
    }

    /**
     * Calls process on this YldScene instance masterEntity.
     *
     * @param delta The time variation between the last frame and the actual one in seconds.
     */
    public final void process(float delta, SampleGraphics graphics) {
        masterEntity.process(delta, graphics);
        masterEntity.sortChildren();
    }

    /**
     * Raycast from point1 to point2 and return the closest hit.
     *
     * @param requestingEntity The entity that is requesting the raycast.
     * @param point1           The starting point of the raycast.
     * @param point2           The end point of the ray.
     * @return A RayCast object.
     */
    public final RayCast rayCast(Entity requestingEntity, Vector2 point1, Vector2 point2) {
        RayCast rayCastCallback = new RayCast();
        rayCastCallback.setRequestEntity(requestingEntity);
        PhysicsSystem physicsSystem = getSystem(PhysicsSystem.class);
        if (physicsSystem == null)
            throw new MissingPhysicsSystemException();
        else {
            World world = physicsSystem.getBox2dWorld();
            world.raycast(rayCastCallback, Yld.toVec2(point1), Yld.toVec2(point2));
        }
        return rayCastCallback;
    }

    /**
     * This function returns a boolean value that indicates whether the start method has been called or not
     *
     * @return The value of the callStart variable.
     */
    public boolean isCallStart() {
        return callStart;
    }

    /**
     * This function sets the callStart variable to the value of the callStart parameter.
     *
     * @param callStart This is a boolean value that indicates whether the start method has been called or not
     */
    public void setCallStart(boolean callStart) {
        this.callStart = callStart;
    }

    /**
     * Getter for the input variable;
     *
     * @return the input variable.
     */
    public YldInput getInput() {
        return input;
    }

    /**
     * Setter for the input variable;
     */
    public void setInput(YldInput input) {
        this.input = input;
    }

    /**
     * @return How many frames this scene is active.
     */
    public int getFrames() {
        return frames;
    }

    /**
     * Setter for the frames variable;
     */
    public void setFrames(int frames) {
        this.frames = frames;
    }

    /**
     * Instantiate an Entity instance based in the prefab passed.
     * (Calls instantiate(prefab) on the masterEntity)
     *
     * @param prefab The prefab passed to the Entity.
     */
    public Entity instantiate(Prefab prefab) {
        return masterEntity.instantiate(prefab);
    }

    /**
     * Removes an Entity that corresponds to the given type from its parent and calls onDestroy().
     *
     * @param type The Entity to be destroyed type.
     */
    public <E extends Prefab> void destroy(Class<E> type) {
        this.masterEntity.destroy(type);
    }

    /**
     * Getter for the masterEntity variable;
     *
     * @return the masterEntity variable.
     */
    public Entity getMasterEntity() {
        return masterEntity;
    }

    /**
     * Setter for the masterEntity variable;
     */
    public void setMasterEntity(Entity masterEntity) {
        this.masterEntity = masterEntity;
    }

    /**
     * Sets the scene of the YldSystem as this instance and adds to the systems list.
     *
     * @param system The system to be added.
     */
    public void addSystem(YldSystem system) {
        system.setScene(this);
        systems.add(system);
        if (system instanceof SystemCreateMethod) {
            ((SystemCreateMethod) system).create();
        }
    }

    /**
     * Remove all systems that have the same class name as the system passed in, then add the system passed in.
     *
     * @param system The system to be added.
     */
    public void replaceSystem(final YldSystem system) {
        systems.removeIf(s -> s.getClass().getName().hashCode() == system.getClass().getName().hashCode());
        addSystem(system);
    }

    /**
     * Getter for the systems variable;
     *
     * @return the systems variable.
     */
    public ArrayList<YldSystem> getSystems() {
        return systems;
    }

    /**
     * Setter for the masterEntity variable;
     */
    public void setSystems(ArrayList<YldSystem> systems) {
        this.systems = systems;
    }

    /**
     * Getter for the time variable;
     *
     * @return the time variable.
     */
    public YldTime getTime() {
        return time;
    }

    /**
     * Setter for the time variable;
     */
    public void setTime(YldTime time) {
        this.time = time;
    }

    /**
     * Get the pixels per meter value from the game's configuration.
     *
     * @return The PPM value from the game's configuration.
     */
    public int getPpm() {
        return getGame().getConfiguration().ppm;
    }

    /**
     * This function returns the view.
     *
     * @return The view.
     */
    public View getView() {
        return view;
    }

    /**
     * This function sets the view to the view passed in.
     *
     * @param view The view value to set.
     */
    public void setView(View view) {
        this.view = view;
        game.getHandler().getRenderMaster().onResize(view.getWidth(), view.getHeight());
        Yld.debug(() -> Yld.log("Resized scene '" + this.getClass().getSimpleName() + "' to " + view.getWidth() + "x" + view.getHeight()));
    }

    /**
     * Returns the graphics object for this scene.
     *
     * @return The graphics object.
     */
    public YldGraphics getGraphics() {
        return graphics;
    }

    /**
     * This function sets the graphics object to the graphics object passed in.
     *
     * @param graphics The graphics object value to set.
     */
    public void setGraphics(YldGraphics graphics) {
        this.graphics = graphics;
    }

    /**
     * Returns an Asset instance for the game's package.
     *
     * @return The assets object.
     */
    public Assets getAssets() {
        return assets;
    }

    /**
     * This function sets the assets variable to the assets variable passed in.
     *
     * @param assets The assets object that contains the asset information.
     */
    public void setAssets(Assets assets) {
        this.assets = assets;
    }

    public boolean isHadProgressScene() {
        return hadProgressScene;
    }

    public void setHadProgressScene(boolean hadProgressScene) {
        this.hadProgressScene = hadProgressScene;
    }
}
