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
import com.xebisco.yield.systems.PhysicsSystem;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import java.util.ArrayList;
import java.util.List;

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


    public void onCollisionEnter(Entity colliding, Vector2 collisionNormal)
    {

    }

    public void onCollisionEnter(Entity colliding)
    {

    }

    public void onCollisionExit(Entity colliding, Vector2 collisionNormal)
    {

    }

    public void onCollisionExit(Entity colliding)
    {

    }

    public void preSolveCollision(Entity colliding, Vector2 collisionNormal)
    {

    }

    public void preSolveCollision(Entity colliding)
    {

    }

    public void postSolveCollision(Entity colliding, Vector2 collisionNormal)
    {

    }

    public void postSolveCollision(Entity colliding)
    {

    }

    public RayCast rayCast(Entity requestEntity, Vector2 point1, Vector2 point2) {
        return scene.rayCast(requestEntity, point1, point2);
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
     * Called on every frame of the scene, after the update method.
     * @param graphics SampleGraphics object
     */
    public void render(SampleGraphics graphics) {

    }

    /**
     * Instantiates an entity based on the given Prefab, and adds it to this Entity children list.
     *
     * @param prefab The Prefab of the instantiated Entity.
     * @return The instantiated Entity instance.
     */
    public Entity instantiate(Prefab prefab)
    {
        if(prefab == null) {
            Yld.log("prefab - " + entity);
        }
        return entity.instantiate(prefab);
    }

    public Entity instantiate(Prefab prefab, YldB yldB)
    {
        return entity.instantiate(prefab, yldB);
    }

    /**
     * Instantiates a null entity, and adds it to this Entity children list.
     *
     * @return The instantiated Entity instance.
     */
    public Entity instantiate()
    {
        return instantiate((Prefab) null);
    }

    public Entity instantiate(Vector2 pos)
    {
        return entity.instantiate(null, null, pos);
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
     * Search for all the Components instances in this Entity.
     *
     * @param type The class type of the component that's being searched.
     * @param index The index of the component.
     * @return The component found (null if not found)
     */
    public <T extends Component> T getComponent(Class<T> type, int index)
    {
        return entity.getComponent(type, index);
    }

    public <T extends Component> List<T> getComponentList(Class<T> type)
    {
        return entity.getComponentList(type);
    }

    public <T extends Component> Component[] getComponents(Class<T> type)
    {
        return entity.getComponents(type);
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

    public void setVisible(boolean visible) {
        entity.setVisible(visible);
    }
    public boolean isVisible() {
        return entity.isVisible();
    }

    public void setActive(boolean active) {
        entity.setActive(active);
    }
    public boolean isActive() {
        return entity.isActive();
    }
}
