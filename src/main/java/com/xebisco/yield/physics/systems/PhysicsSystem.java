package com.xebisco.yield.physics.systems;

import com.xebisco.yield.Component;
import com.xebisco.yield.ProcessSystem;
import com.xebisco.yield.Yld;
import com.xebisco.yield.physics.components.Collider;
import com.xebisco.yield.physics.components.Rigidbody;
import com.xebisco.yield.utils.Vector2;
import jdk.jfr.Experimental;

import java.util.HashSet;

/**
 * THIS SYSTEM IS NOT FINISHED
 */
@Experimental
public class PhysicsSystem extends ProcessSystem {
    @Override
    public String[] componentFilters() {
        return null;
    }

    private final HashSet<Collider> colliders = new HashSet<>();

    @Override
    public void process(Component component, float delta) {
        /*if (component instanceof Collider) {
            Component rb = component.getComponent("Rigidbody");
            Collider collider = (Collider) component;
            colliders.add(collider);
            if (rb != null) {
                ((Rigidbody) rb).collider = collider;
            }
            collider.setColliding(false);
            collider.setInside(false);


            for (Collider c : colliders) {
                if (c.hashCode() != collider.hashCode()) {
                    if (collider.getX1() <= c.getX2() - 1 && collider.getX2() >= c.getX1() + 1
                            && collider.getY1() <= c.getY2() - 1 && collider.getY2() >= c.getY1() + 1) {
                        collider.setInside(true);
                    }
                    if (collider.getX1() <= c.getX2() + 1 && collider.getX2() >= c.getX1() - 1
                            && collider.getY1() <= c.getY2() + 1 && collider.getY2() >= c.getY1() - 1) {
                        collider.setColliding(true);
                    }
                    if (collider.isColliding()) {
                        if (c.isTrigger()) {
                            collider.setInside(false);
                            break;
                        }
                        if(rb != null) {
                            if(((Rigidbody) rb).velocity.y > 0) {
                                ((Rigidbody) rb).velocity.y = 0;
                                collider.getTransform().position.y -= collider.getY2() - c.getY1();

                            }
                            if(((Rigidbody) rb).velocity.y < 0) {
                                ((Rigidbody) rb).velocity.y = 0;
                                collider.getTransform().position.y  += 2;
                                Yld.log(collider.getTransform().position.y);
                            }
                            if(((Rigidbody) rb).velocity.x > 0) {

                                ((Rigidbody) rb).velocity.x = 0;
                                collider.getTransform().position.x -= collider.getX2() - c.getX1();
                            }
                            if(((Rigidbody) rb).velocity.x < 0) {
                                ((Rigidbody) rb).velocity.x = 0;
                                collider.getTransform().position.x -= collider.getX1() - c.getX2();
                            }
                        }
                    }
                }

            }
        }*/
        if (component instanceof Rigidbody) {
            Vector2 velocity = ((Rigidbody) component).velocity;
            component.getTransform().translate(velocity.x * delta, velocity.y * delta);
        }
    }
}
