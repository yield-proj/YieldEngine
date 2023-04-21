package com.xebisco.yield.androidimpl;

import android.content.Context;
import android.graphics.Color;
import android.graphics.*;
import android.view.View;
import com.xebisco.yield.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class MainView extends View implements PlatformGraphics, TextureManager, FontLoader {
    private final Paint viewPaint = new Paint();
    private Bitmap gameBuffer, uiBuffer, buffer;
    private final Rect drawRect = new Rect();
    private final Paint drawPaint = new Paint();

    public MainView(Context context) {
        super(context);
    }

    int x;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        x++;


        viewPaint.setColor(Color.WHITE);
        canvas.drawRect(10, x, 100, 100, viewPaint);
    }

    @Override
    public void init(PlatformInit platformInit) {
        gameBuffer = Bitmap.createBitmap(
                (int) platformInit.getGameResolution().getWidth(),
                (int) platformInit.getGameResolution().getHeight(),
                Bitmap.Config.ARGB_8888,
                false
        );
        uiBuffer = Bitmap.createBitmap(
                (int) platformInit.getUiResolution().getWidth(),
                (int) platformInit.getUiResolution().getHeight(),
                Bitmap.Config.ARGB_8888,
                true
        );
        buffer = Bitmap.createBitmap(
                (int) platformInit.getResolution().getWidth(),
                (int) platformInit.getResolution().getHeight(),
                Bitmap.Config.ARGB_8888,
                false
        );
    }

    @Override
    public void frame() {
        System.out.println("aaa");
    }

    @Override
    public void draw(DrawInstruction drawInstruction) {

    }

    @Override
    public void resetRotation() {

    }

    @Override
    public boolean shouldClose() {
        return false;
    }

    @Override
    public void conclude() {

    }

    @Override
    public Vector2D getCamera() {
        return null;
    }

    @Override
    public void setCamera(Vector2D vector2D) {

    }

    @Override
    public void dispose() {

    }

    @Override
    public Object loadTexture(Texture texture) {
        return BitmapFactory.decodeStream(texture.getInputStream()).copy(Bitmap.Config.ARGB_8888, true);
    }

    @Override
    public void unloadTexture(Texture texture) {
        texture.setImageRef(null);
    }

    @Override
    public void setPixel(Object o, com.xebisco.yield.Color color, int x, int y) {
        ((Bitmap) o).setPixel(x, y, color.getARGB());
    }

    @Override
    public int[] getPixels(Object o) {
        int[] pixels = new int[((Bitmap) o).getWidth() * ((Bitmap) o).getHeight() * 4];
        ((Bitmap) o).getPixels(pixels, 0, ((Bitmap) o).getWidth(), 0, 0, ((Bitmap) o).getWidth(), ((Bitmap) o).getHeight());
        return pixels;
    }

    @Override
    public int[] getPixel(Object o, int x, int y) {
        com.xebisco.yield.Color c = new com.xebisco.yield.Color(((Bitmap) o).getPixel(x, y), com.xebisco.yield.Color.Format.ARGB);
        return new int[]{(int) (c.getAlpha() * 255.0), (int) (c.getRed() * 255.0), (int) (c.getGreen() * 255.0), (int) (c.getBlue() * 255.0)};
    }

    @Override
    public int getImageWidth(Object o) {
        return ((Bitmap) o).getWidth();
    }

    @Override
    public int getImageHeight(Object o) {
        return ((Bitmap) o).getHeight();
    }

    @Override
    public Texture cropTexture(Object o, int x, int y, int w, int h) {
        Bitmap generated = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(generated);
        drawRect.set(x, y, ((Bitmap) o).getWidth(), ((Bitmap) o).getHeight());
        canvas.drawBitmap((Bitmap) o, null, drawRect, null);
        return new Texture(generated, null, this);
    }

    @Override
    public Texture scaledTexture(Object o, int w, int h) {
        Bitmap generated = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(generated);
        drawRect.set(0, 0, w, h);
        canvas.drawBitmap((Bitmap) o, null, drawRect, null);
        return new Texture(generated, null, this);
    }

    @Override
    public Texture printScreenTexture() {
        Bitmap generated = Bitmap.createBitmap(gameBuffer.getWidth(), gameBuffer.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(generated);
        drawRect.set(0, 0, gameBuffer.getWidth(), gameBuffer.getHeight());
        canvas.drawBitmap(gameBuffer, null, drawRect, null);
        return new Texture(generated, null, this);
    }

    @Override
    public Object loadFont(Font font) {
        File temp;
        try {
            temp = File.createTempFile("yfont", null);
            Files.copy(font.getInputStream(), temp.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Typeface.createFromFile(temp);
    }

    @Override
    public void unloadFont(Font font) {
        font.setFontRef(null);
    }

    @Override
    public double getStringWidth(String s, Object o) {
        Rect bounds = new Rect();
        drawPaint.getTextBounds(s, 0, s.length(), bounds);
        return bounds.width();
    }

    @Override
    public double getStringHeight(String s, Object o) {
        Rect bounds = new Rect();
        drawPaint.getTextBounds(s, 0, s.length(), bounds);
        return bounds.height();
    }
}
