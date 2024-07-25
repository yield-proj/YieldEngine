package com.xebisco.yieldengine.core.input;

import java.util.Collection;

public interface IMouseDevice {
    float getMouseX();
    float getMouseY();
    void addPressedMouseButtons(Collection<MouseButton> mouseButtons);
}
