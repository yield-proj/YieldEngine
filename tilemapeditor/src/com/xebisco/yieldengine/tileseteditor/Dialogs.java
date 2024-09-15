package com.xebisco.yieldengine.tileseteditor;

import com.xebisco.yieldengine.uiutils.Point;
import com.xebisco.yieldengine.uiutils.Utils;
import com.xebisco.yieldengine.uiutils.fields.*;

import java.awt.*;
import java.io.File;

import static com.xebisco.yieldengine.uiutils.Lang.getString;

public class Dialogs {

    public static final String
            IMAGE_FILE = getString("image_file"),
            NAME = getString("name"),
            ENTITY_CREATOR_CLASS_NAME = getString("entity_creator_class_name"),
            FIT_TEXTURE = getString("fit_texture"),
            COLOR = getString("color");

    public static FieldPanel<?>[] newImageTileFields() {
        return new FieldPanel<?>[]{
                new FilePanel(IMAGE_FILE, new File("."), Utils.IMAGE_FILE_EXTENSIONS, true),
                new StringFieldPanel(NAME, "", true),
                new StringFieldPanel(ENTITY_CREATOR_CLASS_NAME, "", true),
                new BooleanFieldPanel(FIT_TEXTURE, true, true),
                new ColorFieldPanel(COLOR, Color.WHITE, true)
        };
    }

    public static FieldPanel<?>[] newSubImageTileFields() {
        return new FieldPanel<?>[]{
                new PointFieldPanel<>("test_point", int.class, new Point<>(1, 2), false, false, true),
                new PointFieldPanel<>("test_size", int.class, new Point<>(3, 4), false, true, true),
                new StringFieldPanel(NAME, "", true),
                new StringFieldPanel(ENTITY_CREATOR_CLASS_NAME, "", true),
                new BooleanFieldPanel(FIT_TEXTURE, true, true),
                new ColorFieldPanel(COLOR, Color.WHITE, true)
        };
    }
}
