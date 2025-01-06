package com.xebisco.yieldengine.core.components;

import com.xebisco.yieldengine.core.Component;
import com.xebisco.yieldengine.core.graphics.Graphics;
import com.xebisco.yieldengine.core.graphics.IPainter;
import com.xebisco.yieldengine.core.graphics.yldg1.Paint;
import com.xebisco.yieldengine.core.io.IO;
import com.xebisco.yieldengine.core.io.text.Font;
import com.xebisco.yieldengine.utils.Color4f;
import com.xebisco.yieldengine.utils.ColorUtils;
import com.xebisco.yieldengine.utils.Editable;
import com.xebisco.yieldengine.utils.Visible;
import org.joml.Vector2f;

public class Text extends Component implements IPainter {
    @Visible
    @Editable
    private String contents = "Sample Text";
    @Visible
    @Editable
    private Font font = IO.getInstance().getDefaultFont();
    @Visible
    @Editable
    private Color4f color = ColorUtils.argb(0xFFFFFFFF);

    protected final Paint paint = new Paint();

    public Text() {
    }

    public Text(String contents) {
        this.contents = contents;
    }

    public Text(String contents, Color4f color) {
        this.contents = contents;
        this.color = color;
    }

    public Text(String contents, Font font, Color4f color) {
        this.contents = contents;
        this.font = font;
        this.color = color;
    }

    @Override
    public void onPaint(Graphics g) {
        g.getG1().drawText(contents, paint.setTransform(getWorldTransform()).setColor(color).setFont(font));
    }

    public Vector2f getSize(Graphics g) {
        paint.setTransform(getWorldTransform()).setColor(color).setFont(font);
        return new Vector2f(g.getG1().stringWidth(contents, paint), g.getG1().stringHeight(contents, paint));
    }

    public String getContents() {
        return contents;
    }

    public Text setContents(String contents) {
        this.contents = contents;
        return this;
    }

    public Font getFont() {
        return font;
    }

    public Text setFont(Font font) {
        this.font = font;
        return this;
    }

    public Color4f getColor() {
        return color;
    }

    public Text setColor(Color4f color) {
        this.color = color;
        return this;
    }

    private Paint getPaint() {
        return paint;
    }
}
