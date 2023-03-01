package com.xebisco.yield;

import java.io.InputStream;

public interface FontLoader {
    Object loadFont(Font font);
    void unloadFont(Font font);

    /**
     * Calculates the text width based on the font reference gave.
     * @param text The text.
     * @param fontRef The font reference.
     * @return The text width.
     */
    double getStringWidth(String text, Object fontRef);

    /**
     * Calculates the text height based on the font reference gave.
     * @param text The text.
     * @param fontRef The font reference.
     * @return The text height.
     */
    double getStringHeight(String text, Object fontRef);
}
