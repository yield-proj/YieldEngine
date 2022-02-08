package com.xebisco.yield.physics.components;

import com.xebisco.yield.Component;
import com.xebisco.yield.Yld;
import com.xebisco.yield.utils.Vector2;

public class Rigidbody extends Component {
    public Vector2 velocity = new Vector2();

    public boolean colliding = false;
    public Vector2 gravity = new Vector2(0, 9.8f);
    public float mass = 20f;

    public Vector2 force = new Vector2();

    public int gForce, fForceX, fForceY, lastForceXCounter, lastForceYCounter;
    public float lastForceX, lastForceY;

    @Override
    public void update(float delta) {
        transform.position.x += force.x * delta;
        transform.position.y += force.y * delta;
        if(force.x != 0) {
            fForceX++;
            if(force.x > 0)
                force.x -= mass * (fForceX / 100f);
            if(force.x < 0)
                force.x += mass * (fForceX / 100f);
            lastForceXCounter--;
            if(lastForceXCounter < 0) {
                lastForceXCounter = (int) (1 / delta);
                if(force.x == lastForceX) {
                    force.x = 0;
                }
                lastForceX = force.x;
            }
        } else {
            fForceX = 0;
        }
        if(force.y != 0) {
            fForceY++;
            if(force.y > 0)
                force.y -= mass * (fForceY / 100f);
            if(force.y < 0)
                force.y += mass * (fForceY / 100f);
            lastForceYCounter--;
            if(lastForceYCounter < 0) {
                lastForceYCounter = (int) (1 / delta);
                if(force.y == lastForceY) {
                    force.y = 0;
                }
                lastForceY = force.y;
            }
        } else {
            fForceY = 0;
        }
        if (!colliding) {
            gForce++;
            velocity.y += gravity.y * (mass * (gForce / 1000f));
        } else {
            gForce = 0;
        }
    }

    public void addForce(Vector2 vector) {
        force = vector.get();
    }
}
