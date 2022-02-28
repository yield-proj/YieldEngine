package com.xebisco.yield.input;

import com.xebisco.yield.View;
import com.xebisco.yield.engine.YldWindow;
import com.xebisco.yield.utils.YldAction;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;

public class YldInput implements KeyListener, MouseListener {

    private HashMap<Keys, YldAction> shortcuts = new HashMap<>();
    private ArrayList<Integer> pressing = new ArrayList<>();
    private boolean touching, clicking;
    private final YldWindow window;

    public YldInput(YldWindow window) {
        this.window = window;
        window.getFrame().addKeyListener(this);
        window.getFrame().addMouseListener(this);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        pressing.add(e.getKeyCode());
        Keys keys = new Keys(pressing.toArray(new Integer[0]));
        if (shortcuts.containsKey(keys)) {
            shortcuts.get(keys).onAction();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressing.removeIf(s -> (s == e.getKeyCode()));
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        clicking = true;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        touching = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        touching = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public boolean isPressing(int...keyCode) {
        int contains = 0;
        int i = 0;
        while (i < keyCode.length) {
            if (pressing.contains(keyCode[i])) {
                contains++;
                break;
            }
            i++;
        }
        return contains == keyCode.length;
    }

    public boolean justPressed(int... keyCode) {
        int contains = 0;
        int i = 0;
        while (i < keyCode.length) {
            if (pressing.contains(keyCode[i])) {
                pressing.remove(new Integer(keyCode[i]));
                contains++;
                break;
            }
            i++;
        }
        return contains == keyCode.length;
    }

    public void addShortcut(Keys keys, YldAction action) {
        shortcuts.put(keys, action);
    }

    public HashMap<Keys, YldAction> getShortcuts() {
        return shortcuts;
    }

    public void setShortcuts(HashMap<Keys, YldAction> shortcuts) {
        this.shortcuts = shortcuts;
    }

    public ArrayList<Integer> getPressing() {
        return pressing;
    }

    public void setPressing(ArrayList<Integer> pressing) {
        this.pressing = pressing;
    }

    public int getX() {
        int p = MouseInfo.getPointerInfo().getLocation().x;
        p -= window.getFrame().getX() + window.getFrame().getInsets().left;
        if (View.getActView() == null) {
            if (p > window.getWindowG().getWidth())
                p = window.getWindowG().getWidth();
        } else {
            p = (int) ((p / (float) window.getWindowG().getWidth()) * View.getActView().getWidth());
            if (p > View.getActView().getWidth())
                p = View.getActView().getWidth();
        }
        if (p < 0)
            p = 0;
        return p;
    }

    public int getY() {
        int p = MouseInfo.getPointerInfo().getLocation().y;
        p -= window.getFrame().getY() + window.getFrame().getInsets().top;
        if (View.getActView() == null) {
            if (p > window.getWindowG().getHeight())
                p = window.getWindowG().getHeight();
        } else {
            p = (int) ((p / (float) window.getWindowG().getHeight()) * View.getActView().getHeight());
            if (p > View.getActView().getHeight())
                p = View.getActView().getHeight();
        }
        if (p < 0)
            p = 0;
        return p;
    }

    public boolean isTouching() {
        return touching;
    }

    public void setTouching(boolean touching) {
        this.touching = touching;
    }

    public boolean isClicking() {
        return clicking;
    }

    public void setClicking(boolean clicking) {
        this.clicking = clicking;
    }
}
