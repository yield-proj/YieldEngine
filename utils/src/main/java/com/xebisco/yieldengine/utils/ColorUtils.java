package com.xebisco.yieldengine.utils;

public class ColorUtils {
    public static final ColorPalette STANDARD_PALETTE = new ColorPalette(null, "standard");

    static {
        STANDARD_PALETTE.getColors().put(ColorPalette.Colors.BLACK.getColorName(), rgb(0x000000));
        STANDARD_PALETTE.getColors().put(ColorPalette.Colors.BLUE.getColorName(), rgb(0x0000FF));
        STANDARD_PALETTE.getColors().put(ColorPalette.Colors.BROWN.getColorName(), rgb(0xA52A2A));
        STANDARD_PALETTE.getColors().put(ColorPalette.Colors.CYAN.getColorName(),  rgb(0x00FFFF));
        STANDARD_PALETTE.getColors().put(ColorPalette.Colors.DARK_BLUE.getColorName(), rgb(0x00008B));
        STANDARD_PALETTE.getColors().put(ColorPalette.Colors.DARK_ORANGE.getColorName(), rgb(0xFF8C00));
        STANDARD_PALETTE.getColors().put(ColorPalette.Colors.DARK_RED.getColorName(), rgb(0x8B0000));
        STANDARD_PALETTE.getColors().put(ColorPalette.Colors.DARK_VIOLET.getColorName(), rgb(0x9400D3));
        STANDARD_PALETTE.getColors().put(ColorPalette.Colors.GOLD.getColorName(), rgb(0xFFD700));
        STANDARD_PALETTE.getColors().put(ColorPalette.Colors.GRAY.getColorName(), rgb(0x808080));
        STANDARD_PALETTE.getColors().put(ColorPalette.Colors.GREEN.getColorName(), rgb(0x008000));
        STANDARD_PALETTE.getColors().put(ColorPalette.Colors.GOLD.getColorName(), rgb(0xFFD700));
        STANDARD_PALETTE.getColors().put(ColorPalette.Colors.LIGHT_BLUE.getColorName(), rgb(0xADD8E6));
        STANDARD_PALETTE.getColors().put(ColorPalette.Colors.LIGHT_PINK.getColorName(), rgb(0xFFB6C1));
        STANDARD_PALETTE.getColors().put(ColorPalette.Colors.LIME.getColorName(), rgb(0x00FF00));
        STANDARD_PALETTE.getColors().put(ColorPalette.Colors.MAGENTA.getColorName(), rgb(0xFF00FF));
        STANDARD_PALETTE.getColors().put(ColorPalette.Colors.ORANGE.getColorName(), rgb(0xFFA500));
        STANDARD_PALETTE.getColors().put(ColorPalette.Colors.PINK.getColorName(), rgb(0xFFC0CB));
        STANDARD_PALETTE.getColors().put(ColorPalette.Colors.PURPLE.getColorName(), rgb(0x800080));
        STANDARD_PALETTE.getColors().put(ColorPalette.Colors.RED.getColorName(), rgb(0xFF0000));
        STANDARD_PALETTE.getColors().put(ColorPalette.Colors.SILVER.getColorName(), rgb(0xC0C0C0));
        STANDARD_PALETTE.getColors().put(ColorPalette.Colors.VIOLET.getColorName(), rgb(0xEE82EE));
        STANDARD_PALETTE.getColors().put(ColorPalette.Colors.WHITE.getColorName(), rgb(0xFFFFFF));
        STANDARD_PALETTE.getColors().put(ColorPalette.Colors.YELLOW.getColorName(), rgb(0xFFFF00));
    }

    public static Color4f rgb(int color) {
        return new Color4f(((color >> 16) & 0xFF) / 255f, ((color >> 8) & 0xFF) / 255f, ((color) & 0xFF) / 255f);
    }

    public static Color4f argb(int color) {
        return new Color4f(((color >> 16) & 0xFF) / 255f, ((color >> 8) & 0xFF) / 255f, ((color) & 0xFF) / 255f, ((color >> 24) & 0xFF) / 255f);
    }

    public static Color4f rgba(int color) {
        return new Color4f(((color >> 24) & 0xFF) / 255f, ((color >> 16) & 0xFF) / 255f, ((color >> 8) & 0xFF) / 255f, ((color) & 0xFF) / 255f);
    }

    public static int argb(Color4f color) {
        return to8BitInt(color.getAlpha(), color.getRed(), color.getGreen(), color.getBlue());
    }

    public static int rgba(Color4f color) {
        return to8BitInt(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    public static Color4f randomColor() {
        return new Color4f(RandomUtils.RANDOM.nextFloat(), RandomUtils.RANDOM.nextFloat(), RandomUtils.RANDOM.nextFloat(), 1f);
    }

    private static int to8BitInt(double f, double s, double t, double fh) {
        return ((int) (f * 255)) << 24 + ((int) (s * 255)) << 16 + ((int) (t * 255)) << 8 + ((int) (fh * 255));
    }
}