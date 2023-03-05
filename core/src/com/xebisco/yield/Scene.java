/*
 * Copyright [2022-2023] [Xebisco]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.xebisco.yield;

import java.util.HashSet;
import java.util.Set;

public abstract class Scene extends Entity2DContainer implements Behavior {
    private int frames;
    private Color backGroundColor = Colors.GRAY.darker();
    private Set<SystemBehavior> systems = new HashSet<>();

    public Scene(Application application) {
        super(application);
    }

    public int getFrames() {
        return frames;
    }

    public void setFrames(int frames) {
        this.frames = frames;
    }

    public Color getBackGroundColor() {
        return backGroundColor;
    }

    public void setBackGroundColor(Color backGroundColor) {
        this.backGroundColor = backGroundColor;
    }

    public Set<SystemBehavior> getSystems() {
        return systems;
    }

    public void setSystems(Set<SystemBehavior> systems) {
        this.systems = systems;
    }
}
