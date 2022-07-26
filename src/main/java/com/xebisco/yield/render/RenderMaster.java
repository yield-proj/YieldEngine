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

package com.xebisco.yield.render;

import com.xebisco.yield.*;
import com.xebisco.yield.config.WindowConfiguration;

import java.util.Set;


/**
 * @since 4-1.2
 * @author Xebisco
 */
public interface RenderMaster extends VisualUtils, AudioUtils {
    SampleGraphics initGraphics();

    SampleGraphics specificGraphics();

    void before(YldGame game);

    SampleWindow initWindow(WindowConfiguration configuration);

    void frameStart(SampleGraphics graphics, View view);

    void frameEnd();

    boolean canStart();

    float fpsCount();

    Set<Integer> pressing();

    int mouseX();

    int mouseY();

    void close();
}
