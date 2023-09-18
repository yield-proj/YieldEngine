package com.xebisco.yield.physics;

public interface BasicContactListener {
    /**
     * This is a function that is called when two colliders begin to make contact.
     *
     * @param collider The first parameter "collider" is an object that represents the collider that this method is being
     * called on.
     * @param colliding The "colliding" parameter in the method signature refers to the collider that the first collider
     * (represented by the "collider" parameter) has made contact with. In other words, it is the collider that is
     * colliding with the first collider.
     */
    void onContactBegin(Collider collider, Collider colliding);

    /**
     * This is a function that is called when two colliders stop colliding.
     *
     * @param collider The first parameter "collider" is an object that represents the collider that the current object is
     * attached to.
     * @param colliding The colliding parameter is a reference to the collider that the collider parameter is no longer in
     * contact with. In other words, it represents the collider that the onContactEnd event is being triggered for.
     */
    void onContactEnd(Collider collider, Collider colliding);
}
