package com.xebisco.yieldengine.editorfactories;

import com.xebisco.yieldengine.core.Entity;
import com.xebisco.yieldengine.core.EntityFactory;
import com.xebisco.yieldengine.core.Transform;
import com.xebisco.yieldengine.editorfactories.components.CamControlComp;

public class CamControl implements EntityFactory {
    @Override
    public Entity createEntity() {
        Entity e = new Entity("CamControl", new Transform());
        e.getComponents().add(new CamControlComp());
        return e;
    }
}
