package com.xebisco.yieldengine.utils;

import java.util.HashMap;

public final class ColorPalette {
    private final String name;
    private final HashMap<String, Color4f> colors = new HashMap<>();

    public enum Colors {
        BLACK("black"),
        BLUE("blue"),
        BROWN("brown"),
        CYAN("cyan"),
        DARK_BLUE("dark_blue"),
        DARK_ORANGE("dark_orange"),
        DARK_RED("dark_red"),
        DARK_VIOLET("dark_violet"),
        GOLD("gold"),
        GRAY("gray"),
        GREEN("green"),
        LIGHT_BLUE("light_blue"),
        LIGHT_PINK("light_pink"),
        LIME("lime"),
        MAGENTA("magenta"),
        ORANGE("orange"),
        PINK("pink"),
        PURPLE("purple"),
        RED("red"),
        SILVER("silver"),
        VIOLET("violet"),
        WHITE("white"),
        YELLOW("yellow"),
        ;
        private final String colorName;

        Colors(String colorName) {
            this.colorName = colorName;
        }

        public String getColorName() {
            return colorName;
        }

        public Color4f get(ColorPalette palette) {
            return palette.getColor(this);
        }

        public Color4f get() {
            return get(ColorUtils.STANDARD_PALETTE);
        }
    }

    public ColorPalette(ColorPalette other, String name) {
        if (other != null) {
            this.colors.putAll(other.colors);
        }
        this.name = name;
    }

    public void putFromStandard(Colors color) {
        colors.put(color.getColorName(), ColorUtils.STANDARD_PALETTE.getColor(color));
    }

    public ColorPalette(String name) {
        this(ColorUtils.STANDARD_PALETTE, name);
    }

    public Color4f getColor(Colors color) {
        return colors.get(color.getColorName());
    }

    public Color4f getRandomColor() {
        return (Color4f) colors.values().toArray()[RandomUtils.RANDOM.nextInt(colors.size())];
    }

    public String getName() {
        return name;
    }

    public HashMap<String, Color4f> getColors() {
        return colors;
    }
}
