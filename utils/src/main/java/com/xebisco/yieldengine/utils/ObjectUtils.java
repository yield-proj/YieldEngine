package com.xebisco.yieldengine.utils;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ObjectUtils {
    public final static HashMap<Class<?>, Creator> OBJECT_CREATORS = new HashMap<>();

    static {
        OBJECT_CREATORS.put(File.class, () -> new File(""));
    }

    public interface Creator {
        Object create();
    }

    public static <T> T newInstance(Class<T> clazz) {
        Creator creator = OBJECT_CREATORS.get(clazz);
        if(creator == null) {
            try {
                return clazz.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | InvocationTargetException | IllegalAccessException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        } else {
            //noinspection unchecked
            return (T) creator.create();
        }
    }

    public static void apply(Map<String, Serializable> properties, Object object) {
        Class<?> objectClass = object.getClass();
        for (Map.Entry<String, Serializable> entry : properties.entrySet()) {
            try {
                Field field = objectClass.getDeclaredField(entry.getKey());
                if (entry.getValue() != null && !entry.getValue().getClass().isAssignableFrom(field.getType())) {
                    throw new RuntimeException(String.format("%s is not assignable from %s.", entry.getKey(), entry.getValue().getClass().getName()));
                }
                field.setAccessible(true);
                field.set(object, entry.getValue());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static ArrayList<Pair<String, Pair<Serializable, Field>>> get(Object object) {
        ArrayList<Pair<String, Pair<Serializable, Field>>> values = new ArrayList<>();
        Class<?> objectClass = object.getClass();
        for (Field field : objectClass.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(Visible.class)) {
                try {
                    if (!Serializable.class.isAssignableFrom(field.getType())) {
                        throw new RuntimeException(String.format("%s is not assignable from java.io.Serializable", field.getName()));
                    }
                    if (Modifier.isFinal(field.getModifiers()))
                        throw new RuntimeException(String.format("%s is final", field.getName()));
                    if (Modifier.isStatic(field.getModifiers()))
                        throw new RuntimeException(String.format("%s is static", field.getName()));
                    values.add(new Pair<>(field.getName(), new Pair<>((Serializable) field.get(object), field)));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return values;
    }
}
