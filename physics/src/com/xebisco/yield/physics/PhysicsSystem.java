package com.xebisco.yield.physics;

import com.xebisco.yield.ContextTime;
import com.xebisco.yield.SystemBehavior;
import com.xebisco.yield.Vector2D;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

public class PhysicsSystem extends SystemBehavior {
    private final World b2World = new World(new Vec2(0, -10));

    private final Vector2D gravity = new Vector2D(0, -10);

    @Override
    public void onUpdate(ContextTime time) {
        super.onUpdate(time);
        b2World.setGravity(Utils.toVec2(gravity));
    }

    public World b2World() {
        return b2World;
    }

    public Vector2D gravity() {
        return gravity;
    }
}
