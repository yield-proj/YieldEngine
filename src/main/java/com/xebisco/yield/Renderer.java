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

package com.xebisco.yield;

import com.xebisco.yield.Component;
import com.xebisco.yield.Obj;
import com.xebisco.yield.YldGraphics;

public class Renderer extends Component {
    private final YldGraphics graphics = new YldGraphics();
    private boolean show = true;

    @Override
    public void update(float delta) {
        if (show) {
            for(int i = 0; i < graphics.shapeRends.size(); i++) {
                Obj obj = graphics.shapeRends.get(i);
                if (!game.getScene().getGraphics().shapeRends.contains(obj))
                    game.getScene().getGraphics().shapeRends.add(obj);
            }
        } else {
            for(int i = 0; i < graphics.shapeRends.size(); i++) {
                Obj obj = graphics.shapeRends.get(i);
                game.getScene().getGraphics().shapeRends.remove(obj);
            }
        }
        while (graphics.shapeRends.size() > 0)
            graphics.shapeRends.clear();
    }

    public YldGraphics getGraphics() {
        return graphics;
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }
}
