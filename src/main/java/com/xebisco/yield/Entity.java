package com.xebisco.yield;

import com.xebisco.yield.components.Renderer;
import com.xebisco.yield.components.Transform;

import java.util.ArrayList;
import java.util.List;

public final class Entity
{
    private ArrayList<Component> components = new ArrayList<>();
    private ArrayList<Entity> children = new ArrayList<>();
    private int index = -1;
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
            if (children.size() > 0)
            {
                i = 0;
                while (i < children.size())
                {
                    Entity e = children.get(i);
                    if (e.getIndex() < -1)
                    {
                        throw new IllegalArgumentException("index cannot be less than -1");
                    }
                    if (e.getIndex() != -1)
                    {
                        if (e.getIndex() >= children.size())
                            e.setIndex(children.size() - 1);
                        int indexOf = children.indexOf(e);
                        Entity e1 = children.get(e.getIndex());
                        children.set(e.getIndex(), e);
                        children.set(indexOf, e1);
                    }
                    i++;
                }
            }
            if (index < -1)
            {
                throw new IllegalArgumentException("index cannot be less than -1");
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

    public Entity instantiate(String name)
    {
        Entity entity = new Entity(name, scene, null);
        entity.setParent(this);
        children.add(entity);
        return entity;
    }

    public Entity instantiate()
    {
        return instantiate(null);
    }

    public boolean destroy(Entity entity)
    {
        return children.remove(entity);
    }

    public boolean destroy(String name)
    {
        Entity entity = null;
        int i = 0;
        while (i < children.size())
        {
            Entity e = children.get(i);
            if (e.getName().hashCode() == name.hashCode())
            {
                if (e.getName().equals(name))
                {
                    entity = e;
                    break;
                }
            }
            i++;
        }
        return children.remove(entity);
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
        return index;
    }

    public void setIndex(int index)
    {
        this.index = index;
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
