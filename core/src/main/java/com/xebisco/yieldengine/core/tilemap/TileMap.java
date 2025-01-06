package com.xebisco.yieldengine.core.tilemap;

import com.xebisco.yieldengine.core.Component;
import com.xebisco.yieldengine.core.Entity;
import com.xebisco.yieldengine.core.Transform;
import com.xebisco.yieldengine.core.components.Sprite;
import com.xebisco.yieldengine.core.io.texture.Texture;
import com.xebisco.yieldengine.core.io.texture.TextureMap;
import org.joml.Vector2f;

import java.io.Serializable;
import java.util.HashMap;

public class TileMap extends Component implements Serializable {
    private final HashMap<Integer, Tile> tileSet;
    private final int[][] map;
    private final int tileWidth, tileHeight;
    private final TextureMap textureMap;

    public TileMap(HashMap<Integer, Tile> tileSet, int[][] map, int tileWidth, int tileHeight, TextureMap textureMap) {
        this.tileSet = tileSet;
        this.map = map;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.textureMap = textureMap;
    }

    @Override
    public void onCreate() {
        Entity spritesEntity = new Entity("sprites", new Transform());
        spritesEntity.addToParent(getEntity());
        for(int y = 0; y < map[0].length; y++) {
            for(int x = 0; x < map.length; x++) {
                int tile = map[y][x];
                if(tile == -1) continue;
                Tile tileC = tileSet.get(tile);
                Texture texture = tileC.getTexture();
                Sprite sprite = new Sprite(tileC.getColor(), tileC.isFitTexture() ? new Vector2f(tileWidth, tileHeight) : new Vector2f(texture.getWidth(), texture.getHeight()), texture);
                //sprite.setOffset(new Vector2f(x * tileWidth + tileWidth / 2f, -y * tileHeight - tileHeight / 2f));
                spritesEntity.getComponents().add(sprite);
            }
        }
    }

    public HashMap<Integer, Tile> getTileSet() {
        return tileSet;
    }

    public int[][] getMap() {
        return map;
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public int getTileHeight() {
        return tileHeight;
    }

    public TextureMap getTextureMap() {
        return textureMap;
    }
}
