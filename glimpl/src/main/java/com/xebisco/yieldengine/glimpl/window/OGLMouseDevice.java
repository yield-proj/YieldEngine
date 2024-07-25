package com.xebisco.yieldengine.glimpl.window;

import com.xebisco.yieldengine.core.input.IMouseDevice;
import com.xebisco.yieldengine.core.input.MouseButton;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class OGLMouseDevice implements IMouseDevice, MouseListener {
    private final OGLWindow window;

    private final Set<MouseButton> buttons = new HashSet<>();

    public OGLMouseDevice(OGLWindow window) {
        this.window = window;
        window.getCanvas().addMouseListener(this);
    }

    @Override
    public float getMouseX() {
        try {
            return (float) window.getFrame().getContentPane().getMousePosition().x;
        } catch (NullPointerException e) {
            return 0;
        }
    }

    @Override
    public float getMouseY() {
        try {
            return (float) window.getFrame().getContentPane().getMousePosition().y;
        } catch (NullPointerException e) {
            return 0;
        }
    }

    @Override
    public void addPressedMouseButtons(Collection<MouseButton> mouseButtons) {
        mouseButtons.addAll(buttons);
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
}
