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

public abstract class Component extends YldB
{
    private int frames;
    private Entity entity;
    protected Transform transform;
    protected YldScene scene;
    protected YldTime time;
    protected YldInput input;
    protected YldGraphics graphics;

    @Override
    public void create()
    {

    }

    public void start()
    {

    }

    @Override
    public void onDestroy()
    {

    }

    @Override
    public void update(float delta)
    {

    }

    public Entity instantiate(Prefab prefab)
    {
        return entity.instantiate(prefab);
    }

    public Entity instantiate()
    {
        return instantiate(null);
    }

    public void destroy()
    {
        this.entity.destroy();
    }

    public <E extends Prefab> void destroy(Class<E> type)
    {
        this.entity.destroy(type);
    }

    public int getFrames()
    {
        return frames;
    }

    public void setFrames(int frames)
    {
        this.frames = frames;
    }

    public void addComponent(Component component)
    {
        entity.addComponent(component);
    }

    public <T extends Component> T getComponent(Class<T> type)
    {
        return entity.getComponent(type);
    }

    public <T extends Component> T getComponentInChildren(Class<T> type)
    {
        return entity.getComponentInChildren(type);
    }

    public <T extends Component> T getComponentInParent(Class<T> type)
    {
        return entity.getComponentInParent(type);
    }

    public void setMaterial(Material material)
    {
        entity.setMaterial(material);
    }

    public Material getMaterial()
    {
        return entity.getMaterial();
    }

    public ArrayList<Component> getComponents()
    {
        return entity.getComponents();
    }

    public <T extends Component> boolean containsComponent(Class<T> type)
    {
        return entity.containsComponent(type);
    }

    public Entity getEntity()
    {
        return entity;
    }

    public void setEntity(Entity entity)
    {
        this.entity = entity;
    }

    public Transform getTransform()
    {
        return entity.getTransform();
    }

    public Transform getSelfTransform()
    {
        return entity.getSelfTransform();
    }

    public void setSelfTransform(Transform selfTransform)
    {
        entity.setSelfTransform(selfTransform);
    }

    public YldInput getInput()
    {
        return input;
    }

    public void setInput(YldInput input)
    {
        this.input = input;
    }

    public YldScene getScene()
    {
        return scene;
    }

    public void setScene(YldScene scene)
    {
        this.scene = scene;
    }

    public YldTime getTime()
    {
        return time;
    }

    public void setTime(YldTime time)
    {
        this.time = time;
    }

    public YldGraphics getGraphics()
    {
        return graphics;
    }

    public void setGraphics(YldGraphics graphics)
    {
        this.graphics = graphics;
    }
}
