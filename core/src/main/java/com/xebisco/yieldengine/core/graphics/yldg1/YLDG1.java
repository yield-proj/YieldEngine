package com.xebisco.yieldengine.core.graphics.yldg1;

import com.xebisco.yieldengine.core.IDispose;
import com.xebisco.yieldengine.core.graphics.GraphicsStandard;

public interface YLDG1 extends GraphicsStandard, IDispose {
    void initContext();
    void drawRect(float width, float height, Paint paint);
    void drawText(String str, Paint paint);
    void drawImage(float width, float height, Paint paint);
    void drawEllipse(float width, float height, Paint paint);
    void drawLine(float x1, float y1, float x2, float y2, Paint paint);
    float stringWidth(String str, Paint paint);
    float stringHeight(String str, Paint paint);
    void clearScreen(Paint paint);

    @Override
    default String name() {
        return "YLDG1";
    }
}
