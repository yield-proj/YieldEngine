package com.xebisco.yield;

import com.xebisco.yield.input.YldInput;
import com.xebisco.yield.systems.MiddlePointSystem;
import com.xebisco.yield.systems.YldTimeSystem;

import java.util.ArrayList;

public class YldScene extends YldB
{

    private int frames;
    protected YldGraphics graphics = new YldGraphics();
    private Entity masterEntity;
    private ArrayList<YldSystem> systems = new ArrayList<>();
    protected YldInput input;
    private boolean callStart;
    protected YldTime time;

    public YldScene()
    {
        addSystem(new YldTimeSystem());
        addSystem(new MiddlePointSystem());
    }

    @Override
    public void create()
    {

    }

    public void start()
    {

    }

    @Deprecated
    public void exit()
    {

    }

    @Override
    public void onDestroy()
    {

    }

    public final void destroyScene() {
        masterEntity.destroy();
        onDestroy();
    }

    @Override
    public void update(float delta)
    {

    }

    public final void process(float delta)
    {
        masterEntity.process(delta);
    }

    public YldGraphics getGraphics()
    {
        return graphics;
    }

    public void setGraphics(YldGraphics graphics)
    {
        this.graphics = graphics;
    }

    public YldInput getInput()
    {
        return input;
    }

    public void setInput(YldInput input)
    {
        this.input = input;
    }

    public int getFrames()
    {
        return frames;
    }

    public void setFrames(int frames)
    {
        this.frames = frames;
    }

    public boolean isCallStart()
    {
        return callStart;
    }

    public void setCallStart(boolean callStart)
    {
        this.callStart = callStart;
    }

    public Entity instantiate(Prefab prefab)
    {
        return masterEntity.instantiate(prefab);
    }

    public Entity instantiate()
    {
        return masterEntity.instantiate();
    }

    public <E extends Prefab> void destroy(Class<E> type) {
        this.masterEntity.destroy(type);
    }

    public Entity getMasterEntity()
    {
        return masterEntity;
    }

    public void setMasterEntity(Entity masterEntity)
    {
        this.masterEntity = masterEntity;
    }

    public void addSystem(YldSystem system)
    {
        system.setScene(this);
        systems.add(system);
    }

    public ArrayList<YldSystem> getSystems()
    {
        return systems;
    }

    public void setSystems(ArrayList<YldSystem> systems)
    {
        this.systems = systems;
    }

    public YldTime getTime()
    {
        return time;
    }

    public void setTime(YldTime time)
    {
        this.time = time;
    }
}
