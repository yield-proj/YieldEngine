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

package com.xebisco.yield.editor.old;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Splash extends JFrame {

    private final Image splashBkg;

    public Splash() throws IOException {
        setUndecorated(true);
        setAlwaysOnTop(true);
        pack();
        setSize(500, 330);
        //noinspection ConstantConditions
        splashBkg = ImageIO.read(Splash.class.getResourceAsStream("/com/xebisco/yield/editor/splashBkg.png")).getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
        setLocationRelativeTo(null);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(splashBkg, 0, 0, this);
        g.dispose();
        if(Icons.YIELD_ICON == null)
            Icons.loadAll();
    }

    @Override
    public void dispose() {
        splashBkg.flush();
        super.dispose();
    }
}
