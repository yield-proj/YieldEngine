/*
 * Copyright [2022] [Xebisco]
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

package com.xebisco.yield.systems;

import com.xebisco.yield.Component;
import com.xebisco.yield.ProcessSystem;
import com.xebisco.yield.Transform;

public class MiddlePointSystem extends ProcessSystem
{
    @Override
    public String[] componentFilters()
    {
        return new String[]{
                Transform.class.getName()
        };
    }

    @Override
    public void destroy() {

    }

    @Override
    public void process(Component component, float delta)
    {
        Transform transform = (Transform) component;
        if (transform.middleRotation)
        {
            transform.middle.x = transform.position.x;
            transform.middle.y = transform.position.y;
        }
    }
}
