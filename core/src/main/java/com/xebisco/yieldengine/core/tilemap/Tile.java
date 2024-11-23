package com.xebisco.yieldengine.core.tilemap;

import com.xebisco.yieldengine.core.EntityFactory;
import com.xebisco.yieldengine.core.io.texture.Texture;
import org.joml.Vector4f;

import java.io.Serializable;

public class Tile implements Serializable {
    private final Texture texture;
    private final boolean fitTexture;
    private final int color;
    private final EntityFactory entityFactory;

    public Tile(Texture texture, boolean fitTexture, int color, EntityFactory entityFactory) {
        this.texture = texture;
        this.fitTexture = fitTexture;
        this.color = color;
        this.entityFactory = entityFactory;
    }

    public Texture getTexture() {
        return texture;
    }

    public boolean isFitTexture() {
        return fitTexture;
    }

    public int getColor() {
        return color;
    }

    public EntityFactory getEntityFactory() {
        return entityFactory;
    }
}
