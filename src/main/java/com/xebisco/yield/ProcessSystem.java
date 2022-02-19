package com.xebisco.yield;

public abstract class ProcessSystem extends YldSystem {
    public abstract <T extends Component> T[] componentFilters();

    public abstract void process(Component component, float delta);
}
