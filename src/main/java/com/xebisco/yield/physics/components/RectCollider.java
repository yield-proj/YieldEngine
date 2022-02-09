package com.xebisco.yield.physics.components;

import com.xebisco.yield.utils.Vector2;
import jdk.jfr.Experimental;

/**
 * THIS COMPONENT IS NOT FINISHED
 */
@Experimental
public class RectCollider extends Collider {
    private Vector2 positionOffset, sizeOffset;
    public RectCollider(Vector2 positionOffset, Vector2 sizeOffset) {
        super(0, 0, 0, 0);
        this.positionOffset = positionOffset;
        this.sizeOffset = sizeOffset;
    }

    public RectCollider() {
        super(0, 0, 0, 0);
        this.positionOffset = new Vector2();
        this.sizeOffset = new Vector2();
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        process();
    }

    private void process() {
        setX1(transform.position.x + positionOffset.x);
        setX2(transform.position.x + transform.size.x + positionOffset.x + sizeOffset.x);
        setY1(transform.position.y + positionOffset.y);
        setY2(transform.position.y + transform.size.y + positionOffset.y + sizeOffset.y);
    }

    public Vector2 getPositionOffset() {
        return positionOffset;
    }

    public void setPositionOffset(Vector2 positionOffset) {
        this.positionOffset = positionOffset;
    }

    public Vector2 getSizeOffset() {
        return sizeOffset;
    }

    public void setSizeOffset(Vector2 sizeOffset) {
        this.sizeOffset = sizeOffset;
    }
}
