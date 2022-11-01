/*
 * Copyright [2022] [Xebisco]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashSet;
import java.util.Set;

public class EditorMain implements MouseListener {
    private static JFrame frame;
    private static Set<Integer> mousePressing = new HashSet<>();

    public static void main(String[] args) {
        frame = new JFrame();
        frame.setSize(1280, 720);
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);
        frame.add(new Renderer());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.addMouseListener(new EditorMain());
        System.out.println(TextInputWindow.request("", "TESTTEXT"));
    }

    public static JFrame getFrame() {
        return frame;
    }

    public static void setFrame(JFrame frame) {
        EditorMain.frame = frame;
    }

    public static Set<Integer> getMousePressing() {
        return mousePressing;
    }

    public static void setMousePressing(Set<Integer> mousePressing) {
        EditorMain.mousePressing = mousePressing;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        mousePressing.add(e.getButton());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mousePressing.remove(e.getButton());
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}