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

public abstract class EntitySystem extends YldSystem {
    public abstract Class<?>[] entityContains();

    @Override
    public final void receive(Entity e, float delta) {
        boolean call = true;
        if(entityContains() != null) {
            call = false;
            for (Class<?> comp : entityContains()) {
                for (int i = 0; i < e.getComponents().size(); i++) {
                    call = e.getComponents().get(i).getClass().getName().hashCode() == comp.getName().hashCode();
                    if (call)
                        break;
                }
            }
        }
        if (call)
            process(e, delta);
    }

    public abstract void process(Entity entity, float delta);
}
