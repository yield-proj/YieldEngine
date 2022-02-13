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

    public Entity instantiate(String name) {
        return entity.instantiate(name);
    }

    public boolean destroy(Entity entity) {
        return this.entity.destroy(entity);
    }

    public boolean destroy(String name) {
        return entity.destroy(name);
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
        return entity.getTransform();
    }

    public Transform getSelfTransform() {
        return entity.getSelfTransform();
    }

    public void setSelfTransform(Transform selfTransform) {
        entity.setSelfTransform(selfTransform);
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
