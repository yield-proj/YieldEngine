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

package com.xebisco.yield.systems;

import com.xebisco.yield.Component;
import com.xebisco.yield.ProcessSystem;
import com.xebisco.yield.RectTrigger;
import com.xebisco.yield.Transform;

import java.util.HashSet;
import java.util.Set;

public class RectTriggerSystem extends ProcessSystem {
    @Override
    public String[] componentFilters() {
        return new String[]{
                RectTrigger.class.getName()
        };
    }

    private final Set<RectTrigger> triggers = new HashSet<>();
    private final Set<RectTrigger> triggersTriggered = new HashSet<>();

    public Set<RectTrigger> getTriggers() {
        return triggers;
    }

    public Set<RectTrigger> getTriggersTriggered() {
        return triggersTriggered;
    }

    @Override
    public void process(Component component, float delta) {
        RectTrigger trigger = (RectTrigger) component;
        triggers.add(trigger);
        for (RectTrigger rectTrigger : triggers) {
            if (rectTrigger != trigger) {
                Transform t = trigger.getTransform();
                if (rectTrigger.colliding(t.position.sum(trigger.getOffset()), trigger.getSize(), t.scale)) {
                    if (!triggersTriggered.contains(rectTrigger) || !triggersTriggered.contains(trigger)) {
                        if (rectTrigger.isTransmit())
                            rectTrigger.getEntity().transmit("triggerEnter", rectTrigger.getEntity().getTag());
                        if (trigger.isTransmit())
                            trigger.getEntity().transmit("triggerEnter", rectTrigger.getEntity().getTag());
                    }
                    triggersTriggered.add(rectTrigger);
                    triggersTriggered.add(trigger);
                } else {
                    triggersTriggered.remove(rectTrigger);
                    triggersTriggered.remove(trigger);
                }
            }
        }
    }
}
