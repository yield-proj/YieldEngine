package com.xebisco.yield;

import java.util.Collection;

public interface InputManager {
    Collection<Integer> getPressingKeys();
    Collection<Integer> getPressingMouse();
    double getMouseX();
    double getMouseY();
}