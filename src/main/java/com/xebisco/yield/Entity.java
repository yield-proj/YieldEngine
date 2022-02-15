package com.xebisco.yield;

import com.xebisco.yield.components.Renderer;
import com.xebisco.yield.components.Transform;

import java.util.ArrayList;

public final class Entity {
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

    public Entity(String name, YldScene scene, Entity parent) {
        if (name == null)
            name = getClass().getSimpleName();
        this.name = name;
        this.scene = scene;
        this.parent = parent;
        addComponent(selfTransform = new Transform());
        addComponent(renderer = new Renderer());
    }

    public void process(float delta) {
        int i = 0;
        while (i < components.size()) {
            Component component = components.get(i);
            component.setFrames(component.getFrames() + 1);
            if (component.getFrames() == 1)
                component.start();
            component.update(delta);
            i++;
        }
        if(children.size() > 0) {
            i = 0;
            while (i < children.size()) {
                Entity e = children.get(i);
                if (e.getIndex() < -1) {
                    throw new IllegalArgumentException("index cannot be less than -1");
                }
                if (e.getIndex() != -1) {
                    if (e.getIndex() >= children.size())
                        e.setIndex(children.size() - 1);
                    int indexOf = children.indexOf(e);
                    Entity e1 = children.get(e.getIndex());
                    children.set(e.getIndex(), e);
                    children.set(indexOf, e1);
                }
                i++;
            }
        } else {
            i = 0;
            while (i < scene.getEntities().size()) {
                Entity e = scene.getEntities().get(i);
                if (e.getIndex() < -1) {
                    throw new IllegalArgumentException("index cannot be less than -1");
                }
                if (e.getIndex() != -1) {
                    if (e.getIndex() >= scene.getEntities().size())
                        e.setIndex(scene.getEntities().size() - 1);
                    int indexOf = scene.getEntities().indexOf(e);
                    Entity e1 = scene.getEntities().get(e.getIndex());
                    scene.getEntities().set(e.getIndex(), e);
                    scene.getEntities().set(indexOf, e1);
                }
                i++;
            }
        }

        i = 0;
        while (i < children.size()) {
            children.get(i).process(delta);
            i++;
        }
    }

    public void addComponent(Component component) {
        component.transform = selfTransform;
        component.setGame(scene.game);
        component.setInput(scene.game.input);
        component.setEntity(this);
        component.setScene(scene);
        component.setTime(scene.time);
        component.create();
        components.add(component);
    }

    public Component getComponent(String name) {
        Component component = null;
        int i = 0;
        while (i < components.size()) {
            Component component1 = components.get(i);
            if (component1.getName().hashCode() == name.hashCode()) {
                if (component1.getName().equals(name)) {
                    component = component1;
                    break;
                }
            }
            i++;
        }
        return component;
    }

    public Component getComponentInParent(String name) {
        return parent.getComponent(name);
    }

    public Component getComponentInChildren(String name) {
        Component c = null;
        int i = 0;
        while (c == null || i < children.size()) {
            c = children.get(i).getComponent(name);
            i++;
        }
        return c;
    }

    public Component getComponent(Component comp) {
        String name = comp.getName();
        Component component = null;
        int i = 0;
        while (i < components.size()) {
            Component component1 = components.get(i);
            if (component1.getName().hashCode() == name.hashCode()) {
                if (component1.getName().equals(name)) {
                    component = component1;
                    break;
                }
            }
            i++;
        }
        return component;
    }

    public Transform getTransform() {
        Transform toReturn = new Transform();
        Transform.setAs(toReturn, selfTransform);
        if (parent != null) {
            toReturn.translate(parent.getTransform().position);
            toReturn.rotation += parent.getTransform().rotation;
            toReturn.scale.x *= parent.getTransform().scale.x;
            toReturn.scale.y *= parent.getTransform().scale.y;
        }

        return toReturn;
    }

    public Transform getSelfTransform() {
        return selfTransform;
    }

    public void setSelfTransform(Transform selfTransform) {
        this.selfTransform = selfTransform;
    }

    public ArrayList<Component> getComponents() {
        return components;
    }

    public void setComponents(ArrayList<Component> components) {
        this.components = components;
    }

    public Renderer getRenderer() {
        return renderer;
    }

    public void setRenderer(Renderer renderer) {
        this.renderer = renderer;
    }

    public YldScene getScene() {
        return scene;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Entity instantiate(String name) {
        Entity entity = new Entity(name, scene, null);
        entity.setParent(this);
        children.add(entity);
        return entity;
    }

    public Entity instantiate() {
        return instantiate(null);
    }

    public boolean destroy(Entity entity) {
        return children.remove(entity);
    }

    public boolean destroy(String name) {
        Entity entity = null;
        int i = 0;
        while (i < children.size()) {
            Entity e = children.get(i);
            if(e.getName().hashCode() == name.hashCode()) {
                if(e.getName().equals(name)) {
                    entity = e;
                    break;
                }
            }
            i++;
        }
        return children.remove(entity);
    }

    public ArrayList<Entity> getChildren() {
        return children;
    }

    public Entity addChildren(Entity e) {
        children.add(e);
        return e;
    }

    public void setChildren(ArrayList<Entity> children) {
        this.children = children;
    }

    public Entity getParent() {
        return parent;
    }

    public void setParent(Entity parent) {
        this.parent = parent;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public static Material getDefaultMaterial() {
        return defaultMaterial;
    }

    public static void setDefaultMaterial(Material defaultMaterial) {
        Entity.defaultMaterial = defaultMaterial;
    }
}
