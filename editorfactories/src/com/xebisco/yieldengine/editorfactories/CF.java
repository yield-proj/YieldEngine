package com.xebisco.yieldengine.editorfactories;

import com.xebisco.yieldengine.core.Entity;
import com.xebisco.yieldengine.core.EntityFactory;
import com.xebisco.yieldengine.core.Transform;
import com.xebisco.yieldengine.core.components.Rectangle;
import com.xebisco.yieldengine.editorfactories.components.CC;
import org.joml.Vector2f;

public class CF implements EntityFactory {
    @Override
    public Entity createEntity() {
        Entity e = new Entity("Test1", new Transform());
        e.getComponents().add(new Rectangle().setSize(new Vector2f(10, 10)).setColor(0xFFFF0000));
        e.getComponents().add(new CC());
        return e;
    }
}