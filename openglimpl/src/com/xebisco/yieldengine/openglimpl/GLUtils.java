/*
 * Copyright [2022-2024] [Xebisco]
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

package com.xebisco.yieldengine.openglimpl;

import com.xebisco.yieldengine.Input;
import com.xebisco.yieldengine.Transform2D;
import com.xebisco.yieldengine.Vector2D;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;

import java.io.*;

public class GLUtils {
    public static int booleanToInt(boolean b) {
        return b ? 1 : 0;
    }

    public static Input.Key intToKey(int k) {
        return switch (k) {
            case GLFW.GLFW_KEY_SPACE -> Input.Key.VK_SPACE;
            case GLFW.GLFW_KEY_APOSTROPHE -> Input.Key.VK_QUOTE;
            case GLFW.GLFW_KEY_COMMA -> Input.Key.VK_COMMA;
            case GLFW.GLFW_KEY_MINUS -> Input.Key.VK_MINUS;
            case GLFW.GLFW_KEY_PERIOD -> Input.Key.VK_PERIOD;
            case GLFW.GLFW_KEY_SLASH -> Input.Key.VK_SLASH;
            case GLFW.GLFW_KEY_0 -> Input.Key.VK_0;
            case GLFW.GLFW_KEY_1 -> Input.Key.VK_1;
            case GLFW.GLFW_KEY_2 -> Input.Key.VK_2;
            case GLFW.GLFW_KEY_3 -> Input.Key.VK_3;
            case GLFW.GLFW_KEY_4 -> Input.Key.VK_4;
            case GLFW.GLFW_KEY_5 -> Input.Key.VK_5;
            case GLFW.GLFW_KEY_6 -> Input.Key.VK_6;
            case GLFW.GLFW_KEY_7 -> Input.Key.VK_7;
            case GLFW.GLFW_KEY_8 -> Input.Key.VK_8;
            case GLFW.GLFW_KEY_9 -> Input.Key.VK_9;
            case GLFW.GLFW_KEY_SEMICOLON -> Input.Key.VK_SEMICOLON;
            case GLFW.GLFW_KEY_EQUAL, GLFW.GLFW_KEY_KP_EQUAL -> Input.Key.VK_EQUALS;
            case GLFW.GLFW_KEY_A -> Input.Key.VK_A;
            case GLFW.GLFW_KEY_B -> Input.Key.VK_B;
            case GLFW.GLFW_KEY_C -> Input.Key.VK_C;
            case GLFW.GLFW_KEY_D -> Input.Key.VK_D;
            case GLFW.GLFW_KEY_E -> Input.Key.VK_E;
            case GLFW.GLFW_KEY_F -> Input.Key.VK_F;
            case GLFW.GLFW_KEY_G -> Input.Key.VK_G;
            case GLFW.GLFW_KEY_H -> Input.Key.VK_H;
            case GLFW.GLFW_KEY_I -> Input.Key.VK_I;
            case GLFW.GLFW_KEY_J -> Input.Key.VK_J;
            case GLFW.GLFW_KEY_K -> Input.Key.VK_K;
            case GLFW.GLFW_KEY_L -> Input.Key.VK_L;
            case GLFW.GLFW_KEY_M -> Input.Key.VK_M;
            case GLFW.GLFW_KEY_N -> Input.Key.VK_N;
            case GLFW.GLFW_KEY_O -> Input.Key.VK_O;
            case GLFW.GLFW_KEY_P -> Input.Key.VK_P;
            case GLFW.GLFW_KEY_Q -> Input.Key.VK_Q;
            case GLFW.GLFW_KEY_R -> Input.Key.VK_R;
            case GLFW.GLFW_KEY_S -> Input.Key.VK_S;
            case GLFW.GLFW_KEY_T -> Input.Key.VK_T;
            case GLFW.GLFW_KEY_U -> Input.Key.VK_U;
            case GLFW.GLFW_KEY_V -> Input.Key.VK_V;
            case GLFW.GLFW_KEY_W -> Input.Key.VK_W;
            case GLFW.GLFW_KEY_X -> Input.Key.VK_X;
            case GLFW.GLFW_KEY_Y -> Input.Key.VK_Y;
            case GLFW.GLFW_KEY_Z -> Input.Key.VK_Z;
            case GLFW.GLFW_KEY_LEFT_BRACKET -> Input.Key.VK_OPEN_BRACKET;
            case GLFW.GLFW_KEY_BACKSLASH -> Input.Key.VK_BACK_SLASH;
            case GLFW.GLFW_KEY_RIGHT_BRACKET -> Input.Key.VK_CLOSE_BRACKET;
            case GLFW.GLFW_KEY_ESCAPE -> Input.Key.VK_ESCAPE;
            case GLFW.GLFW_KEY_ENTER, GLFW.GLFW_KEY_KP_ENTER -> Input.Key.VK_ENTER;
            case GLFW.GLFW_KEY_TAB -> Input.Key.VK_TAB;
            case GLFW.GLFW_KEY_BACKSPACE -> Input.Key.VK_BACK_SPACE;
            case GLFW.GLFW_KEY_INSERT -> Input.Key.VK_INSERT;
            case GLFW.GLFW_KEY_DELETE -> Input.Key.VK_DELETE;
            case GLFW.GLFW_KEY_RIGHT -> Input.Key.VK_RIGHT;
            case GLFW.GLFW_KEY_LEFT -> Input.Key.VK_LEFT;
            case GLFW.GLFW_KEY_DOWN -> Input.Key.VK_DOWN;
            case GLFW.GLFW_KEY_UP -> Input.Key.VK_UP;
            case GLFW.GLFW_KEY_PAGE_UP -> Input.Key.VK_PAGE_UP;
            case GLFW.GLFW_KEY_PAGE_DOWN -> Input.Key.VK_PAGE_DOWN;
            case GLFW.GLFW_KEY_HOME -> Input.Key.VK_HOME;
            case GLFW.GLFW_KEY_END -> Input.Key.VK_END;
            case GLFW.GLFW_KEY_CAPS_LOCK -> Input.Key.VK_CAPS_LOCK;
            case GLFW.GLFW_KEY_SCROLL_LOCK -> Input.Key.VK_SCROLL_LOCK;
            case GLFW.GLFW_KEY_NUM_LOCK -> Input.Key.VK_NUM_LOCK;
            case GLFW.GLFW_KEY_PRINT_SCREEN -> Input.Key.VK_PRINTSCREEN;
            case GLFW.GLFW_KEY_PAUSE -> Input.Key.VK_PAUSE;
            case GLFW.GLFW_KEY_F1 -> Input.Key.VK_F1;
            case GLFW.GLFW_KEY_F2 -> Input.Key.VK_F2;
            case GLFW.GLFW_KEY_F3 -> Input.Key.VK_F3;
            case GLFW.GLFW_KEY_F4 -> Input.Key.VK_F4;
            case GLFW.GLFW_KEY_F5 -> Input.Key.VK_F5;
            case GLFW.GLFW_KEY_F6 -> Input.Key.VK_F6;
            case GLFW.GLFW_KEY_F7 -> Input.Key.VK_F7;
            case GLFW.GLFW_KEY_F8 -> Input.Key.VK_F8;
            case GLFW.GLFW_KEY_F9 -> Input.Key.VK_F9;
            case GLFW.GLFW_KEY_F10 -> Input.Key.VK_F10;
            case GLFW.GLFW_KEY_F11 -> Input.Key.VK_F11;
            case GLFW.GLFW_KEY_F12 -> Input.Key.VK_F12;
            case GLFW.GLFW_KEY_KP_0 -> Input.Key.VK_NUMPAD0;
            case GLFW.GLFW_KEY_KP_1 -> Input.Key.VK_NUMPAD1;
            case GLFW.GLFW_KEY_KP_2 -> Input.Key.VK_NUMPAD2;
            case GLFW.GLFW_KEY_KP_3 -> Input.Key.VK_NUMPAD3;
            case GLFW.GLFW_KEY_KP_4 -> Input.Key.VK_NUMPAD4;
            case GLFW.GLFW_KEY_KP_5 -> Input.Key.VK_NUMPAD5;
            case GLFW.GLFW_KEY_KP_6 -> Input.Key.VK_NUMPAD6;
            case GLFW.GLFW_KEY_KP_7 -> Input.Key.VK_NUMPAD7;
            case GLFW.GLFW_KEY_KP_8 -> Input.Key.VK_NUMPAD8;
            case GLFW.GLFW_KEY_KP_9 -> Input.Key.VK_NUMPAD9;
            case GLFW.GLFW_KEY_KP_DECIMAL -> Input.Key.VK_DECIMAL;
            case GLFW.GLFW_KEY_KP_DIVIDE -> Input.Key.VK_DIVIDE;
            case GLFW.GLFW_KEY_KP_MULTIPLY -> Input.Key.VK_MULTIPLY;
            case GLFW.GLFW_KEY_KP_SUBTRACT -> Input.Key.VK_SUBTRACT;
            case GLFW.GLFW_KEY_KP_ADD -> Input.Key.VK_ADD;
            case GLFW.GLFW_KEY_LEFT_SHIFT, GLFW.GLFW_KEY_RIGHT_SHIFT -> Input.Key.VK_SHIFT;
            case GLFW.GLFW_KEY_LEFT_CONTROL, GLFW.GLFW_KEY_RIGHT_CONTROL -> Input.Key.VK_CONTROL;
            case GLFW.GLFW_KEY_LEFT_ALT, GLFW.GLFW_KEY_RIGHT_ALT -> Input.Key.VK_ALT;
            case GLFW.GLFW_KEY_LEFT_SUPER, GLFW.GLFW_KEY_RIGHT_SUPER -> Input.Key.VK_SUPER;
            case GLFW.GLFW_KEY_MENU -> Input.Key.VK_CONTEXT_MENU;
            default -> Input.Key.UNDEFINED;
        };
    }

    public static Matrix4f orthoViewMatrix(Transform2D camera2D, Vector2D gameResolution) {
        float w = (float) (gameResolution.width() * camera2D.scale().x());
        float h = (float) (gameResolution.height() * camera2D.scale().y());
        float x = (float) (camera2D.position().x() - w / 2);
        float y = (float) (camera2D.position().y() - h / 2);
        return new Matrix4f().identity().ortho2D(x, w + x, y, y + h);
    }

    public static Input.MouseButton intToMouseButton(int i) {
        return switch (i) {
            case GLFW.GLFW_MOUSE_BUTTON_LEFT -> Input.MouseButton.LEFT_BUTTON;
            case GLFW.GLFW_MOUSE_BUTTON_RIGHT -> Input.MouseButton.RIGHT_BUTTON;
            case GLFW.GLFW_MOUSE_BUTTON_MIDDLE -> Input.MouseButton.MIDDLE_BUTTON;
            case GLFW.GLFW_MOUSE_BUTTON_4 -> Input.MouseButton.BUTTON_4;
            case GLFW.GLFW_MOUSE_BUTTON_5 -> Input.MouseButton.BUTTON_5;
            default -> Input.MouseButton.UNDEFINED;
        };
    }

    public static String inputStreamToString(InputStream is) {
        StringBuilder c = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new BufferedInputStream(is)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                c.append(line).append('\n');
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return c.toString();
    }
}
