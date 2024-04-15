package com.xebisco.yield.uiutils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

public class Srd {
    public static final Properties LANG = new Properties();
    public static final Map<String, BufferedImage> imageCache = new HashMap<>();
    public static final BufferedImage NULL_IMAGE = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);

    public static BufferedImage getImage(String path) {
        if(imageCache.containsKey(path)) return imageCache.get(path);
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
        if(imageCache.containsKey(res.toExternalForm())) return imageCache.get(res.toExternalForm());
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
