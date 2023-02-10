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

/**
 * This is an implementation of YldSystem that process every Component in a YldScene instance.
 */
public abstract class ProcessSystem extends YldSystem
{
    /**
     * What components will be processed in this system, if returns null, will process all of them.
     */
    public abstract String[] componentFilters();

    @Override
    public final void receive(Entity e, float delta) {
        for(int i = 0; i < e.getComponents().size(); i++)
        {
            Component component = e.getComponents().get(i);
            boolean call = false;
            if (componentFilters() != null)
            {
                for (int i4 = 0; i4 < componentFilters().length; i4++)
                {
                    if (component.getClass().getName().hashCode() == componentFilters()[i4].hashCode())
                    {
                        if (component.getClass().getName().equals(componentFilters()[i4]))
                        {
                            call = true;
                            break;
                        }
                    }
                }
            }
            else
            {
                call = true;
            }

            if (call)
                process(component, delta);
        }
    }

    /**
     * Process all the components of a YldScene instance on every frame.
     * @param component The actual component to be processed.
     * @param delta The time variation between the last frame and the actual one in seconds.
     */
    public abstract void process(Component component, float delta);
}
