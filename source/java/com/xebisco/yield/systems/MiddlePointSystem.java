package com.xebisco.yield.systems;

import com.xebisco.yield.Component;
import com.xebisco.yield.ProcessSystem;
import com.xebisco.yield.Yld;
import com.xebisco.yield.components.Transform;

public class MiddlePointSystem extends ProcessSystem
{
    @Override
    public String[] componentFilters()
    {
        return new String[]{
                Transform.class.getName()
        };
    }

    @Override
    public void process(Component component, float delta)
    {
        Transform transform = (Transform) component;
        if (transform.middleRotation)
        {
            transform.middle.x = transform.position.x;
            transform.middle.y = transform.position.y;
        }
    }
}
