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

package com.xebisco.yield.editor.app;

import com.xebisco.yield.editor.annotations.Visible;
import com.xebisco.yield.editor.app.editor.ComponentProp;
import com.xebisco.yield.editor.app.editor.Editor;
import com.xebisco.yield.editor.app.editor.FontFileProp;
import com.xebisco.yield.editor.runtime.pack.EditorComponent;
import com.xebisco.yield.editor.runtime.pack.EditorEntity;
import com.xebisco.yield.uiutils.Srd;
import com.xebisco.yield.uiutils.props.PositionProp;
import com.xebisco.yield.utils.Pair;
import com.xebisco.yield.utils.Reversed;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.regex.Matcher;

public class Global {
    public static Properties appProps = new Properties();
    public static final String BUILD = "prelaunch-trintaabr0-2024", VERSION = "2024.0pl";

    public static Font defaultFont;

    public static List<File> listf(File directory) {
        List<File> files = new ArrayList<>();

        File[] fList = directory.listFiles();
        if (fList != null) for (File file : fList) {
            if (file.isFile()) {
                files.add(file);
            } else if (file.isDirectory()) {
                files.addAll(listf(file));
            }
        }
        return files;
    }

    public static void addFields(Class<?> clazz, Editor editor, List<Pair<Pair<String, String>, String[]>> fields) {
        Object o;
        try {
            o = clazz.getConstructor().newInstance();
        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        Class<?> c = clazz;
        do {
            extractFields(c, o, editor, fields, true);
            c = c.getSuperclass();
        } while (!c.isPrimitive() && c.getSuperclass() != null);
    }

    public static Dimension size(EditorEntity entity, Editor editor) {
        Dimension size = new Dimension(0, 0);
        Point2D.Double scale = entity.scale();
        for (EditorComponent c : entity.components()) {
            try {
                Class<?> cl = editor.yieldEngineClassLoader.loadClass(c.className());
                if (cl.isAnnotationPresent(editor.SIZE_ANNOTATION)) {
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

    public static void draw(Graphics2D g, EditorEntity entity, Editor editor) {
        Point2D.Double position = entity.realPosition(), scale = entity.scale();
        for (EditorComponent c : entity.components()) {
            AffineTransform t = new AffineTransform(g.getTransform());

            g.rotate(Math.toRadians(-entity.rotation()), position.x, -position.y);
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

                Point2D aP = entity.anchorP(size);
                g.translate(aP.getX(), aP.getY());

                g.setColor(color);
                g.fill(new Rectangle2D.Double(position.x - (size.x * scale.x / 2), -position.y - (size.y * scale.y / 2), size.x * scale.x, size.y * scale.y));
            } else if (c.className().equals("com.xebisco.yield.texture.TexturedRectangleMesh")) {
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

                Point2D aP = entity.anchorP(size);
                g.translate(aP.getX(), aP.getY());

                AffineTransform imageTransform = new AffineTransform();

                BufferedImage image = Srd.getImage(Objects.requireNonNull(EditorEntity.class.getResource("/logo/logo.png")));
                for (Pair<Pair<String, String>, String[]> field : c.fields()) {
                    if (field.first().first().equals("texture")) {
                        //image = new Point2D.Double(Double.parseDouble(   field.second()[0]), Double.parseDouble(field.second()[1]));
                        if (field.second().length > 0 && field.second()[0] != null && !field.second()[0].equals("null"))
                            try {
                                image = Srd.getImage(new File(editor.project().assetsDirectory(), field.second()[0]).getAbsolutePath());
                            } catch (RuntimeException ignore) {
                            }
                        break;
                    }
                }


                imageTransform.translate(position.x - (size.x * scale.x / 2), -position.y - (size.y * scale.y / 2));
                imageTransform.scale(size.x * scale.x / (double) image.getWidth(), size.y * scale.y / (double) image.getHeight());

                g.drawImage(applyShader(image, color), imageTransform, null);
            } else if (c.className().equals("com.xebisco.yield.TextMesh")) {
                Color color = new Color(0, 0, 0, 1);
                for (Pair<Pair<String, String>, String[]> field : c.fields()) {
                    if (field.first().first().equals("color")) {
                        color = new Color(Float.parseFloat(field.second()[0]), Float.parseFloat(field.second()[1]), Float.parseFloat(field.second()[2]), Float.parseFloat(field.second()[3]));
                        break;
                    }
                }

                Font font = defaultFont;

                for (Pair<Pair<String, String>, String[]> field : c.fields()) {
                    if (field.first().first().equals("font")) {
                        if (!(field.second()[0] == null || field.second()[0].isEmpty() || field.second()[0].equals("null"))) {
                            Matcher m = FontFileProp.SIZEP.matcher(field.second()[0]);
                            if (m.find()) {
                                try {
                                    if (!(m.group(1) == null || m.group(1).isEmpty() || m.group(1).equals("null")))
                                        font = Font.createFont(Font.TRUETYPE_FONT, new File(m.group(1))).deriveFont(Float.parseFloat(m.group(2)));
                                } catch (FontFormatException | IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            break;
                        }
                    }
                }

                String contents = "error_loading";

                for (Pair<Pair<String, String>, String[]> field : c.fields()) {
                    if (field.first().first().equals("contents")) {
                        if (field.second()[0] != null)
                            contents = field.second()[0];
                        break;
                    }
                }
                g.setFont(font);
                g.setColor(color);

                Rectangle2D rect = g.getFontMetrics().getStringBounds(contents, g);

                Point2D.Double size = new Point2D.Double(rect.getWidth(), rect.getHeight() / 2);
                Point2D aP = entity.anchorP(size);
                g.translate(aP.getX(), aP.getY());
                g.drawString(contents, (float) (-size.getX() / 2 + position.x), (float) (size.getY() / 2 - position.y));
            }
            g.setTransform(t);
        }
        for (EditorEntity child : Reversed.reversed(entity.children())) draw(g, child, editor);
    }

    public static EditorComponent sameAs(EditorComponent component, Editor editor) {
        try {
            return component.sameAs(component.fields(), Global.addFields(editor.yieldEngineClassLoader.loadClass(component.className()), editor, new EditorComponent(component.className())));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static BufferedImage applyShader(BufferedImage input, Color factor) {
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

    public static boolean inside(Point2D.Double mousePosition, Dimension size, EditorEntity entity) {

        Point2D.Double a = entity.anchorP(new Point2D.Double(size.width, size.height)), pp = entity.realPosition();
        Point2D.Double p = new Point2D.Double(pp.x + a.x, pp.y - a.y);

        return p.x - size.width / 2. < mousePosition.x && p.x + size.width / 2. > mousePosition.x && -p.y - size.height / 2. < mousePosition.y && -p.y + size.height / 2. > mousePosition.y;
    }

    public static boolean inside(Point2D.Double mousePosition, EditorEntity entity, Editor editor) {
        for (EditorEntity child : entity.children()) {
            if (inside(mousePosition, child, editor)) return true;
        }
        Dimension d = size(entity, editor);
        if (d.width < 8) d.width = 8;
        if (d.height < 8) d.height = 8;
        return inside(mousePosition, d, entity);
    }

    public static void setPosition(double x, double y, EditorEntity entity) {
        if (entity.props != null) {
            entity.props.forEach(c -> {
                if (((ComponentProp) c).name().equals("com.xebisco.yield.Transform2D")) {
                    ((ComponentProp) c).props().forEach(c1 -> {
                        if (c1.name().equals("position")) {
                            ((PositionProp) c1).setValue((float) x, (float) y);
                        }
                    });
                }
            });
        }
        entity.setTransformPosition(x, y);
    }

    public static EditorEntity clearComponents(EditorEntity entity, Editor editor) {
        entity.components().clear();
        EditorComponent transform;
        try {
            transform = addFields(editor.yieldEngineClassLoader.loadClass("com.xebisco.yield.Transform2D"), editor, new EditorComponent("com.xebisco.yield.Transform2D"));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        entity.components().add(transform);
        return entity;
    }

    public static EditorComponent addFields(Class<?> clazz, Editor editor, EditorComponent component) {
        addFields(clazz, editor, component.fields());
        return component;
    }

    public static void extractFields(Class<?> clazz, Object o, Editor editor, List<Pair<Pair<String, String>, String[]>> fields, boolean supportFileInput) {
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            try {
                String[] value;
                if (field.getType().getName().equals("com.xebisco.yield.Vector2D")) {
                    Object obj = field.get(o);
                    value = new String[]{String.valueOf(obj.getClass().getMethod("x").invoke(obj)), String.valueOf(obj.getClass().getMethod("y").invoke(obj))};
                } else if (field.getType().getName().equals("com.xebisco.yield.Color")) {
                    Object obj = field.get(o);
                    value = new String[]{String.valueOf(obj.getClass().getMethod("red").invoke(obj)), String.valueOf(obj.getClass().getMethod("green").invoke(obj)), String.valueOf(obj.getClass().getMethod("blue").invoke(obj)), String.valueOf(obj.getClass().getMethod("alpha").invoke(obj))};
                } else if (editor.yieldEngineClassLoader.loadClass("com.xebisco.yield.FileInput").isAssignableFrom(field.getType())) {
                    if (!supportFileInput) {
                        JOptionPane.showMessageDialog(null, "It doesn't support fileinput", "Error", JOptionPane.ERROR_MESSAGE);
                        continue;
                    }
                    Object obj = null;
                    if (o != null) obj = field.get(o);
                    String path = null;
                    if (obj != null) path = (String) obj.getClass().getMethod("path").invoke(obj);
                    try {
                        String[] extensions = (String[]) clazz.getMethod("extensions").invoke(null);
                        StringBuilder extensionsS = new StringBuilder();
                        for (String ext : extensions) {
                            extensionsS.append(ext).append(";");
                        }
                        if (extensionsS.toString().endsWith(";"))
                            extensionsS = new StringBuilder(extensionsS.substring(0, extensionsS.length() - 1));
                        value = new String[]{path, extensionsS.toString()};
                    } catch (NoSuchMethodException e) {
                        value = new String[]{path};
                    }
                } else value = new String[]{String.valueOf(field.get(o))};
                if (field.isAnnotationPresent(editor.VISIBLE_ANNOTATION) || field.isAnnotationPresent(Visible.class))
                    fields.add(new Pair<>(new Pair<>(field.getName(), field.getType().getName()), value));
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException |
                     ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
