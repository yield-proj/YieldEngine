/*
 * Copyright [2022] [Xebisco]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xebisco.yield;

import com.xebisco.yield.utils.Conversions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class TileMap extends Component
{
    private boolean visible = true, smartProcessing = true;
    private ArrayList<TilePos> tiles = new ArrayList<>();
    private HashSet<Obj> actObjs = new HashSet<>(), allObjs = new HashSet<>();
    private TileSet actualTileSet;

    public TileMap(TileSet tileSet)
    {
        setActualTileSet(tileSet);
    }

    public void loadMap(Texture map, Vector2 grid)
    {
        for (int y = 0; y < map.getHeight(); y++)
        {
            for (int x = 0; x < map.getWidth(); x++)
            {
                final TilePos tilePos = new TilePos();
                tilePos.setPosition(new Vector2(x * grid.x, y * grid.y));
                tilePos.setColor(map.getPixelColor(x, y));
                tiles.add(tilePos);
            }
        }
    }

    @Override
    public void update(float delta)
    {
        if (actualTileSet != null && getEntity().isActive())
        {
            graphics.shapeRends.removeAll(actObjs);
            while (!actObjs.isEmpty())
                actObjs.clear();
            for (TilePos tile : tiles)
            {
                TileID toBe = null;
                for (TileID tileID : actualTileSet.getTiles())
                {
                    if(tileID.getColor().hashCode() == tile.getColor().hashCode() && tileID.getColor().equals(tile.getColor())) {
                        toBe = tileID;
                        break;
                    }
                }
                tile.setTileID(toBe);
                Obj obj = graphics.img(tile.getTileID().getTile(), tile.getPosition().x, tile.getPosition().y);
                actObjs.add(obj);
                allObjs.add(obj);
                getEntity().transmit("processTile", tile);
            }
        }
        else
        {
            graphics.shapeRends.removeIf(allObjs::contains);
        }
    }

    @Override
    public void onDestroy()
    {

    }

    public boolean isVisible()
    {
        return visible;
    }

    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }

    public TileSet getActualTileSet()
    {
        return actualTileSet;
    }

    public void setActualTileSet(TileSet actualTileSet)
    {
        this.actualTileSet = actualTileSet;
    }

    public boolean isSmartProcessing()
    {
        return smartProcessing;
    }

    public void setSmartProcessing(boolean smartProcessing)
    {
        this.smartProcessing = smartProcessing;
    }

    public HashSet<Obj> getAllObjs()
    {
        return allObjs;
    }

    public void setAllObjs(HashSet<Obj> allObjs)
    {
        this.allObjs = allObjs;
    }

    public ArrayList<TilePos> getTiles()
    {
        return tiles;
    }

    public void setTiles(ArrayList<TilePos> tiles)
    {
        this.tiles = tiles;
    }

    public HashSet<Obj> getActObjs()
    {
        return actObjs;
    }

    public void setActObjs(HashSet<Obj> actObjs)
    {
        this.actObjs = actObjs;
    }
}
