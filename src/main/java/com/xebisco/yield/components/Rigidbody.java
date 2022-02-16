package com.xebisco.yield.components;

import com.xebisco.yield.Component;
import com.xebisco.yield.utils.Vector2;

public class Rigidbody extends Component {
    public Vector2 velocity = new Vector2(), gravity = new Vector2(0, 1), gravityTime = new Vector2();

    @Override
    public void update(float delta) {
        velocity.x += gravity.x * gravityTime.x * delta;
        velocity.y += gravity.y * gravityTime.y * delta;
        gravityTime.x++;
        gravityTime.y++;
    }

    public void addForce(Vector2 force) {
        velocity.x += force.x;
        velocity.y += force.y;
    }
}
