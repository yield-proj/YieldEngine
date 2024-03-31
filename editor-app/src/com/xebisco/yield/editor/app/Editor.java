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

package com.xebisco.yield.editor.app;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Editor {
    public final static HashMap<String, HashMap<String, Serializable>> STD_PROJECT_VALUES = new HashMap<>();

    static {
        Map<String, Serializable> general = new HashMap<>();
        general.put("p_t_general_projectName", "");
        general.put("p_t_general_projectIcon", new File("icon.png"));
        general.put("p_t_general_projectIcon", new File("icon.png"));
    }
}


/*
public void mouseWheelMoved(MouseWheelEvent e) {
            Point2D before = null;
            Point2D after = null;

            AffineTransform originalTransform = new AffineTransform(at);
            AffineTransform zoomedTransform = new AffineTransform();

            try {
                before = originalTransform.inverseTransform(e.getPoint(), null);
            } catch (NoninvertibleTransformException e1) {
                e1.printStackTrace();
            }

            zoom *= 1-(e.getPreciseWheelRotation()/5);
            ((Painter) Frame1.painter).setScale(zoom/100);

            zoomedTransform.setToIdentity();
            zoomedTransform.translate(getWidth()/2, getHeight()/2);
            zoomedTransform.scale(scale, scale);
            zoomedTransform.translate(-getWidth()/2, -getHeight()/2);
            zoomedTransform.translate(translateX, translateY);

            try {
                after = zoomedTransform.inverseTransform(e.getPoint(), null);
            } catch (NoninvertibleTransformException e1) {
                e1.printStackTrace();
            }


            double deltaX = after.getX() - before.getX();
            double deltaY = after.getY() - before.getY();
            translate(deltaX,deltaY);

            Frame1.painter.repaint();
}
 */