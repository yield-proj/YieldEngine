package com.xebisco.yield.systems;

import com.xebisco.yield.Component;
import com.xebisco.yield.ProcessSystem;
import com.xebisco.yield.Yld;
import com.xebisco.yield.components.RectCollider;
import com.xebisco.yield.components.Rigidbody;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;

public class PhysicsSystem extends ProcessSystem {
    @Override
    public String[] componentFilters() {
        return new String[]{
                "Rigidbody", "RectCollider"
        };
    }

    private final HashSet<RectCollider> colliders = new HashSet<>();

    @Override
    public void process(Component component, float delta) {
        /*if (component instanceof RectCollider) {
            RectCollider collider = (RectCollider) component;
            int x, y, w, h;
            x = (int) (collider.getTransform().position.x + collider.getRectPosition().x);
            y = (int) (collider.getTransform().position.y + collider.getRectPosition().y);
            w = (int) (collider.getTransform().scale.x * collider.getWidth());
            h = (int) (collider.getTransform().scale.y * collider.getHeight());

            colliders.add(collider);
            Rigidbody rigidbody = null;

            int x1, y1, w1, h1;
            for (RectCollider c : colliders) {
                if (c != collider) {
                    x1 = (int) (c.getTransform().position.x + c.getRectPosition().x);
                    y1 = (int) (c.getTransform().position.y + c.getRectPosition().y);
                    w1 = (int) (c.getTransform().scale.x * c.getWidth());
                    h1 = (int) (c.getTransform().scale.y * c.getHeight());
                    Rectangle r1 = new Rectangle(x, y, w, h), r2 = new Rectangle(x1, y1, w1, h1);
                    //Yld.log(x + ", " + x1);
                    if (r1.(r2)) {
                        rigidbody = (Rigidbody) c.getComponent("Rigidbody");
                        if(rigidbody != null) {
                            if(rigidbody.velocity.y > 0) {
                                rigidbody.getSelfTransform().translate(0 , -rigidbody.velocity.y);
                            }
                            rigidbody.velocity.x = 0;
                            rigidbody.velocity.y = 0;
                            rigidbody.gravityTime.x = 0;
                            rigidbody.gravityTime.y = 0;
                        }
                    }
                }

            }
        }*/
        if (component instanceof Rigidbody) {
            Rigidbody rigidbody = (Rigidbody) component;
            rigidbody.getSelfTransform().translate(rigidbody.velocity.x * delta, rigidbody.velocity.y * delta);
        }
    }
}
