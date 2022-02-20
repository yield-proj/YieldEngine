package com.xebisco.yield;

import com.xebisco.yield.input.YldInput;

import java.util.ArrayList;
import java.util.List;

public class YldScene implements YldB {

    private int frames;
    protected YldGraphics graphics = new YldGraphics();
    private Entity masterEntity;
    private ArrayList<YldSystem> systems = new ArrayList<>();
    protected YldInput input;
    private boolean callStart;
    protected YldGame game;
    protected YldTime time;

    public YldScene() {
        addSystem(new YldTimeSystem());
    }

    @Override
    public void create() {

    }

    public void start() {

    }

    public void exit() {

    }

    @Override
    public void update(float delta) {

    }

    public final void process(float delta) {
        masterEntity.process(delta);
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

    public Entity instantiate(String name) {
        return masterEntity.instantiate(name);
    }

    public Entity instantiate() {
        return masterEntity.instantiate();
    }

    public boolean destroy(Entity entity) {
        return masterEntity.destroy(entity);
    }

    public boolean destroy(String name) {
        return masterEntity.destroy(name);
    }

    public Entity getMasterEntity() {
        return masterEntity;
    }

    public void setMasterEntity(Entity masterEntity) {
        this.masterEntity = masterEntity;
    }

    public void addSystem(YldSystem system) {
        system.setScene(this);
        systems.add(system);
    }

    public ArrayList<YldSystem> getSystems() {
        return systems;
    }

    public void setSystems(ArrayList<YldSystem> systems) {
        this.systems = systems;
    }

    public YldTime getTime() {
        return time;
    }

    public void setTime(YldTime time) {
        this.time = time;
    }
}
