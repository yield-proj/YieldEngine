package com.xebisco.yield;

import com.xebisco.yield.components.Renderer;
import com.xebisco.yield.components.Transform;
import com.xebisco.yield.graphics.Material;
import com.xebisco.yield.graphics.Texture;

import java.util.ArrayList;

public final class Entity {
    private ArrayList<Component> components = new ArrayList<>();
    private Transform transform;
    private Renderer renderer;
    private Material material;
    private String name;
    private final YldScene scene;

    public Entity(String name, YldScene scene, Material material) {
        if(name == null)
            name = getClass().getSimpleName();
        this.name = name;
        this.material = material;
        this.scene = scene;
        addComponent(transform = new Transform());
        addComponent(renderer = new Renderer(scene.getGraphics(), material));
    }

    public void process(float delta) {
        int i = 0;
        while (i < components.size()) {
            Component component = components.get(i);
            component.setFrames(component.getFrames() + 1);
            if(component.getFrames() == 1)
                component.start();
            component.update(delta);
            i++;
        }
    }

    public void addComponent(Component component) {
        component.transform = transform;
        component.setGame(scene.game);
        component.setInput(scene.game.input);
        component.setEntity(this);
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
        return transform;
    }

    public void setTransform(Transform transform) {
        this.transform = transform;
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

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
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
}
