package com.xebisco.yieldengine.core.graphics;

import com.xebisco.yieldengine.core.graphics.yldg1.YLDG1;

public final class Graphics {
    private static IPainterReceiver painterReceiver;
    private final GraphicsStandard standard;

    public Graphics(GraphicsStandard standard) {
        this.standard = standard;
    }

    public YLDG1 getG1() {
        return (YLDG1) standard;
    }

    public GraphicsStandard getStandard() {
        return standard;
    }

    public static IPainterReceiver getPainterReceiver() {
        return painterReceiver;
    }

    public static void setPainterReceiver(IPainterReceiver painterReceiver) {
        Graphics.painterReceiver = painterReceiver;
    }
}
