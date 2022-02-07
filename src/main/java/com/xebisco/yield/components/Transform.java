package com.xebisco.yield.components;

import com.xebisco.yield.Component;
import com.xebisco.yield.utils.Vector2;

public class Transform extends Component {
    public Vector2 position = new Vector2(), scale = new Vector2(64, 64);
    public float rotation = 0;
}
