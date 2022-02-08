package com.xebisco.yield;

import com.xebisco.yield.components.Transform;
import com.xebisco.yield.input.YldInput;

public class Component implements YldB {
    private String name = getClass().getSimpleName();
    private int frames;
    private Entity entity;
    protected Transform transform;
    protected YldGame game;
    protected YldInput input;

    @Override
    public void create() {

    }

    public void start() {

    }

    @Override
    public void update(float delta) {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFrames() {
        return frames;
    }

    public void setFrames(int frames) {
        this.frames = frames;
    }

    public Component getComponent(String name) {
        return entity.getComponent(name);
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public Transform getTransform() {
        return transform;
    }

    public void setTransform(Transform transform) {
        this.transform = transform;
    }

    public YldGame getGame() {
        return game;
    }

    public void setGame(YldGame game) {
        this.game = game;
    }

    public YldInput getInput() {
        return input;
    }

    public void setInput(YldInput input) {
        this.input = input;
    }
}
