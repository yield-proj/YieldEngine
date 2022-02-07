package com.xebisco.yield;

public abstract class ProcessSystem {
    public abstract String[] componentFilters();

    public abstract void process(Component component, float delta);
}
