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

package com.xebisco.yield.ux;

import com.xebisco.yield.Component;
import com.xebisco.yield.DevelopmentFase;
import com.xebisco.yield.NotFinished;
import com.xebisco.yield.render.Renderable;

import java.util.LinkedHashSet;
import java.util.Set;

@NotFinished(fase = DevelopmentFase.EXPERIMENTAL)
public class UXCanvas extends Component {
    private final Set<UXPanel> panels = new LinkedHashSet<>();

    @Override
    public void render(Set<Renderable> renderables) {
        for (UXPanel panel : panels) {
            panel.setX((int) transform.position.x);
            panel.setY((int) transform.position.y);
            if (panel.getRenderMaster() == null) {
                panel.setRenderMaster(getGame().getHandler().getRenderMaster());
                panel.repaint();
            }
            renderables.addAll(panel.getRenderables());
        }
    }

    public void add(UXPanel panel) {
        panels.add(panel);
    }

    public void remove(UXPanel panel) {
        panels.remove(panel);
        panel.setRenderMaster(null);
    }

    public Set<UXPanel> getPanels() {
        return panels;
    }
}
