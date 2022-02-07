package com.xebisco.yield;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Resolution {
    private final BufferedImage image;
    private static Resolution actResolution;
    private static int standardImageType = BufferedImage.TYPE_INT_RGB;
    private final int width, height;
    private Color bgColor = new Color(0xFF1E2D74);

    public Resolution(int width, int height) {
        actC();
        this.width = width;
        this.height = height;
        image = new BufferedImage(width, height, standardImageType);
    }

    public Resolution(int width, int height, Color bgColor) {
        actC();
        this.width = width;
        this.height = height;
        this.bgColor = bgColor;
        image = new BufferedImage(width, height, standardImageType);
    }

    private void actC() {
        if(actResolution == null) {
            actResolution = this;
        }
    }

    public BufferedImage getImage() {
        return image;
    }

    public Color getBgColor() {
        return bgColor;
    }

    public void setBgColor(Color bgColor) {
        this.bgColor = bgColor;
    }

    public static Resolution getActResolution() {
        return actResolution;
    }

    public static int getStandardImageType() {
        return standardImageType;
    }

    public static void setActResolution(Resolution actCamera) {
        Resolution.actResolution = actCamera;
    }

    public static void setStandardImageType(int standardImageType) {
        Resolution.standardImageType = standardImageType;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
