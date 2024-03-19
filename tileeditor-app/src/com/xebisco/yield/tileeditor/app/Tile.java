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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class Tile implements Serializable {
    @Serial
    private static final long serialVersionUID = 1351230705453406099L;

    private String name, instanceEntity;

    private transient BufferedImage image;

    private int width, height;

    public Tile(String name, String instanceEntity, BufferedImage image, int width, int height) {
        this.instanceEntity = instanceEntity;
        this.image = image;
        this.name = name;
        this.width = width;
        this.height = height;
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        ImageIO.write(image, "png", out);
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
            image = ImageIO.read(in);
    }

    public BufferedImage image() {
        return image;
    }

    public Tile setImage(BufferedImage image) {
        this.image = image;
        return this;
    }

    public String name() {
        return name;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public Tile setName(String name) {
        this.name = name;
        return this;
    }

    public Tile setWidth(int width) {
        this.width = width;
        return this;
    }

    public Tile setHeight(int height) {
        this.height = height;
        return this;
    }

    public String instanceEntity() {
        return instanceEntity;
    }

    public Tile setInstanceEntity(String instanceEntity) {
        this.instanceEntity = instanceEntity;
        return this;
    }
}
