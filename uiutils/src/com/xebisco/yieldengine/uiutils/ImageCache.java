package com.xebisco.yieldengine.uiutils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class ImageCache {
    private final static HashMap<File, BufferedImage> imageCache = new HashMap<>();

    public static BufferedImage get(File file) {
        BufferedImage image = imageCache.get(file);
        if (image == null) {
            try {
                image = ImageIO.read(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            imageCache.put(file, image);
        }
        return image;
    }
}
