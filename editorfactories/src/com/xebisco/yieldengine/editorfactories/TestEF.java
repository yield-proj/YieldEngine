package com.xebisco.yieldengine.editorfactories;

import com.xebisco.yieldengine.core.Entity;
import com.xebisco.yieldengine.core.EntityFactory;
import com.xebisco.yieldengine.core.Transform;
import com.xebisco.yieldengine.core.components.Rectangle;

public class TestEF implements EntityFactory {
    @Override
    public Entity createEntity() {
        Entity e = new Entity("Test", new Transform());
        e.getComponents().add(new Rectangle());
        return e;
    }
}
