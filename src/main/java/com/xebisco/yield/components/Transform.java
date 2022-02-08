package com.xebisco.yield.components;

import com.xebisco.yield.Component;
import com.xebisco.yield.utils.Vector2;

public class Transform extends Component {
    public Vector2 position = new Vector2(), size = new Vector2(64, 64), middle;
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
}
