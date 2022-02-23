package com.xebisco.yield;

public abstract class ProcessSystem extends YldSystem
{
    public abstract <C extends Component> C[] componentFilters();

    public abstract void process(Component component, float delta);
}
