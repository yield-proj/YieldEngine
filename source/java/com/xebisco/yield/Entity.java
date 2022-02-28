package com.xebisco.yield;

import com.xebisco.yield.components.Renderer;
import com.xebisco.yield.components.Transform;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public final class Entity
{
    private ArrayList<Component> components = new ArrayList<>();
    private ArrayList<Entity> children = new ArrayList<>();
    private Entity parent;
    private Transform selfTransform;
    private Renderer renderer;
    private String name;
    private Material material = defaultMaterial;
    private static Material defaultMaterial = new Material();
    private final YldScene scene;
    private boolean active = true;

    public Entity(String name, YldScene scene, Entity parent)
    {
        if (name == null)
            name = getClass().getSimpleName();
        this.name = name;
        this.scene = scene;
        this.parent = parent;
        addComponent(selfTransform = new Transform());
        addComponent(renderer = new Renderer());
    }

    public void process(float delta)
    {
        if (active)
        {
            int i = 0;
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
            i = 0;
            while (i < components.size())
            {
                Component component = components.get(i);
                component.setFrames(component.getFrames() + 1);
                if (component.getFrames() == 1)
                    component.start();
                component.update(delta);
                i++;
            }

            i = 0;
            while (i < children.size())
            {
                children.get(i).process(delta);
                i++;
            }
        }
    }

    public void addComponent(Component component)
    {
        component.transform = selfTransform;
        component.setGame(scene.game);
        component.setInput(scene.game.input);
        component.setEntity(this);
        component.setScene(scene);
        component.setGraphics(scene.graphics);
        component.setTime(scene.time);
        component.create();
        components.add(component);
    }

    public <T extends Component> T getComponent(Class<T> type)
    {
        T component = null;
        int i = 0;
        while (i < components.size())
        {
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

    public <T extends Component> boolean containsComponent(Class<T> type)
    {
        return getComponent(type) != null;
    }

    public <T extends Component> T getComponentInParent(Class<T> type)
    {
        return parent.getComponent(type);
    }

    public <T extends Component> T getComponentInChildren(Class<T> type)
    {
        T component = null;
        int i = 0;
        while (i < children.size())
        {
            component = children.get(i).getComponent(type);
            if (component != null)
                break;
            i++;
        }
        return component;
    }

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

    public Transform getSelfTransform()
    {
        return selfTransform;
    }

    public void setSelfTransform(Transform selfTransform)
    {
        this.selfTransform = selfTransform;
    }

    public ArrayList<Component> getComponents()
    {
        return components;
    }

    public void setComponents(ArrayList<Component> components)
    {
        this.components = components;
    }

    public Renderer getRenderer()
    {
        return renderer;
    }

    public void setRenderer(Renderer renderer)
    {
        this.renderer = renderer;
    }

    public YldScene getScene()
    {
        return scene;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Entity instantiate(Prefab prefab)
    {
        String name = "Entity";
        if (prefab != null)
            name = prefab.getClass().getName();
        Entity entity = new Entity(name, scene, null);
        entity.setParent(this);
        children.add(entity);
        if (prefab != null)
            prefab.create(entity);
        return entity;
    }

    public Entity instantiate()
    {
        return instantiate(null);
    }

    public <E extends Prefab> void destroy(Class<E> type)
    {
        int i = 0;
        while (i < children.size())
        {
            Entity e = children.get(i);
            if (e.getName().hashCode() == type.getName().hashCode())
            {
                if (e.getName().equals(name))
                {
                    e.destroy();
                    break;
                }
            }
            i++;
        }
    }

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
            parent.getChildren().remove(this);
    }

    public ArrayList<Entity> getChildren()
    {
        return children;
    }

    public Entity addChildren(Entity e)
    {
        children.add(e);
        return e;
    }

    public void transmit(String method, Object... arguments)
    {
        for (Component c : components)
        {
            Method[] methods = c.getClass().getDeclaredMethods();
            for (Method m : methods)
            {
                try
                {
                    if (m.getName().hashCode() == method.hashCode() && m.getName().equals(method))
                        m.invoke(c, arguments);
                } catch (IllegalAccessException | InvocationTargetException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    public void transmitToChildren(String method, Object... arguments)
    {
        for (Entity e : children)
        {
            e.transmit(method, arguments);
        }
    }

    public void setChildren(ArrayList<Entity> children)
    {
        this.children = children;
    }

    public Entity getParent()
    {
        return parent;
    }

    public void setParent(Entity parent)
    {
        this.parent = parent;
    }

    public int getIndex()
    {
        if (parent != null)
            return parent.getChildren().indexOf(this);
        else return 0;
    }

    public int getEntityIndex()
    {
        int index = getIndex();
        if (parent != null)
        {
            index += parent.getChildren().size();
        }
        return index;
    }

    public void setIndex(int index)
    {
        if (index < -1)
        {
            throw new IllegalArgumentException("index cannot be less than -1");
        }
        if (index != -1)
        {
            int i = index;
            if (index >= parent.getChildren().size())
                i = parent.getChildren().size() - 1;
            int indexOf = parent.getChildren().indexOf(this);
            Entity e1 = parent.getChildren().get(i);
            parent.getChildren().set(i, this);
            parent.getChildren().set(indexOf, e1);
        }
    }

    public Material getMaterial()
    {
        return material;
    }

    public void setMaterial(Material material)
    {
        this.material = material;
    }

    public static Material getDefaultMaterial()
    {
        return defaultMaterial;
    }

    public static void setDefaultMaterial(Material defaultMaterial)
    {
        Entity.defaultMaterial = defaultMaterial;
    }

    public boolean isActive()
    {
        return active;
    }

    public void setActive(boolean active)
    {
        this.active = active;
    }
}
