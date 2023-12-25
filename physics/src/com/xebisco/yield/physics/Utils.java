package com.xebisco.yield.physics;

import com.xebisco.yield.ImmutableVector2D;
import com.xebisco.yield.Scene;
import com.xebisco.yield.Vector2D;
import org.jbox2d.common.Vec2;

public final class Utils {
    public static Vec2 toVec2(Vector2D v) {
        return new Vec2((float) v.x(), (float) v.y());
    }

    public static Vector2D toVector2D(Vec2 v) {
        return new Vector2D(v.x, v.y);
    }
    public static ImmutableVector2D toImmutableVector2D(Vec2 v) {
        return new ImmutableVector2D(v.x, v.y);
    }
}
