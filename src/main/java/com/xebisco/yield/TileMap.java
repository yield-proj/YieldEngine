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
    private TileSet actualTileSet;

    public TileMap(TileSet tileSet)
    {
        setActualTileSet(tileSet);
    }

    public void loadMap(Texture map, Vector2 grid)
    {
        while (!tiles.isEmpty())
            tiles.clear();
        for (int y = 0; y < map.getHeight(); y++)
        {
            for (int x = 0; x < map.getWidth(); x++)
            {
                final TilePos tilePos = new TilePos();
                tilePos.setPosition(new Vector2(x * grid.x, y * grid.y));
                tilePos.setColor(map.getPixelColor(x, y));
                for (TileID tileID : actualTileSet.getTiles())
                {
                    if (tileID.getColor().hashCode() == tilePos.getColor().hashCode())
                    {
                        tilePos.setTileID(tileID);
                        break;
                    }
                }
                if (tilePos.getTileID() != null)
                {
                    tilePos.setGraphicalObject(graphics.img(tilePos.getTileID().getTile(), tilePos.getPosition().x, tilePos.getPosition().y));
                    tilePos.getGraphicalObject().index = getEntity().getEntityIndex();
                    tiles.add(tilePos);
                }
            }
        }
    }

    @Override
    public void update(float delta)
    {
        if (actualTileSet != null && getEntity().isActive())
        {
            for (TilePos tile : tiles)
            {
                boolean process = !smartProcessing;
                if (!process)
                {
                    if (tile.getPosition().x - View.getActView().getCamera().getPosition().x + tile.getTileID().getTile().getWidth() > 0
                            && tile.getPosition().x - View.getActView().getCamera().getPosition().x < View.getActView().getWidth()
                            && tile.getPosition().y - View.getActView().getCamera().getPosition().y + tile.getTileID().getTile().getHeight() > 0
                            && tile.getPosition().y - View.getActView().getCamera().getPosition().y < View.getActView().getHeight())
                    {
                        process = true;
                    }
                }
                tile.getGraphicalObject().x = (int) (tile.getPosition().x);
                tile.getGraphicalObject().y = (int) (tile.getPosition().y);
                tile.getGraphicalObject().x2 = tile.getGraphicalObject().x + tile.getTileID().getTile().getWidth();
                tile.getGraphicalObject().y2 = tile.getGraphicalObject().y + tile.getTileID().getTile().getHeight();
                if (process)
                {
                    tile.getGraphicalObject().active = getEntity().isActive();
                    getEntity().transmit("processTile", tile);
                } else {
                    tile.getGraphicalObject().active = false;
                }
            }
        }
        else
        {
            graphics.shapeRends.removeIf(o ->
            {
                for (TilePos tilePos : tiles)
                {
                    if (tilePos.getGraphicalObject().hashCode() == o.hashCode())
                    {
                        return true;
                    }
                }
                return false;
            });
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

    public ArrayList<TilePos> getTiles()
    {
        return tiles;
    }

    public void setTiles(ArrayList<TilePos> tiles)
    {
        this.tiles = tiles;
    }
}
