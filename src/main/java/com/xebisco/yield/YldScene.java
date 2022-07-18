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

import com.xebisco.yield.systems.MiddlePointSystem;
import com.xebisco.yield.systems.YldTimeSystem;

import java.util.ArrayList;

/**
 * A YldScene is the base of a Yield game, it contains the Entities, YldGraphics and the Systems, it's used to alternate between game moments.
 * @since 4_alpha1
 * @author Xebisco
 */
public class YldScene extends YldB
{

    private int frames, ppm;
    private Entity masterEntity;

    protected YldGraphics graphics;

    protected View view = new View(1280, 720);

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

    public YldScene()
    {
        addSystem(new YldTimeSystem());
        addSystem(new MiddlePointSystem());
    }

    /**
     * This method is the first method called when a YldScene instance is added to a YldGame instance.
     */
    @Override
    public void create()
    {

    }

    /**
     * Called every time when the scene enters in place.
     */
    public void start()
    {

    }

    @Deprecated
    public void exit()
    {

    }

    /**
     * Called when the scene is destroyed.
     */
    @Override
    public void onDestroy()
    {

    }

    /**
     * Destroy this YldScene masterEntity and call onDestroy().
     */
    public final void destroyScene()
    {
        masterEntity.destroy();
        onDestroy();
    }

    /**
     * Called on every frame of the scene.
     *
     * @param delta The time variation between the last frame and the actual one in seconds.
     */
    @Override
    public void update(float delta)
    {

    }

    /**
     * Search for all the YldSystem instances in this YldScene.
     * @param system The class type of the system that's being searched.
     * @return The system found (null if not found)
     */
    public <S extends YldSystem> S getSystem(Class<S> system)
    {
        S system1 = null;
        for (YldSystem system2 : systems)
        {
            if (system.getName().hashCode() == system2.getClass().getName().hashCode() && system.getName().equals(system2.getClass().getName()))
            {
                system1 = system.cast(system2);
                break;
            }
        }
        return system1;
    }

    /**
     * Calls process on this YldScene instance masterEntity.
     * @param delta The time variation between the last frame and the actual one in seconds.
     */
    public final void process(float delta, SampleGraphics graphics)
    {
        masterEntity.process(delta, graphics);
        masterEntity.sortChildren();
    }

    public boolean isCallStart()
    {
        return callStart;
    }

    public void setCallStart(boolean callStart)
    {
        this.callStart = callStart;
    }

    /**
     * Getter for the input variable;
     * @return the input variable.
     */
    public YldInput getInput()
    {
        return input;
    }

    /**
     * Setter for the input variable;
     */
    public void setInput(YldInput input)
    {
        this.input = input;
    }

    /**
     * @return How many frames this scene is active.
     */
    public int getFrames()
    {
        return frames;
    }

    /**
     * Setter for the frames variable;
     */
    public void setFrames(int frames)
    {
        this.frames = frames;
    }

    /**
     * Instantiate an Entity instance based in the prefab passed.
     * (Calls instantiate(prefab) on the masterEntity)
     * @param prefab The prefab passed to the Entity.
     */
    public Entity instantiate(Prefab prefab)
    {
        return masterEntity.instantiate(prefab);
    }

    public Entity instantiate(Prefab prefab, YldB yldB)
    {
        return masterEntity.instantiate(prefab, yldB);
    }

    /**
     * Create an empty Entity instance.
     * (Calls instantiate() on the masterEntity)
     */
    public Entity instantiate()
    {
        return masterEntity.instantiate();
    }

    /**
     * Removes an Entity that corresponds to the given type from its parent and calls onDestroy().
     * @param type The Entity to be destroyed type.
     */
    public <E extends Prefab> void destroy(Class<E> type)
    {
        this.masterEntity.destroy(type);
    }

    /**
     * Getter for the masterEntity variable;
     * @return the masterEntity variable.
     */
    public Entity getMasterEntity()
    {
        return masterEntity;
    }

    /**
     * Setter for the masterEntity variable;
     */
    public void setMasterEntity(Entity masterEntity)
    {
        this.masterEntity = masterEntity;
    }

    /**
     * Sets the scene of the YldSystem as this instance and adds to the systems list.
     * @param system The system to be added.
     */
    public void addSystem(YldSystem system)
    {
        system.setScene(this);
        systems.add(system);
    }

    /**
     * Getter for the systems variable;
     * @return the systems variable.
     */
    public ArrayList<YldSystem> getSystems()
    {
        return systems;
    }

    /**
     * Setter for the masterEntity variable;
     */
    public void setSystems(ArrayList<YldSystem> systems)
    {
        this.systems = systems;
    }

    /**
     * Getter for the time variable;
     * @return the time variable.
     */
    public YldTime getTime()
    {
        return time;
    }

    /**
     * Setter for the time variable;
     */
    public void setTime(YldTime time)
    {
        this.time = time;
    }

    public int getPpm()
    {
        return ppm;
    }

    public void setPpm(int ppm)
    {
        this.ppm = ppm;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public YldGraphics getGraphics() {
        return graphics;
    }

    public void setGraphics(YldGraphics graphics) {
        this.graphics = graphics;
    }
}
