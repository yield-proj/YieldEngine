package com.xebisco.yieldengine.glimpl.window;

import com.xebisco.yieldengine.core.input.IMouseDevice;
import com.xebisco.yieldengine.core.input.MouseButton;

import java.awt.event.*;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Set;

public class OGLMouseDevice implements IMouseDevice, MouseListener, MouseWheelListener {
    private final OGLPanel window;

    private final Set<MouseButton> buttons = new HashSet<>();

    public OGLMouseDevice(OGLPanel window) {
        this.window = window;
        window.getCanvas().addMouseListener(this);
        window.getCanvas().addMouseWheelListener(this);
    }

    private float lastMouseX, lastMouseY;

    @Override
    public float getMouseX() {
        try {
            return lastMouseX = ((float) window.getContentPane().getMousePosition().x) / window.getCanvas().getWidth();
        } catch (NullPointerException e) {
            return lastMouseX;
        }
    }

    @Override
    public float getMouseY() {
        try {
            return lastMouseY = ((float) (window.getCanvas().getHeight() - window.getContentPane().getMousePosition().y)) / window.getCanvas().getHeight();
        } catch (NullPointerException e) {
            return lastMouseY;
        }
    }

    @Override
    public void addPressedMouseButtons(Collection<MouseButton> mouseButtons) {
        try {
            mouseButtons.addAll(buttons);
        } catch (ConcurrentModificationException ignore) {
        }
        buttons.remove(MouseButton.SCROLL_WELL_UP);
        buttons.remove(MouseButton.SCROLL_WELL_DOWN);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        buttons.add(awtToYieldButton(e));
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        buttons.remove(awtToYieldButton(e));
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    private static MouseButton awtToYieldButton(MouseEvent e) {
        return MouseButton.values()[e.getButton() - 1];
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if(e.getWheelRotation() < 0) {
            buttons.add(MouseButton.SCROLL_WELL_UP);
        } else {
            buttons.add(MouseButton.SCROLL_WELL_DOWN);
        }
    }
}
