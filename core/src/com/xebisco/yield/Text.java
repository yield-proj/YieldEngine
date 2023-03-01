package com.xebisco.yield;

public class Text extends ComponentBehavior {
    private final DrawInstruction drawInstruction = new DrawInstruction();
    private Color color = Colors.LIGHT_BLUE.brighter();
    private String contents = "Sample Text";
    private Font font;

    private double width, height;

    public Text() {
    }

    public Text(String contents) {
        this.contents = contents;
    }

    @Override
    public void onStart() {
        drawInstruction.setSize(new Size2D(0, 0));
        if (Global.getDefaultFont() == null)
            Global.setDefaultFont(new Font("OpenSans-Regular.ttf", 48, getEntity().getFontLoader()));
        if (font == null)
            font = Global.getDefaultFont();
        drawInstruction.setType(DrawInstruction.Type.TEXT);
    }

    @Override
    public void onUpdate() {
        drawInstruction.setRenderRef(contents);
        drawInstruction.setFont(font);
        drawInstruction.setRotation(getEntity().getTransform().getzRotation());
        drawInstruction.setInnerColor(color);
        drawInstruction.setPosition(getEntity().getTransform().getPosition());
    }

    @Override
    public void dispose() {

    }

    @Override
    public void render(PlatformGraphics graphics) {
        if (font != null) {
            width = graphics.getStringWidth(contents, font.getFontRef());
            height = graphics.getStringHeight(contents, font.getFontRef());
            drawInstruction.getSize().set(width, height);
            graphics.draw(drawInstruction);
        }
    }

    public DrawInstruction getDrawInstruction() {
        return drawInstruction;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }
}
