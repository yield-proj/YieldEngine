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

import java.util.ArrayList;
import java.util.List;

public class TileMap extends SimpleRenderable {
    private List<YldPair<Vector2, Tile>> tiles;

    private boolean dynamicProcessing = true, process = true;

    private float processTime = .05f, actualProcessTime;

    private Vector2 grid;

    public static TileMap loadTileMap(Color[][] colors, TileMap tileMap, TileSet tileSet, Vector2 grid) {
        tileMap.tiles = new ArrayList<>();
        tileMap.grid = grid.get();
        if (colors.length > 0) {
            for (int x = 0; x < colors.length; x++) {
                for (int y = 0; y < colors[0].length; y++) {
                    try {
                        TileGen gen = tileSet.getTiles().get(colors[x][y].getRGB());
                        MappedTiles mappedTiles = new MappedTiles(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
                        try {
                            mappedTiles.setBottom(tileSet.getTiles().get(colors[x][y + 1].getRGB()).getTile());
                        } catch (ArrayIndexOutOfBoundsException ignore) {
                        }
                        try {
                            mappedTiles.setLeft(tileSet.getTiles().get(colors[x - 1][y].getRGB()).getTile());
                        } catch (ArrayIndexOutOfBoundsException ignore) {
                        }
                        try {
                            mappedTiles.setTop(tileSet.getTiles().get(colors[x][y - 1].getRGB()).getTile());
                        } catch (ArrayIndexOutOfBoundsException ignore) {
                        }
                        try {
                            mappedTiles.setRight(tileSet.getTiles().get(colors[x + 1][y].getRGB()).getTile());
                        } catch (ArrayIndexOutOfBoundsException ignore) {
                        }

                        try {
                            mappedTiles.setOnlyRightBottom(tileSet.getTiles().get(colors[x + 1][y + 1].getRGB()).getTile());
                        } catch (ArrayIndexOutOfBoundsException ignore) {
                        }
                        try {
                            mappedTiles.setOnlyBottomLeft(tileSet.getTiles().get(colors[x - 1][y + 1].getRGB()).getTile());
                        } catch (ArrayIndexOutOfBoundsException ignore) {
                        }
                        try {
                            mappedTiles.setOnlyTopLeft(tileSet.getTiles().get(colors[x - 1][y - 1].getRGB()).getTile());
                        } catch (ArrayIndexOutOfBoundsException ignore) {
                        }
                        try {
                            mappedTiles.setOnlyTopRight(tileSet.getTiles().get(colors[x + 1][y - 1].getRGB()).getTile());
                        } catch (ArrayIndexOutOfBoundsException ignore) {
                        }
                        gen.setComparingMapped(mappedTiles);
                        tileMap.tiles.add(new YldPair<>(new Vector2(x * tileMap.grid.x, y * tileMap.grid.y), gen.getTile()));
                        gen.setComparingMapped(null);
                    } catch (NullPointerException ignore) {

                    }
                }
            }
        }
        return tileMap;
    }

    public static TileMap loadTileMap(Color[][] colors, TileMap tileMap, TileSet tileSet) {
        return loadTileMap(colors, tileMap, tileSet, new Vector2(32, 32));
    }

    @Override
    public void update(float delta) {
        actualProcessTime -= delta;
    }

    @Override
    public void render(SampleGraphics graphics) {
        super.render(graphics);
        Vector2 cam = scene.getView().getTransform().position.get();
        Transform t = getTransform();
        for (int i = 0; i < tiles.size(); i++) {
            YldPair<Vector2, Tile> pair = tiles.get(i);
            Tile tile = pair.getSecond();
            if (tile != null) {
                Vector2 pos = pair.getFirst().sum(t.position).sum(tile.getOffSet()).subt(cam).subt(tile.getSize().mul(t.scale).div(2f)),
                        size = tile.getSize().mul(t.scale);
                boolean render = true;
                if (dynamicProcessing && (transform.rotation % 360 == 0 || transform.rotation == 0)) {
                    if (!(pos.x + size.x / 2f >= 0 &&
                            pos.x - size.x / 2f < scene.getView().getWidth() &&
                            pos.y + size.y / 2f >= 0 &&
                            pos.y - size.y / 2f < scene.getView().getHeight())) {
                        render = false;
                    }
                }
                if (render) {
                    graphics.drawTexture(tile.getTexture(), pos, size);
                    if (actualProcessTime <= 0 && process)
                        getEntity().transmit("processTile", pos, tile.getLayer());
                }
            }
        }
        if (actualProcessTime <= 0) {
            actualProcessTime = processTime;
        }
    }

    public List<YldPair<Vector2, Tile>> getTiles() {
        return tiles;
    }

    public void setTiles(List<YldPair<Vector2, Tile>> tiles) {
        this.tiles = tiles;
    }

    public Vector2 getGrid() {
        return grid;
    }

    public void setGrid(Vector2 grid) {
        this.grid = grid;
    }

    public boolean isDynamicProcessing() {
        return dynamicProcessing;
    }

    public void setDynamicProcessing(boolean dynamicProcessing) {
        this.dynamicProcessing = dynamicProcessing;
    }

    public float getProcessTime() {
        return processTime;
    }

    public void setProcessTime(float processTime) {
        this.processTime = processTime;
    }

    public float getActualProcessTime() {
        return actualProcessTime;
    }

    public void setActualProcessTime(float actualProcessTime) {
        this.actualProcessTime = actualProcessTime;
    }

    public boolean isProcess() {
        return process;
    }

    public void setProcess(boolean process) {
        this.process = process;
    }
}
