package com.xebisco.yield.physics.systems;

import com.xebisco.yield.Component;
import com.xebisco.yield.ProcessSystem;
import com.xebisco.yield.physics.components.Rigidbody;
import com.xebisco.yield.utils.Vector2;

public class PhysicsSystem extends ProcessSystem {
    @Override
    public String[] componentFilters() {
        return new String[]{"Rigidbody"};
    }

    @Override
    public void process(Component component, float delta) {
        if (component instanceof Rigidbody) {
            Vector2 velocity = ((Rigidbody) component).velocity;
            component.getTransform().translate(velocity.x * delta, velocity.y * delta);
        }
    }
}
