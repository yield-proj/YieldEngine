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

package com.xebisco.yield.editor.scene;

import com.xebisco.yield.ComponentIcon;
import com.xebisco.yield.ComponentIconType;
import com.xebisco.yield.editor.Assets;

import javax.swing.*;
import java.awt.*;

public class ComponentListRender extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        Class<?> v = (Class<?>) value;
        if (v.isAnnotationPresent(ComponentIcon.class)) {
            ComponentIconType icon = v.getAnnotation(ComponentIcon.class).iconType();
            setIcon(switch (icon) {
                case TRANSFORM -> Assets.images.get("transformIcon.png");
                case PHYSICS -> Assets.images.get("physicsIcon.png");
                case GRAPHICAL -> Assets.images.get("graphicalIcon.png");
                case ANIMATION -> Assets.images.get("animationIcon.png");
                case AUDIO -> Assets.images.get("audioIcon.png");
            });
        } else {
            setIcon(Assets.images.get("scriptIcon.png"));
        }
        setText(v.getSimpleName());
        return this;
    }
}
