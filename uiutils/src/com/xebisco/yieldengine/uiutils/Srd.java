package com.xebisco.yieldengine.uiutils;

import javax.imageio.ImageIO;
import javax.lang.model.SourceVersion;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Srd {
    public static final Properties LANG = new Properties();
    public static final Map<String, BufferedImage> imageCache = new HashMap<>();
    public static final BufferedImage NULL_IMAGE = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);

    public static String prettyString(String s) {
        if (s.isEmpty()) return s;
        StringBuilder builder = new StringBuilder().append(Character.toUpperCase(s.charAt(0)));
        for (int i = 1; i < s.length(); i++) {
            char c = s.charAt(i);
            if (Character.isUpperCase(c)) {
                if (!Character.isUpperCase(s.charAt(i - 1)) && !Character.isWhitespace(s.charAt(i - 1))) {
                    builder.append(" ");
                }
            }
            builder.append(c);
        }
        return builder.toString();
    }

    public static String readFile(File file) throws IOException {
        StringBuilder out = new StringBuilder();

        try(BufferedReader reader = new BufferedReader(new FileReader(file));) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (!out.isEmpty())
                    out.append("\n");
                out.append(line);
            }
        }
        return out.toString();
    }

    public static boolean validateClassName(String className) {
        return SourceVersion.isIdentifier(className) && !SourceVersion.isKeyword(className);
    }

    public static BufferedImage getImage(String path) {
        if (imageCache.containsKey(path)) return imageCache.get(path);
        BufferedImage image;
        try {
            image = ImageIO.read(new File(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        imageCache.put(path, image);
        return image;
    }

    public static BufferedImage getImage(URL res) {
        if (imageCache.containsKey(res.toExternalForm())) return imageCache.get(res.toExternalForm());
        BufferedImage image;
        try {
            image = ImageIO.read(res);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        imageCache.put(res.toExternalForm(), image);
        return image;
    }
}
