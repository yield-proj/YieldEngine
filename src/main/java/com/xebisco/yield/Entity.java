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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * An Entity is a combination of Components and other Entities(called children), can contain game logic and be displayed on the screen.
 */
public final class Entity implements Comparable<Entity>
{
    private ArrayList<Component> components = new ArrayList<>();
    private Set<Entity> children = new TreeSet<>();
    private int index = 0;
    private Entity parent;
    private Transform selfTransform;
    private String name;
    private Material material = new Material();
    private static Material defaultMaterial = new Material();
    private final YldScene scene;
    private String tag = "default";
    private boolean active = true;

    /**
     * Creates an entity with a Transform component and a Renderer component.
     *
     * @param name   The name of this Entity.
     * @param scene  The scene of this Entity.
     * @param parent The parent Entity.
     */
    public Entity(String name, YldScene scene, Entity parent)
    {
        if (name == null)
            name = getClass().getSimpleName();
        this.name = name;
        this.scene = scene;
        this.parent = parent;
        addComponent(selfTransform = new Transform());
        material.setColor(defaultMaterial.getColor());
        material.setTexture(defaultMaterial.getTexture());
        material.setLined(defaultMaterial.isLined());
    }

    /**
     * Updates all the components of this Entity and all the systems in the scene of this Entity.
     *
     * @param delta The time variation between the last frame and the actual one in seconds.
     */
    public void process(float delta, SampleGraphics graphics)
    {
        if (active)
        {
            int i = 0;
            while (i < components.size())
            {
                Component component = components.get(i);
                component.setFrames(component.getFrames() + 1);
                if (component.getFrames() == 1)
                    component.start();
                component.update(delta);
                component.render(graphics);
                i++;
            }

            i = 0;
            while (i < scene.getSystems().size())
            {
                YldSystem sys = scene.getSystems().get(i);
                if (sys instanceof ProcessSystem)
                {
                    ProcessSystem system = (ProcessSystem) sys;
                    int i1 = 0;
                    while (i1 < components.size())
                    {
                        Component component = components.get(i1);
                        boolean call = false;
                        if (system.componentFilters() != null)
                        {
                            for (int i4 = 0; i4 < system.componentFilters().length; i4++)
                            {
                                if (component.getClass().getName().hashCode() == system.componentFilters()[i4].hashCode())
                                {
                                    if (component.getClass().getName().equals(system.componentFilters()[i4]))
                                    {
                                        call = true;
                                        break;
                                    }
                                }
                            }
                        }
                        else
                        {
                            call = true;
                        }

                        if (call)
                            system.process(component, delta);
                        i1++;
                    }
                }
                else if (sys instanceof UpdateSystem)
                {
                    ((UpdateSystem) sys).update(delta);
                }

                i++;
            }
            for(Entity child: children) {
                try {
                    child.process(delta, graphics);
                } catch (ConcurrentModificationException ignore) {

                }
            }
            selfTransform.getTransformed().reset();
        }
    }

    /**
     * Adds and setts a Component to this Entity.
     *
     * @param component The component to be added.
     */
    public void addComponent(Component component)
    {
        component.transform = selfTransform;
        component.setGame(scene.game);
        component.setInput(scene.game.input);
        component.setEntity(this);
        component.setScene(scene);
        component.setTime(scene.time);
        component.create();
        components.add(component);
    }

    /**
     * Search for all the Components instances in this Entity.
     *
     * @param type The class type of the component that's being searched.
     * @return The component found (null if not found)
     */
    public <T extends Component> T getComponent(Class<T> type)
    {
        return getComponent(type, 0);
    }

    /**
     * Search for all the Components instances in this Entity.
     *
     * @param type  The class type of the component that's being searched.
     * @param index the index of this component.
     * @return The component found (null if not found)
     */
    public <T extends Component> T getComponent(Class<T> type, int index)
    {
        T component = null;
        int i = 0;
        while (i < components.size())
        {
            if (i < index)
                break;
            Component component1 = components.get(i);
            if (component1.getClass().getName().hashCode() == type.getName().hashCode())
            {
                if (component1.getClass().getName().equals(type.getName()))
                {
                    component = type.cast(component1);
                    break;
                }
            }
            i++;
        }

        return component;
    }

    /**
     * Check if this Entity contains a Component with the specified class type.
     *
     * @param type The class type of the Component.
     * @return If the Entity contains the Component.
     */
    public <T extends Component> boolean containsComponent(Class<T> type)
    {
        return getComponent(type) != null;
    }

    /**
     * Check if this Entity parent contains a Component with the specified class type.
     *
     * @param type The class type of the Component.
     * @return If the Entity parent contains the Component.
     */
    public <T extends Component> T getComponentInParent(Class<T> type)
    {
        return parent.getComponent(type);
    }

    /**
     * Check if any of this Entity children contains a Component with the specified class type.
     *
     * @param type The class type of the Component.
     * @return If contains the Component.
     */
    public <T extends Component> T getComponentInChildren(Class<T> type)
    {
        T component = null;
        for(Entity child: children) {
            component = child.getComponent(type);
            if (component != null)
                break;
        }
        return component;
    }

    /**
     * Gets the transform based on all of these parents.
     *
     * @return The global transform.
     */
    public Transform getTransform()
    {
        Transform toReturn = new Transform();
        Transform.setAs(toReturn, selfTransform);
        if (parent != null)
        {
            toReturn.translate(parent.getTransform().position);
            toReturn.rotation += parent.getTransform().rotation;
            toReturn.scale.x *= parent.getTransform().scale.x;
            toReturn.scale.y *= parent.getTransform().scale.y;
        }

        return toReturn;
    }

    /**
     * Getter of the selfTransform variable of this Entity.
     *
     * @return The selfTransform variable.
     */
    public Transform getSelfTransform()
    {
        return selfTransform;
    }

    /**
     * Setter of the selfTransform variable of this Entity.
     */
    public void setSelfTransform(Transform selfTransform)
    {
        this.selfTransform = selfTransform;
    }

    /**
     * Getter of the components list of this Entity.
     *
     * @return The components list.
     */
    public ArrayList<Component> getComponents()
    {
        return components;
    }

    /**
     * Setter of the components list of this Entity.
     */
    public void setComponents(ArrayList<Component> components)
    {
        this.components = components;
    }

    /**
     * Getter of the scene variable of this Entity.
     *
     * @return The scene variable.
     */
    public YldScene getScene()
    {
        return scene;
    }

    /**
     * Getter for the tag of this entity.
     *
     * @return The tag variable.
     */
    public String getTag()
    {
        return tag;
    }

    /**
     * Setter for the tag of this entity.
     */
    public void setTag(String tag)
    {
        this.tag = tag;
    }

    /**
     * Getter of the name variable of this Entity.
     *
     * @return The name variable.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Setter of the name variable of this Entity.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Instantiates an entity based on the given Prefab, and adds it to this Entity children list.
     *
     * @param prefab The Prefab of the instantiated Entity.
     * @return The instantiated Entity instance.
     */
    public Entity instantiate(Prefab prefab)
    {
        String name = "Entity";
        if (prefab != null)
            name = prefab.getClass().getName();
        Entity entity = new Entity(name, scene, null);
        entity.setIndex(index + 1);
        addChild(entity);
        if (prefab != null)
            prefab.create(entity);
        return entity;
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
     * Destroys a child of this Entity based on the given class type.
     *
     * @param type The type of the entity that will be destroyed.
     */
    public <E extends Prefab> void destroy(Class<E> type)
    {
        for(Entity e: children) {
            if (e.getName().hashCode() == type.getName().hashCode())
            {
                if (e.getName().equals(name))
                {
                    e.destroy();
                    break;
                }
            }
        }
    }

    /**
     * Destroys this Entity.
     */
    public void destroy()
    {
        for (Entity e : children)
        {
            e.destroy();
        }
        for (Component component : components)
        {
            component.onDestroy();
        }
        if (parent != null)
            parent.getChildren().removeIf(e -> e == this);
    }

    /**
     * Getter of the children list of this Entity.
     *
     * @return The children list.
     */
    public Set<Entity> getChildren()
    {
        return children;
    }

    @Deprecated
    public Entity addChildren(Entity e)
    {
        children.add(e);
        return e;
    }

    /**
     * Adds a Entity to the children list.
     *
     * @param child The Entity to be added.
     * @return The added Entity.
     */
    public Entity addChild(Entity child)
    {
        child.setParent(this);
        children.add(child);
        return child;
    }

    /**
     * Calls the specified method on all the components of this Entity.
     *
     * @param method    The name of the method.
     * @param arguments The arguments of the method.
     */
    public void transmit(String method, Object... arguments)
    {
        for (Component c : components)
        {
            Method[] methods = c.getClass().getDeclaredMethods();
            for (Method m : methods)
            {
                if (m.getName().hashCode() == method.hashCode() && m.getName().equals(method))
                {
                    try
                    {
                        m.invoke(c, arguments);
                    } catch (IllegalAccessException | InvocationTargetException e)
                    {
                        e.printStackTrace();
                    }
                    break;
                }

            }
        }
    }

    /**
     * Calls the specified method on all the components on all the children of this Entity.
     *
     * @param method    The name of the method.
     * @param arguments The arguments of the method.
     */
    public void transmitToChildren(String method, Object... arguments)
    {
        for (Entity e : children)
        {
            e.transmit(method, arguments);
        }
    }

    @Override
    public String toString() {
        return "Entity{" +
                "components=" + components +
                ", children=" + children +
                ", tag='" + tag + '\'' +
                '}';
    }

    /**
     * Setter of the children list of this Entity.
     */
    public void setChildren(Set<Entity> children)
    {
        this.children = children;
    }

    /**
     * Getter of the parent variable of this Entity.
     *
     * @return the parent variable.
     */
    public Entity getParent()
    {
        return parent;
    }

    /**
     * Setter of the parent variable of this Entity.
     */
    public void setParent(Entity parent)
    {
        this.parent = parent;
    }

    /**
     * @return The render index of this Entity. (not influenced by the parent Entity)
     */
    public int getIndex()
    {
        return index;
    }

    /**
     * Setter of the index variable of this Entity.
     */
    public void setIndex(int index)
    {
        this.index = index;
    }

    /**
     * Getter of the material variable of this Entity.
     *
     * @return the material variable.
     */
    public Material getMaterial()
    {
        return material;
    }

    /**
     * Setter of the material variable of this Entity.
     */
    public void setMaterial(Material material)
    {
        this.material = material;
    }

    /**
     * Getter of the default material.
     *
     * @return the default material variable.
     */
    public static Material getDefaultMaterial()
    {
        return defaultMaterial;
    }

    /**
     * Setter of the default material.
     */
    public static void setDefaultMaterial(Material defaultMaterial)
    {
        Entity.defaultMaterial = defaultMaterial;
    }

    /**
     * Getter of the active material.
     *
     * @return the active material variable.
     */
    public boolean isActive()
    {
        return active;
    }

    /**
     * Setter of the active material.
     */
    public void setActive(boolean active)
    {
        this.active = active;
    }

    @Override
    public int compareTo(Entity o) {
        return Integer.compare(index, o.getIndex());
    }


}
