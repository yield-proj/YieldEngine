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

package com.xebisco.yield.editor.app.editor;

import com.xebisco.yield.editor.app.Global;
import com.xebisco.yield.uiutils.Srd;
import com.xebisco.yield.uiutils.props.PositionProp;
import com.xebisco.yield.utils.Pair;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        clearComponents();
    }

    public void clearComponents() {
        components.clear();
        EditorComponent transform;
        try {
            transform = new EditorComponent(Global.yieldEngineClassLoader.loadClass("com.xebisco.yield.Transform2D"));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        components.add(transform);
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
            if (c.className().equals("com.xebisco.yield.Transform2D")) {
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
            if (c.className().equals("com.xebisco.yield.Transform2D")) {
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
            if (c.className().equals("com.xebisco.yield.Transform2D")) {
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
            if (c.className().equals("com.xebisco.yield.Transform2D")) {
                for (Pair<Pair<String, String>, String[]> field : c.fields()) {
                    if (field.first().first().equals("scale")) {
                        return new Point2D.Double(Double.parseDouble(field.second()[0]), Double.parseDouble(field.second()[1]));
                    }
                }
            }
        }
        throw new IllegalStateException();
    }

    public void setPosition(double x, double y) {
        if (props != null) {
            props.forEach(c -> {
                if (((ComponentProp) c).name().equals("com.xebisco.yield.Transform2D")) {
                    ((ComponentProp) c).props.forEach(c1 -> {
                        if (c1.name().equals("position")) {
                            ((PositionProp) c1).setValue((float) x, (float) y);
                        }
                    });
                }
            });
        }
        setTransformPosition(x, y);
    }

    public void setTransformPosition(double x, double y) {
        for (EditorComponent c : components) {
            if (c.className().equals("com.xebisco.yield.Transform2D")) {
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

    public void draw(Graphics2D g) {
        Point2D.Double position = realPosition(), scale = scale();
        for (EditorComponent c : components) {
            AffineTransform t = new AffineTransform(g.getTransform());

            g.rotate(Math.toRadians(-rotation()), position.x, -position.y);
            if (c.className().equals("com.xebisco.yield.RectangleMesh")) {

                Color color = new Color(0, 0, 0, 1);
                for (Pair<Pair<String, String>, String[]> field : c.fields()) {
                    if (field.first().first().equals("color")) {
                        color = new Color(Float.parseFloat(field.second()[0]), Float.parseFloat(field.second()[1]), Float.parseFloat(field.second()[2]), Float.parseFloat(field.second()[3]));
                        break;
                    }
                }
                Point2D.Double size = new Point2D.Double();
                for (Pair<Pair<String, String>, String[]> field : c.fields()) {
                    if (field.first().first().equals("size")) {
                        size = new Point2D.Double(Double.parseDouble(field.second()[0]), Double.parseDouble(field.second()[1]));
                        break;
                    }
                }

                Point2D aP = anchorP(size);
                g.translate(aP.getX(), aP.getY());

                g.setColor(color);
                g.fill(new Rectangle2D.Double(position.x - (size.x * scale.x / 2), -position.y - (size.y * scale.y / 2), size.x * scale.x, size.y * scale.y));
                g.setTransform(t);
            } else {
                if (c.className().equals("com.xebisco.yield.texture.TexturedRectangleMesh")) {
                    Color color = new Color(0, 0, 0, 1);
                    for (Pair<Pair<String, String>, String[]> field : c.fields()) {
                        if (field.first().first().equals("color")) {
                            color = new Color(Float.parseFloat(field.second()[0]), Float.parseFloat(field.second()[1]), Float.parseFloat(field.second()[2]), Float.parseFloat(field.second()[3]));
                            break;
                        }
                    }
                    Point2D.Double size = new Point2D.Double();
                    for (Pair<Pair<String, String>, String[]> field : c.fields()) {
                        if (field.first().first().equals("size")) {
                            size = new Point2D.Double(Double.parseDouble(field.second()[0]), Double.parseDouble(field.second()[1]));
                            break;
                        }
                    }

                    Point2D aP = anchorP(size);
                    g.translate(aP.getX(), aP.getY());

                    AffineTransform imageTransform = new AffineTransform();

                    BufferedImage image = Srd.getImage(Objects.requireNonNull(EditorEntity.class.getResource("/logo/logo.png")));
                    for (Pair<Pair<String, String>, String[]> field : c.fields()) {
                        if (field.first().first().equals("texture")) {
                            //image = new Point2D.Double(Double.parseDouble(   field.second()[0]), Double.parseDouble(field.second()[1]));
                            if (field.second().length > 0 && field.second()[0] != null && !field.second()[0].equals("null"))
                                try {
                                    image = Srd.getImage(field.second()[0]);
                                } catch (RuntimeException ignore) {
                                }
                            break;
                        }
                    }


                    imageTransform.translate(position.x - (size.x * scale.x / 2), -position.y - (size.y * scale.y / 2));
                    imageTransform.scale(size.x * scale.x / (double) image.getWidth(), size.y * scale.y / (double) image.getHeight());

                    g.drawImage(applyShader(image, color), imageTransform, null);
                    g.setTransform(t);
                }
            }
        }
        for (EditorEntity child : children) child.draw(g);
    }

    public BufferedImage applyShader(BufferedImage input, Color factor) {
        if (factor.equals(Color.WHITE)) return input;
        BufferedImage output = new BufferedImage(input.getWidth(), input.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < input.getWidth(); x++) {
            for (int y = 0; y < input.getHeight(); y++) {
                int argb = input.getRGB(x, y);

                int a = (argb >> 24) & 0xFF;
                int r = (argb >> 16) & 0xFF;
                int g = (argb >> 8) & 0xFF;
                int b = argb & 0xFF;

                a = a * factor.getAlpha() / 255;
                r = r * factor.getRed() / 255;
                g = g * factor.getGreen() / 255;
                b = b * factor.getBlue() / 255;

                argb = (a << 24) | (r << 16) | (g << 8) | b;

                output.setRGB(x, y, argb);
            }
        }
        return output;
    }

    public boolean inside(Point2D.Double mousePosition) {
        for (EditorEntity child : children) {
            if (child.inside(mousePosition)) return true;
        }
        Dimension d = size();
        if (d.width < 8) d.width = 8;
        if (d.height < 8) d.height = 8;
        return inside(mousePosition, d);
    }

    public boolean inside(Point2D.Double mousePosition, Dimension size) {

        Point2D.Double a = anchorP(new Point2D.Double(size.width, size.height)), pp = realPosition();
        Point2D.Double p = new Point2D.Double(pp.x + a.x, pp.y - a.y);

        return p.x - size.width / 2. < mousePosition.x && p.x + size.width / 2. > mousePosition.x && -p.y - size.height / 2. < mousePosition.y && -p.y + size.height / 2. > mousePosition.y;
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

    public Dimension size() {
        Dimension size = new Dimension(0, 0);
        Point2D.Double scale = scale();
        for (EditorComponent c : components) {
            try {
                Class<?> cl = Global.yieldEngineClassLoader.loadClass(c.className());
                if (cl.isAnnotationPresent(Global.SIZE_ANNOTATION)) {
                    Point2D.Double s1 = new Point2D.Double();
                    c.fields().forEach(p -> {
                        if (p.first().first().equals("size")) {
                            s1.x = Double.parseDouble(p.second()[0]);
                            s1.y = Double.parseDouble(p.second()[1]);
                        }
                    });
                    //TODO component actual size (100, 100)
                    Dimension f = new Dimension((int) (scale.x * s1.x), (int) (scale.y * s1.y));
                    if (size.width < f.width) size.width = f.width;
                    if (size.height < f.height) size.height = f.height;
                }
            } catch (ClassNotFoundException ignore) {
            }
        }
        return size;
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
}