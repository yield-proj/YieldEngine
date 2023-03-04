package com.xebisco.yield;

import java.util.Collection;

public interface InputManager {
    Collection<Input.Key> getPressingKeys();
    Collection<Input.MouseButton> getPressingMouseButtons();
    double getMouseX();
    double getMouseY();
}