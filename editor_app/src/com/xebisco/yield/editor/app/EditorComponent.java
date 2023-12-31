package com.xebisco.yield.editor.app;

import com.xebisco.yield.editor.utils.Visible;

import java.io.Serial;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EditorComponent implements Serializable {
    @Serial
    private static final long serialVersionUID = -9027305243114159863L;
    private List<Pair<Pair<String, String>, String[]>> fields = new ArrayList<>();
    private final String className;

    private boolean canRemove = true;

    public EditorComponent(Class<?> clazz) {
        this.className = clazz.getName();
        Object o;
        try {
            o = clazz.getConstructor().newInstance();
        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            try {
                String[] value;
                if(field.getType().getName().equals("com.xebisco.yield.Vector2D")) {
                    Object obj = field.get(o);
                    value = new String[]{String.valueOf(obj.getClass().getMethod("x").invoke(obj)), String.valueOf(obj.getClass().getMethod("y").invoke(obj))};
                }
                    else value = new String[]{String.valueOf(field.get(o))};
                if (field.isAnnotationPresent(Visible.class))
                    fields.add(new Pair<>(new Pair<>(field.getName(), field.getType().getName()), value));
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public List<Pair<Pair<String, String>, String[]>> fields() {
        return fields;
    }

    public EditorComponent setFields(List<Pair<Pair<String, String>, String[]>> fields) {
        this.fields = fields;
        return this;
    }

    public boolean canRemove() {
        return canRemove;
    }

    public EditorComponent setCanRemove(boolean canRemove) {
        this.canRemove = canRemove;
        return this;
    }

    public String className() {
        return className;
    }
}
