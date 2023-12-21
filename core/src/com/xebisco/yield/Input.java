/*
 * Copyright [2022-2023] [Xebisco]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xebisco.yield;

/**
 * The class "Input" contains two enums, "Key" and "MouseButton", which list various keyboard and mouse input options.
 */
public class Input {
    public enum Key {
        UNDEFINED, VK_ENTER, VK_BACK_SPACE, VK_TAB, VK_CANCEL, VK_CLEAR, VK_SHIFT, VK_CONTROL, VK_ALT, VK_PAUSE, VK_CAPS_LOCK, VK_ESCAPE, VK_SPACE, VK_PAGE_UP, VK_PAGE_DOWN, VK_END, VK_HOME, VK_LEFT, VK_UP, VK_RIGHT, VK_DOWN, VK_COMMA, VK_MINUS, VK_PERIOD, VK_SLASH, VK_0, VK_1, VK_2, VK_3, VK_4, VK_5, VK_6, VK_7, VK_8, VK_9, VK_SEMICOLON, VK_EQUALS, VK_A, VK_B, VK_C, VK_D, VK_E, VK_F, VK_G, VK_H, VK_I, VK_J, VK_K, VK_L, VK_M, VK_N, VK_O, VK_P, VK_Q, VK_R, VK_S, VK_T, VK_U, VK_V, VK_W, VK_X, VK_Y, VK_Z, VK_OPEN_BRACKET, VK_BACK_SLASH, VK_CLOSE_BRACKET, VK_NUMPAD0, VK_NUMPAD1, VK_NUMPAD2, VK_NUMPAD3, VK_NUMPAD4, VK_NUMPAD5, VK_NUMPAD6, VK_NUMPAD7, VK_NUMPAD8, VK_NUMPAD9, VK_MULTIPLY, VK_ADD, VK_SEPARATOR, VK_SUBTRACT, VK_DECIMAL, VK_DIVIDE, VK_DELETE, /* ASCII DEL */        VK_NUM_LOCK, VK_SCROLL_LOCK, VK_F1, VK_F2, VK_F3, VK_F4, VK_F5, VK_F6, VK_F7, VK_F8, VK_F9, VK_F10, VK_F11, VK_F12, VK_PRINTSCREEN, VK_INSERT, VK_HELP, VK_META, VK_BACK_QUOTE, VK_QUOTE, VK_KP_UP, VK_KP_DOWN, VK_KP_LEFT, VK_KP_RIGHT, VK_AMPERSAND, VK_ASTERISK, VK_QUOTEDBL, VK_LESS, VK_GREATER, VK_BRACELEFT, VK_BRACERIGHT, VK_AT, VK_COLON, VK_CIRCUMFLEX, VK_DOLLAR, VK_EURO_SIGN, VK_EXCLAMATION_MARK, VK_INVERTED_EXCLAMATION_MARK, VK_LEFT_PARENTHESIS, VK_NUMBER_SIGN, VK_PLUS, VK_RIGHT_PARENTHESIS, VK_UNDERSCORE, VK_SUPER, VK_CONTEXT_MENU
    }

    public enum MouseButton {
        UNDEFINED, LEFT_BUTTON, MIDDLE_BUTTON, RIGHT_BUTTON, BUTTON_4, BUTTON_5
    }
}