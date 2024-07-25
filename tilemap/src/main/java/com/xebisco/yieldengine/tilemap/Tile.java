package com.xebisco.yieldengine.tilemap;

import com.xebisco.yieldengine.core.EntityFactory;
import org.joml.Vector4f;

import java.io.Serializable;

public class Tile implements Serializable {
    private final PositionAndSize positionAndSize;
    private final boolean fitTexture;
    private final Vector4f color;
    private final EntityFactory entityFactory;

    public Tile(PositionAndSize positionAndSize, boolean fitTexture, Vector4f color, EntityFactory entityFactory) {
        this.positionAndSize = positionAndSize;
        this.fitTexture = fitTexture;
        this.color = color;
        this.entityFactory = entityFactory;
    }

    public PositionAndSize getPositionAndSize() {
        return positionAndSize;
    }

    public boolean isFitTexture() {
        return fitTexture;
    }

    public Vector4f getColor() {
        return color;
    }

    public EntityFactory getEntityFactory() {
        return entityFactory;
    }
}
