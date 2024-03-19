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
    public static ClassLoader yieldEngineClassLoader;

    public static final Map<String, BufferedImage> imageCache = new HashMap<>();

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
}
