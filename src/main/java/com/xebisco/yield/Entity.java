package com.xebisco.yield;

import com.xebisco.yield.components.Renderer;
import com.xebisco.yield.components.Transform;
import com.xebisco.yield.graphics.Texture;

import java.util.ArrayList;

public final class Entity {
    private ArrayList<Component> components = new ArrayList<>();
    private Transform transform;
    private Renderer renderer;
    private Texture texture;
    private final YldScene scene;

    public Entity(YldScene scene, Texture texture) {
        this.texture = texture;
        this.scene = scene;
        addComponent(transform = new Transform());
        addComponent(renderer = new Renderer(scene.getGraphics(), texture));
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

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public YldScene getScene() {
        return scene;
    }
}
