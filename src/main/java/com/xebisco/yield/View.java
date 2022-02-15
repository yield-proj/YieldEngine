package com.xebisco.yield;

import java.awt.image.BufferedImage;

public class View {
    private final BufferedImage image;
    private static View actView;
    private static int standardImageType = BufferedImage.TYPE_INT_RGB;
    private final int width, height;
    private Color bgColor = new Color(.1176470588235294f, .1764705882352941f, .4549019607843137f);

    public View(int width, int height) {
        actC();
        this.width = width;
        this.height = height;
        image = new BufferedImage(width, height, standardImageType);
    }

    public View(int width, int height, Color bgColor) {
        actC();
        this.width = width;
        this.height = height;
        this.bgColor = bgColor;
        image = new BufferedImage(width, height, standardImageType);
    }

    private void actC() {
        if(actView == null) {
            actView = this;
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

    public static View getActView() {
        return actView;
    }

    public static int getStandardImageType() {
        return standardImageType;
    }

    public static void setActView(View actCamera) {
        View.actView = actCamera;
    }

    public static void setStandardImageType(int standardImageType) {
        View.standardImageType = standardImageType;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
