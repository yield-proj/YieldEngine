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

package com.xebisco.yieldengine.editor.runtime.pack;

import com.xebisco.yieldengine.utils.Pair;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EditorEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = -5169409393577048649L;
    private String entityName = "Empty Entity";
    private boolean enabled = true;
    private List<EditorComponent> components = new ArrayList<>();
    private List<EditorEntity> children = new ArrayList<>();

    public transient List<Object> props;

    private File prefabFile;
    private EditorEntity parent;

    public EditorEntity() {
    }

    public boolean isChildOf(EditorEntity e) {
        EditorEntity a = this;
        while (a.parent != null) {
            if (a.parent.equals(e)) return true;
            a = a.parent;
        }
        return false;
    }

    public double rotation() {
        for (EditorComponent c : components) {
            if (c.className().equals("com.xebisco.yieldengine.Transform2D")) {
                for (Pair<Pair<String, String>, String[]> field : c.fields()) {
                    if (field.first().first().equals("zRotation")) {
                        return Double.parseDouble(field.second()[0]);
                    }
                }
            }
        }
        throw new IllegalStateException();
    }

    public Point2D.Double position() {
        for (EditorComponent c : components) {
            if (c.className().equals("com.xebisco.yieldengine.Transform2D")) {
                for (Pair<Pair<String, String>, String[]> field : c.fields()) {
                    if (field.first().first().equals("position")) {
                        return new Point2D.Double(Double.parseDouble(field.second()[0]), Double.parseDouble(field.second()[1]));
                    }
                }
            }
        }
        throw new IllegalStateException();
    }

    public Point2D.Double realPosition() {
        Point2D.Double rawPosition = position();
        if (parent != null) {
            Point2D.Double parentPosition = parent.realPosition();
            rawPosition.x += parentPosition.x;
            rawPosition.y += parentPosition.y;
        }
        return rawPosition;
    }

    public String anchor() {
        for (EditorComponent c : components) {
            if (c.className().equals("com.xebisco.yieldengine.Transform2D")) {
                for (Pair<Pair<String, String>, String[]> field : c.fields()) {
                    if (field.first().first().equals("anchor")) {
                        return field.second()[0];
                    }
                }
            }
        }
        throw new IllegalStateException();
    }

    public Point2D.Double scale() {
        for (EditorComponent c : components) {
            if (c.className().equals("com.xebisco.yieldengine.Transform2D")) {
                for (Pair<Pair<String, String>, String[]> field : c.fields()) {
                    if (field.first().first().equals("scale")) {
                        return new Point2D.Double(Double.parseDouble(field.second()[0]), Double.parseDouble(field.second()[1]));
                    }
                }
            }
        }
        throw new IllegalStateException();
    }

    public void setTransformPosition(double x, double y) {
        for (EditorComponent c : components) {
            if (c.className().equals("com.xebisco.yieldengine.Transform2D")) {
                for (Pair<Pair<String, String>, String[]> field : c.fields()) {
                    if (field.first().first().equals("position")) {
                        field.second()[0] = String.valueOf(x);
                        field.second()[1] = String.valueOf(y);
                        return;
                    }
                }
            }
        }
        throw new IllegalStateException();
    }

    public Point2D.Double anchorP(Point2D.Double size) {
        Point2D.Double a = new Point2D.Double();
        switch (anchor()) {
            case "WEST" -> {
                a.x = size.x / 2.;
            }
            case "EAST" -> {
                a.x = -size.x / 2.;
            }
            case "SOUTH" -> {
                a.y = -size.y / 2.;
            }
            case "NORTH" -> {
                a.y = size.y / 2.;
            }
            case "NORTHWEST" -> {
                a.x = size.x / 2.;
                a.y = size.y / 2.;
            }
            case "NORTHEAST" -> {
                a.x = -size.x / 2.;
                a.y = size.y / 2.;
            }
            case "SOUTHWEST" -> {
                a.x = size.x / 2.;
                a.y = -size.y / 2.;
            }
            case "SOUTHEAST" -> {
                a.x = -size.x / 2.;
                a.y = -size.y / 2.;
            }
        }
        return a;
    }

    @Override
    public String toString() {
        return entityName;
    }

    public String entityName() {
        return entityName;
    }

    public EditorEntity setEntityName(String entityName) {
        this.entityName = entityName;
        return this;
    }

    public List<EditorComponent> components() {
        return components;
    }

    public EditorEntity setComponents(List<EditorComponent> components) {
        this.components = components;
        return this;
    }

    public boolean enabled() {
        return enabled;
    }

    public EditorEntity setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public List<EditorEntity> children() {
        return children;
    }

    public EditorEntity setChildren(List<EditorEntity> children) {
        this.children = children;
        return this;
    }

    public EditorEntity parent() {
        return parent;
    }

    public EditorEntity setParent(EditorEntity parent) {
        this.parent = parent;
        return this;
    }

    public File prefabFile() {
        return prefabFile;
    }

    public EditorEntity setPrefabFile(File prefabFile) {
        this.prefabFile = prefabFile;
        return this;
    }

    public List<Object> props() {
        return props;
    }

    public EditorEntity setProps(List<Object> props) {
        this.props = props;
        return this;
    }
}