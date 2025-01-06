package com.xebisco.yieldengine.core.graphics;

import java.util.Queue;

public interface IPainterReceiver {
    void paint(Queue<IPainter> painters);
}
