package com.xebisco.yield.editor.app;

import com.xebisco.yield.editor.utils.AffectsEditorEntitySize;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EditorEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = -5169409393577048649L;
    private String entityName = "Empty Entity";
    private boolean enabled = true;
    private List<EditorComponent> components = new ArrayList<>();
    private List<EditorEntity> children = new ArrayList<>();

    public EditorEntity() {
        clearComponents();
    }

    public void clearComponents() {
        components.clear();
        EditorComponent transform;
        try {
            transform = new EditorComponent(Srd.yieldEngineClassLoader.loadClass("com.xebisco.yield.Transform2D"));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        components.add(transform);
    }

    public Point2D.Double position() {
        for(EditorComponent c : components) {
            if(c.className().equals("com.xebisco.yield.Transform2D")) {
                for(Pair<Pair<String, String>, String[]> field : c.fields()) {
                    if(field.first().first().equals("position")) {
                        return new Point2D.Double(Double.parseDouble(field.second()[0]), Double.parseDouble(field.second()[1]));
                    }
                }
            }
        }
        throw new IllegalStateException();
    }

    public Point2D.Double scale() {
        for(EditorComponent c : components) {
            if(c.className().equals("com.xebisco.yield.Transform2D")) {
                for(Pair<Pair<String, String>, String[]> field : c.fields()) {
                    if(field.first().first().equals("scale")) {
                        return new Point2D.Double(Double.parseDouble(field.second()[0]), Double.parseDouble(field.second()[1]));
                    }
                }
            }
        }
        throw new IllegalStateException();
    }

    public void setPosition(double x, double y) {
        for(EditorComponent c : components) {
            if(c.className().equals("com.xebisco.yield.Transform2D")) {
                for(Pair<Pair<String, String>, String[]> field : c.fields()) {
                    if(field.first().first().equals("position")) {
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
        Point2D.Double position = position(), scale = scale();
        for(EditorComponent c : components) {
            if(c.className().equals("com.xebisco.yield.SquareMesh")) {
                Color color = new Color(0, 0, 0,1);
                for(Pair<Pair<String, String>, String[]> field : c.fields()) {
                    if(field.first().first().equals("color")) {
                        color = new Color(Integer.parseInt(field.second()[0], 16), true);
                        break;
                    }
                }
                Point2D.Double size = new Point2D.Double();
                for(Pair<Pair<String, String>, String[]> field : c.fields()) {
                    if(field.first().first().equals("size")) {
                        size = new Point2D.Double(Double.parseDouble(field.second()[0]), Double.parseDouble(field.second()[1]));
                        break;
                    }
                }
                g.setColor(color);
                g.fill(new Rectangle2D.Double(position.x - (size.x * scale.x / 2), position.y - (size.y * scale.y / 2), size.x * scale.x, size.y * scale.y));
            }
        }
    }

    public Dimension size() {
        Dimension size = new Dimension(100, 100);
        Point2D.Double scale = scale();
        for(EditorComponent c : components) {
            try {
                Class<?> cl = Srd.yieldEngineClassLoader.loadClass(c.className());
                if(cl.isAnnotationPresent(AffectsEditorEntitySize.class)) {
                    //TODO component actual size (100, 100)
                    Dimension f = new Dimension((int) (scale.x * 100), (int) (scale.y * 100));
                    if(size.width < f.width)
                        size.width = f.width;
                    if(size.height < f.height)
                        size.height = f.height;
                }
            } catch (ClassNotFoundException ignore) {
            }
        }
        return size;
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
}
