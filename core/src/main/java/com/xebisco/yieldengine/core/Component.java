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

package com.xebisco.yieldengine.core;

public abstract class Component extends OnSceneBehavior {
    private Entity entity;

    @Override
    public void onCreate() {

    }

    @Override
    public void onLateUpdate() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onUpdate() {

    }

    @Override
    public long getFrames() {
        return getEntity().getFrames();
    }

    public Entity getEntity() {
        return entity;
    }

    Component setEntity(Entity entity) {
        this.entity = entity;
        return this;
    }

    protected Transform getTransform() {
        return getEntity().getTransform();
    }

    protected Component getComponent(Class<? extends Component> componentClass, int index) {
        return getEntity().getComponent(componentClass, index);
    }

    protected Component getComponent(Class<? extends Component> componentClass) {
        return getEntity().getComponent(componentClass);
    }
}
