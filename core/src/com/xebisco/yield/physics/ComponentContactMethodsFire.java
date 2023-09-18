package com.xebisco.yield.physics;

public class ComponentContactMethodsFire extends ContactAdapter {
    @Override
    public void onContactBegin(Collider collider, Collider colliding) {
        collider.entity().components().forEach(c -> c.onContactBegin(collider, colliding));
        colliding.entity().components().forEach(c -> c.onContactBegin(colliding, collider));
    }

    @Override
    public void onContactEnd(Collider collider, Collider colliding) {
        collider.entity().components().forEach(c -> c.onContactEnd(collider, colliding));
        colliding.entity().components().forEach(c -> c.onContactEnd(colliding, collider));
    }
}
