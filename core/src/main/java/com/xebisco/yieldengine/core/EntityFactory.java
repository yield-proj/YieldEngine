package com.xebisco.yieldengine.core;

import java.io.Serializable;

public interface EntityFactory extends Serializable {
    Entity createEntity();
}
