package com.xebisco.yield;

import com.xebisco.yield.graphics.Material;
import com.xebisco.yield.graphics.Texture;
import com.xebisco.yield.input.YldInput;
import com.xebisco.yield.physics.systems.PhysicsSystem;

import javax.xml.soap.Text;
import java.util.ArrayList;
import java.util.List;

public class YldScene implements YldB {

    private int frames;
    protected YldGraphics graphics = new YldGraphics();
    private ArrayList<Entity> entities = new ArrayList<>();
    private ArrayList<ProcessSystem> systems = new ArrayList<>();
    protected YldInput input;
    private boolean callStart;
    protected YldGame game;

    public YldScene() {
        addSystem(new PhysicsSystem());
    }

    @Override
    public void create() {

    }

    public void start() {

    }

    @Override
    public void update(float delta) {

    }

    public final void process(float delta) {
        int i = 0;
        while (i < systems.size()) {
            ProcessSystem system = systems.get(i);
            int i2 = 0;
            while (i2 < entities.size()) {
                List<Component> components = entities.get(i2).getComponents();
                int i3 = 0;
                while (i3 < components.size()) {
                    Component component = components.get(i3);
                    boolean call = false;
                    if (system.componentFilters() != null) {
                        for (int i4 = 0; i4 < system.componentFilters().length; i4++) {
                            if (component.getName().hashCode() == system.componentFilters()[i4].hashCode()) {
                                if (component.getName().equals(system.componentFilters()[i4])) {
                                    call = true;
                                    break;
                                }
                            }
                        }
                    } else {
                        call = true;
                    }

                    if (call)
                        system.process(component, delta);
                    i3++;
                }
                i2++;
            }
            i++;
        }
        i = 0;
        while (i < entities.size()) {
            entities.get(i).process(delta);
            i++;
        }
    }

    public YldGraphics getGraphics() {
        return graphics;
    }

    public void setGraphics(YldGraphics graphics) {
        this.graphics = graphics;
    }

    public YldInput getInput() {
        return input;
    }

    public void setInput(YldInput input) {
        this.input = input;
    }

    public int getFrames() {
        return frames;
    }

    public void setFrames(int frames) {
        this.frames = frames;
    }

    public boolean isCallStart() {
        return callStart;
    }

    public void setCallStart(boolean callStart) {
        this.callStart = callStart;
    }

    public YldGame getGame() {
        return game;
    }

    public void setGame(YldGame game) {
        this.game = game;
    }

    public Entity instantiate(String name, Material material) {
        Entity entity = new Entity(name, this, material);
        entities.add(entity);
        return entity;
    }

    public Entity instantiate(String name) {
        return instantiate(name, new Material());
    }

    public Entity instantiate(Material material) {
        return instantiate(null, material);
    }

    public boolean destroy(Entity entity) {
        return entities.remove(entity);
    }

    public boolean destroy(String name) {
        Entity entity = null;
        int i = 0;
        while (i < entities.size()) {
            Entity e = entities.get(i);
            if(e.getName().hashCode() == name.hashCode()) {
                if(e.getName().equals(name)) {
                    entity = e;
                    break;
                }
            }
            i++;
        }
        return entities.remove(entity);
    }

    @Deprecated
    public Entity addEntity(Entity entity) {
        entities.add(entity);
        return entity;
    }

    public ArrayList<Entity> getEntities() {
        return entities;
    }

    public void setEntities(ArrayList<Entity> entities) {
        this.entities = entities;
    }

    public ProcessSystem addSystem(ProcessSystem system) {
        systems.add(system);
        return system;
    }

    public ArrayList<ProcessSystem> getSystems() {
        return systems;
    }

    public void setSystems(ArrayList<ProcessSystem> systems) {
        this.systems = systems;
    }
}
