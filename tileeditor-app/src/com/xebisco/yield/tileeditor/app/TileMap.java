/*
 * Copyright [2022-2024] [Xebisco]
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xebisco.yield.tileeditor.app;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TileMap implements Serializable {
    @Serial
    private static final long serialVersionUID = 649405555207546260L;
    private final TileSet tileSet;

    private final int[][] map;
    private String entityCreationClassName;

    public TileMap(TileSet tileSet, int[][] map) {
        this.tileSet = tileSet;
        this.map = map;
    }

    public TileSet tileSet() {
        return tileSet;
    }

    public int[][] map() {
        return map;
    }

    public String entityCreationClassName() {
        return entityCreationClassName;
    }

    public TileMap setEntityCreationClassName(String entityCreationClassName) {
        this.entityCreationClassName = entityCreationClassName;
        return this;
    }
}
