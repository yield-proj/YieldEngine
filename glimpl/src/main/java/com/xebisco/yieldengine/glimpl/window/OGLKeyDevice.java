package com.xebisco.yieldengine.glimpl.window;

import com.xebisco.yieldengine.core.input.IKeyDevice;
import com.xebisco.yieldengine.core.input.Key;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;

public class OGLKeyDevice implements IKeyDevice, KeyListener {

    private final Set<Key> keys = new HashSet<>();

    public OGLKeyDevice(OGLPanel window) {
        window.getContentPane().addKeyListener(this);
    }

    @Override
    public void addPressedKeys(Collection<Key> keys) {
        try {
            keys.addAll(this.keys);
        } catch (ConcurrentModificationException ignore) {
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        keys.add(awtToYieldKey(e));
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys.remove(awtToYieldKey(e));
    }

    private static Key awtToYieldKey(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ENTER:
                return Key.VK_ENTER;
            case KeyEvent.VK_BACK_SPACE:
                return Key.VK_BACK_SPACE;
            case KeyEvent.VK_TAB:
                return Key.VK_TAB;
            case KeyEvent.VK_CANCEL:
                return Key.VK_CANCEL;
            case KeyEvent.VK_CLEAR:
                return Key.VK_CLEAR;
            case KeyEvent.VK_SHIFT:
                return Key.VK_SHIFT;
            case KeyEvent.VK_CONTROL:
                return Key.VK_CONTROL;
            case KeyEvent.VK_ALT:
                return Key.VK_ALT;
            case KeyEvent.VK_PAUSE:
                return Key.VK_PAUSE;
            case KeyEvent.VK_CAPS_LOCK:
                return Key.VK_CAPS_LOCK;
            case KeyEvent.VK_ESCAPE:
                return Key.VK_ESCAPE;
            case KeyEvent.VK_SPACE:
                return Key.VK_SPACE;
            case KeyEvent.VK_PAGE_UP:
                return Key.VK_PAGE_UP;
            case KeyEvent.VK_PAGE_DOWN:
                return Key.VK_PAGE_DOWN;
            case KeyEvent.VK_END:
                return Key.VK_END;
            case KeyEvent.VK_HOME:
                return Key.VK_HOME;
            case KeyEvent.VK_LEFT:
                return Key.VK_LEFT;
            case KeyEvent.VK_UP:
                return Key.VK_UP;
            case KeyEvent.VK_RIGHT:
                return Key.VK_RIGHT;
            case KeyEvent.VK_DOWN:
                return Key.VK_DOWN;
            case KeyEvent.VK_COMMA:
                return Key.VK_COMMA;
            case KeyEvent.VK_MINUS:
                return Key.VK_MINUS;
            case KeyEvent.VK_PERIOD:
                return Key.VK_PERIOD;
            case KeyEvent.VK_SLASH:
                return Key.VK_SLASH;
            case KeyEvent.VK_0:
                return Key.VK_0;
            case KeyEvent.VK_1:
                return Key.VK_1;
            case KeyEvent.VK_2:
                return Key.VK_2;
            case KeyEvent.VK_3:
                return Key.VK_3;
            case KeyEvent.VK_4:
                return Key.VK_4;
            case KeyEvent.VK_5:
                return Key.VK_5;
            case KeyEvent.VK_6:
                return Key.VK_6;
            case KeyEvent.VK_7:
                return Key.VK_7;
            case KeyEvent.VK_8:
                return Key.VK_8;
            case KeyEvent.VK_9:
                return Key.VK_9;
            case KeyEvent.VK_SEMICOLON:
                return Key.VK_SEMICOLON;
            case KeyEvent.VK_EQUALS:
                return Key.VK_EQUALS;
            case KeyEvent.VK_A:
                return Key.VK_A;
            case KeyEvent.VK_B:
                return Key.VK_B;
            case KeyEvent.VK_C:
                return Key.VK_C;
            case KeyEvent.VK_D:
                return Key.VK_D;
            case KeyEvent.VK_E:
                return Key.VK_E;
            case KeyEvent.VK_F:
                return Key.VK_F;
            case KeyEvent.VK_G:
                return Key.VK_G;
            case KeyEvent.VK_H:
                return Key.VK_H;
            case KeyEvent.VK_I:
                return Key.VK_I;
            case KeyEvent.VK_J:
                return Key.VK_J;
            case KeyEvent.VK_K:
                return Key.VK_K;
            case KeyEvent.VK_L:
                return Key.VK_L;
            case KeyEvent.VK_M:
                return Key.VK_M;
            case KeyEvent.VK_N:
                return Key.VK_N;
            case KeyEvent.VK_O:
                return Key.VK_O;
            case KeyEvent.VK_P:
                return Key.VK_P;
            case KeyEvent.VK_Q:
                return Key.VK_Q;
            case KeyEvent.VK_R:
                return Key.VK_R;
            case KeyEvent.VK_S:
                return Key.VK_S;
            case KeyEvent.VK_T:
                return Key.VK_T;
            case KeyEvent.VK_U:
                return Key.VK_U;
            case KeyEvent.VK_V:
                return Key.VK_V;
            case KeyEvent.VK_W:
                return Key.VK_W;
            case KeyEvent.VK_X:
                return Key.VK_X;
            case KeyEvent.VK_Y:
                return Key.VK_Y;
            case KeyEvent.VK_Z:
                return Key.VK_Z;
            case KeyEvent.VK_OPEN_BRACKET:
                return Key.VK_OPEN_BRACKET;
            case KeyEvent.VK_BACK_SLASH:
                return Key.VK_BACK_SLASH;
            case KeyEvent.VK_CLOSE_BRACKET:
                return Key.VK_CLOSE_BRACKET;
            case KeyEvent.VK_DELETE:
                return Key.VK_DELETE;
            case KeyEvent.VK_NUM_LOCK:
                return Key.VK_NUM_LOCK;
            case KeyEvent.VK_SCROLL_LOCK:
                return Key.VK_SCROLL_LOCK;
            case KeyEvent.VK_F1:
                return Key.VK_F1;
            case KeyEvent.VK_F2:
                return Key.VK_F2;
            case KeyEvent.VK_F3:
                return Key.VK_F3;
            case KeyEvent.VK_F4:
                return Key.VK_F4;
            case KeyEvent.VK_F5:
                return Key.VK_F5;
            case KeyEvent.VK_F6:
                return Key.VK_F6;
            case KeyEvent.VK_F7:
                return Key.VK_F7;
            case KeyEvent.VK_F8:
                return Key.VK_F8;
            case KeyEvent.VK_F9:
                return Key.VK_F9;
            case KeyEvent.VK_F10:
                return Key.VK_F10;
            case KeyEvent.VK_F11:
                return Key.VK_F11;
            case KeyEvent.VK_F12:
                return Key.VK_F12;
            case KeyEvent.VK_PRINTSCREEN:
                return Key.VK_PRINTSCREEN;
            case KeyEvent.VK_INSERT:
                return Key.VK_INSERT;
            case KeyEvent.VK_BACK_QUOTE:
                return Key.VK_BACK_QUOTE;
            case KeyEvent.VK_QUOTE:
                return Key.VK_QUOTE;
            case KeyEvent.VK_AMPERSAND:
                return Key.VK_AMPERSAND;
            case KeyEvent.VK_ASTERISK:
                return Key.VK_ASTERISK;
            case KeyEvent.VK_QUOTEDBL:
                return Key.VK_QUOTEDBL;
            case KeyEvent.VK_LESS:
                return Key.VK_LESS;
            case KeyEvent.VK_GREATER:
                return Key.VK_GREATER;
            case KeyEvent.VK_BRACELEFT:
                return Key.VK_BRACELEFT;
            case KeyEvent.VK_BRACERIGHT:
                return Key.VK_BRACERIGHT;
            case KeyEvent.VK_AT:
                return Key.VK_AT;
            case KeyEvent.VK_COLON:
                return Key.VK_COLON;
            case KeyEvent.VK_CIRCUMFLEX:
                return Key.VK_CIRCUMFLEX;
            case KeyEvent.VK_DOLLAR:
                return Key.VK_DOLLAR;
            case KeyEvent.VK_EXCLAMATION_MARK:
                return Key.VK_EXCLAMATION_MARK;
            case KeyEvent.VK_LEFT_PARENTHESIS:
                return Key.VK_LEFT_PARENTHESIS;
            case KeyEvent.VK_NUMBER_SIGN:
                return Key.VK_NUMBER_SIGN;
            case KeyEvent.VK_PLUS:
                return Key.VK_PLUS;
            case KeyEvent.VK_RIGHT_PARENTHESIS:
                return Key.VK_RIGHT_PARENTHESIS;
            case KeyEvent.VK_UNDERSCORE:
                return Key.VK_UNDERSCORE;
            case KeyEvent.VK_CONTEXT_MENU:
                return Key.VK_SUPER;
        }
        return Key.UNDEFINED;
    }
}
