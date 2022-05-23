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

import java.util.ArrayList;

/**
 * A simpler way to write a Component
 * @since 4_1.1.1
 * @author Xebisco
 */
public abstract class YldScript extends Component {
    @Override
    public final void render(SampleGraphics graphics) {
    }

    @Override
    public final Entity instantiate(Prefab prefab) {
        return super.instantiate(prefab);
    }

    @Override
    public final Entity instantiate() {
        return super.instantiate();
    }

    @Override
    public final void destroy() {
        super.destroy();
    }

    @Override
    public final <E extends Prefab> void destroy(Class<E> type) {
        super.destroy(type);
    }

    @Override
    public final int getFrames() {
        return super.getFrames();
    }

    @Override
    public final void setFrames(int frames) {
        super.setFrames(frames);
    }

    @Override
    public final void addComponent(Component component) {
        super.addComponent(component);
    }

    @Override
    public final <T extends Component> T getComponent(Class<T> type) {
        return super.getComponent(type);
    }

    @Override
    public final <T extends Component> T getComponent(Class<T> type, int index) {
        return super.getComponent(type, index);
    }

    @Override
    public final <T extends Component> T getComponentInChildren(Class<T> type) {
        return super.getComponentInChildren(type);
    }

    @Override
    public final <T extends Component> T getComponentInParent(Class<T> type) {
        return super.getComponentInParent(type);
    }

    @Override
    public final void setMaterial(Material material) {
        super.setMaterial(material);
    }

    @Override
    public final Material getMaterial() {
        return super.getMaterial();
    }

    @Override
    public final ArrayList<Component> getComponents() {
        return super.getComponents();
    }

    @Override
    public final <T extends Component> boolean containsComponent(Class<T> type) {
        return super.containsComponent(type);
    }

    @Override
    public final Entity getEntity() {
        return super.getEntity();
    }

    @Override
    public final void setEntity(Entity entity) {
        super.setEntity(entity);
    }

    @Override
    public final Transform getTransform() {
        return super.getTransform();
    }

    @Override
    public final Transform getSelfTransform() {
        return super.getSelfTransform();
    }

    @Override
    public final void setSelfTransform(Transform selfTransform) {
        super.setSelfTransform(selfTransform);
    }

    @Override
    public final YldInput getInput() {
        return super.getInput();
    }

    @Override
    public final void setInput(YldInput input) {
        super.setInput(input);
    }

    @Override
    public final YldScene getScene() {
        return super.getScene();
    }

    @Override
    public final void setScene(YldScene scene) {
        super.setScene(scene);
    }

    @Override
    public final YldTime getTime() {
        return super.getTime();
    }

    @Override
    public final void setTime(YldTime time) {
        super.setTime(time);
    }

    @Override
    public final String getTag() {
        return super.getTag();
    }

    @Override
    public final void setTag(String tag) {
        super.setTag(tag);
    }

    @Override
    public final YldGame getGame() {
        return super.getGame();
    }

    @Override
    public final void setGame(YldGame game) {
        super.setGame(game);
    }
}
