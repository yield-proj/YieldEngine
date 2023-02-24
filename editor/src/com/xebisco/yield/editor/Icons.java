package com.xebisco.yield.editor;

import javax.swing.*;
import java.awt.*;

public class Icons {
    public static ImageIcon
            EDITOR_LOGO,
            EDITOR_LOGO_12dv,
            YIELD_ICON,
            UPLOAD_ICON,
            UPLOAD_ICON_16x16,
            STARRED_ICON,
            STARRED_ICON_16x16,
            NOT_STARRED_ICON,
            NOT_STARRED_ICON_16x16,
        YIELD_ICON_16x16;

    public static void loadAll() {
        //noinspection ConstantConditions
        STARRED_ICON = new ImageIcon(Icons.class.getResource("/starredIcon.png"));
        //noinspection ConstantConditions
        NOT_STARRED_ICON = new ImageIcon(Icons.class.getResource("/notStarredIcon.png"));
        //noinspection ConstantConditions
        YIELD_ICON = new ImageIcon(Icons.class.getResource("/yieldIcon.png"));
        //noinspection ConstantConditions
        UPLOAD_ICON = new ImageIcon(Icons.class.getResource("/uploadIcon.png"));
        //noinspection ConstantConditions
        EDITOR_LOGO = new ImageIcon(Icons.class.getResource("/editorLogo.png"));
        YIELD_ICON_16x16 = new ImageIcon(YIELD_ICON.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH));
        EDITOR_LOGO_12dv = new ImageIcon(EDITOR_LOGO.getImage().getScaledInstance(EDITOR_LOGO.getIconWidth() / 12, EDITOR_LOGO.getIconHeight() / 12, Image.SCALE_SMOOTH));
        UPLOAD_ICON_16x16 = new ImageIcon(UPLOAD_ICON.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH));
        STARRED_ICON_16x16 = new ImageIcon(STARRED_ICON.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH));
        NOT_STARRED_ICON_16x16 = new ImageIcon(NOT_STARRED_ICON.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH));
    }
}
