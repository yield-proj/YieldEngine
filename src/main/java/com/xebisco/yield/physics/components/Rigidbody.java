package com.xebisco.yield.physics.components;

import com.xebisco.yield.Component;
import com.xebisco.yield.Yld;
import com.xebisco.yield.utils.Vector2;
import jdk.jfr.Experimental;

/**
 * THIS COMPONENT IS NOT FINISHED
 */
@Experimental
public class Rigidbody extends Component {
    public Vector2 velocity = new Vector2();

    //public Collider collider;
    public boolean colliding;
    public Vector2 gravity = new Vector2(0, 9.8f);
    public float mass = 1f, time;
    public boolean stopForce = true;

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void update(float delta) {
            if (!colliding) {
                time += delta;
                velocity.x += gravity.x * time;
                velocity.y += gravity.y * time;
            } else {
                time = 0;
        }

    }

    public void addForce(Vector2 vector) {
        velocity.x += vector.x;
        velocity.y += vector.y;
    }
}
