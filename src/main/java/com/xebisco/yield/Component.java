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

import com.xebisco.yield.components.Transform;
import com.xebisco.yield.input.YldInput;

import java.util.ArrayList;

/**
 * This class is a script for Entities, and is made to modify them.
 */
public abstract class Component extends YldB
{
    private int frames;
    private Entity entity;
    /**
     * The selfTransform variable of this Component Entity.
     */
    protected Transform transform;
    /**
     * The scene variable of this Component Entity.
     */
    protected YldScene scene;
    /**
     * The time variable of this Component Entity.
     */
    protected YldTime time;
    /**
     * The input variable of the scene of this Component.
     */
    protected YldInput input;
    /**
     * The graphics variable of the scene of this Component.
     */
    protected YldGraphics graphics;

    /**
     * Called when this Component is added to an Entity.
     */
    @Override
    public void create()
    {

    }

    /**
     * Called when this Component reaches in his first frame.
     */
    public void start()
    {

    }

    /**
     * Called when the Entity of this component is destroyed.
     */
    @Override
    public void onDestroy()
    {

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
     * Instantiates an entity based on the given Prefab, and adds it to this Entity children list.
     *
     * @param prefab The Prefab of the instantiated Entity.
     * @return The instantiated Entity instance.
     */
    public Entity instantiate(Prefab prefab)
    {
        return entity.instantiate(prefab);
    }

    /**
     * Instantiates a null entity, and adds it to this Entity children list.
     *
     * @return The instantiated Entity instance.
     */
    public Entity instantiate()
    {
        return instantiate(null);
    }

    /**
     * Destroys this Entity.
     */
    public void destroy()
    {
        this.entity.destroy();
    }

    /**
     * Destroys a child of this Entity based on the given class type.
     *
     * @param type The type of the entity that will be destroyed.
     */
    public <E extends Prefab> void destroy(Class<E> type)
    {
        this.entity.destroy(type);
    }

    /**
     * Getter of the frames variable of this Component.
     *
     * @return The frames variable.
     */
    public int getFrames()
    {
        return frames;
    }

    /**
     * Setter of the frames variable of this Component.
     */
    public void setFrames(int frames)
    {
        this.frames = frames;
    }

    /**
     * Adds and setts a Component to this Entity.
     *
     * @param component The component to be added.
     */
    public void addComponent(Component component)
    {
        entity.addComponent(component);
    }

    /**
     * Search for all the Components instances in this Entity.
     *
     * @param type The class type of the component that's being searched.
     * @return The component found (null if not found)
     */
    public <T extends Component> T getComponent(Class<T> type)
    {
        return entity.getComponent(type);
    }

    /**
     * Check if any of this Entity children contains a Component with the specified class type.
     *
     * @param type The class type of the Component.
     * @return If contains the Component.
     */
    public <T extends Component> T getComponentInChildren(Class<T> type)
    {
        return entity.getComponentInChildren(type);
    }

    /**
     * Check if this Entity parent contains a Component with the specified class type.
     *
     * @param type The class type of the Component.
     * @return If the Entity parent contains the Component.
     */
    public <T extends Component> T getComponentInParent(Class<T> type)
    {
        return entity.getComponentInParent(type);
    }

    /**
     * Setter of the material variable of this Entity.
     */
    public void setMaterial(Material material)
    {
        entity.setMaterial(material);
    }

    /**
     * Getter of the material variable of this Entity.
     * @return the material variable.
     */
    public Material getMaterial()
    {
        return entity.getMaterial();
    }

    /**
     * Getter of the components list of this Entity.
     *
     * @return The components list.
     */
    public ArrayList<Component> getComponents()
    {
        return entity.getComponents();
    }

    /**
     * Check if this Entity contains a Component with the specified class type.
     *
     * @param type The class type of the Component.
     * @return If the Entity contains the Component.
     */
    public <T extends Component> boolean containsComponent(Class<T> type)
    {
        return entity.containsComponent(type);
    }

    /**
     * Getter of the entity of this Component.
     *
     * @return The entity variable.
     */
    public Entity getEntity()
    {
        return entity;
    }

    /**
     * Setter of the entity of this Component.
     */
    public void setEntity(Entity entity)
    {
        this.entity = entity;
    }

    /**
     * Gets the transform based on all of these parents.
     *
     * @return The global transform.
     */
    public Transform getTransform()
    {
        return entity.getTransform();
    }

    /**
     * Getter of the selfTransform variable of this Component Entity.
     *
     * @return The selfTransform variable.
     */
    public Transform getSelfTransform()
    {
        return entity.getSelfTransform();
    }

    /**
     * Setter of the selfTransform variable of this Component Entity.
     */
    public void setSelfTransform(Transform selfTransform)
    {
        entity.setSelfTransform(selfTransform);
    }

    /**
     * Getter of the input of this Component.
     *
     * @return The input variable.
     */
    public YldInput getInput()
    {
        return input;
    }

    /**
     * Setter of the input of this Component.
     */
    public void setInput(YldInput input)
    {
        this.input = input;
    }

    /**
     * Getter of the scene of this Component.
     *
     * @return The scene variable.
     */
    public YldScene getScene()
    {
        return scene;
    }

    /**
     * Setter of the scene of this Component.
     */
    public void setScene(YldScene scene)
    {
        this.scene = scene;
    }

    /**
     * Getter of the time of this Component.
     *
     * @return The time variable.
     */
    public YldTime getTime()
    {
        return time;
    }

    /**
     * Setter of the time of this Component.
     */
    public void setTime(YldTime time)
    {
        this.time = time;
    }

    /**
     * Getter of the graphics of this Component.
     *
     * @return The graphics variable.
     */
    public YldGraphics getGraphics()
    {
        return graphics;
    }

    /**
     * Setter of the graphics of this Component.
     */
    public void setGraphics(YldGraphics graphics)
    {
        this.graphics = graphics;
    }

    /**
     * Getter for the tag of this entity.
     * @return The tag variable.
     */
    public String getTag()
    {
        return entity.getTag();
    }

    /**
     * Setter for the tag of this entity.
     */
    public void setTag(String tag)
    {
        entity.setTag(tag);
    }
}
