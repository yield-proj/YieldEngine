package com.xebisco.yield.components;

import com.xebisco.yield.Component;
import com.xebisco.yield.utils.Vector2;

public class Transform extends Component {
    public Vector2 position = new Vector2(), scale = new Vector2(1, 1), middle;
    public float rotation = 0;
    public boolean middleRotation = true;

    public void translate(Vector2 vector2) {
        position.x += vector2.x;
        position.y += vector2.y;
    }

    public void translate(float x, float y) {
        translate(new Vector2(x, y));
    }

    public void rotate(float degrees) {
        rotation += degrees;
    }

    public static void setAs(Transform toSet, Transform transform) {
        toSet.position = transform.position.get();
        toSet.scale = transform.scale.get();
        if (transform.middle != null)
            toSet.middle = transform.middle.get();
        toSet.rotation = transform.rotation;
        toSet.middleRotation = transform.middleRotation;
    }
}
