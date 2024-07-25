package com.xebisco.yieldengine.core.input;

import org.joml.Vector2f;
import org.joml.Vector2fc;

import java.util.HashSet;
import java.util.Set;

public final class Input {
    private static Input instance;

    private final IKeyDevice keyDevice;
    private final IMouseDevice mouseDevice;

    private final Set<Key> pressedKeys = new HashSet<>();
    private final Set<MouseButton> pressedMouseButtons = new HashSet<>();

    public Input(IKeyDevice keyDevice, IMouseDevice mouseDevice) {
        this.keyDevice = keyDevice;
        this.mouseDevice = mouseDevice;
    }

    public void updateKeyList() {
        pressedKeys.clear();
        keyDevice.addPressedKeys(pressedKeys);
    }

    public void updateMouseButtonList() {
        pressedMouseButtons.clear();
        mouseDevice.addPressedMouseButtons(pressedMouseButtons);
    }

    public boolean isKeyPressed(Key key) {
        return pressedKeys.contains(key);
    }

    public boolean isMouseButtonPressed(MouseButton button) {
        return pressedMouseButtons.contains(button);
    }

    public Vector2fc getMousePosition() {
        return new Vector2f(mouseDevice.getMouseX(), mouseDevice.getMouseY());
    }

    public static Input getInstance() {
        return instance;
    }

    public static void setInstance(Input instance) {
        Input.instance = instance;
    }

    public IKeyDevice getKeyDevice() {
        return keyDevice;
    }

    public Set<Key> getPressedKeys() {
        return pressedKeys;
    }

    public IMouseDevice getMouseDevice() {
        return mouseDevice;
    }

    public Set<MouseButton> getPressedMouseButtons() {
        return pressedMouseButtons;
    }
}
