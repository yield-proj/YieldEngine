package com.xebisco.yield.input;

import com.xebisco.yield.utils.YldAction;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;

public class YldInput implements KeyListener {

    private HashMap<Keys, YldAction> shortcuts = new HashMap<>();
    private ArrayList<Integer> pressing = new ArrayList<>();

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

    public boolean justPressed(int...keyCode) {
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
}
