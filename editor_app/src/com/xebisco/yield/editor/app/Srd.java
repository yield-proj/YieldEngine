package com.xebisco.yield.editor.app;

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
    public static final String VERSION = "INFDEV", TITLE = "Yield Editor " + VERSION;
    public static File yieldEngineJar = new File(System.getProperty("user.dir") + "/out/artifacts/core_jar/core.jar");
    public static ClassLoader yieldEngineClassLoader;

    public static final Map<String, BufferedImage> imageCache = new HashMap<>();

    public static final BufferedImage NULL_IMAGE;

    static {
        try {
            yieldEngineClassLoader = new URLClassLoader(new URL[]{yieldEngineJar.toURI().toURL()});
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        try {
            NULL_IMAGE = ImageIO.read(Objects.requireNonNull(Srd.class.getResourceAsStream("/com/xebisco/yield/editor/app/noTextureTexture.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

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
