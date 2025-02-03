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

import org.joml.Quaternionf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Entity extends OnSceneBehavior implements Comparable<Entity> {
    private final EntityHeader header;
    private Entity parent;
    private final List<Entity> children = new ArrayList<>();
    private final ArrayList<Component> components = new ArrayList<>();
    private final Transform transform;
    private int preferredIndex;
    private final Transform worldTransform = new Transform();
    private boolean started;

    public Entity(EntityHeader header, Transform transform) {
        this.header = header;
        this.transform = transform;
    }

    private void sortChildren() {
        Collections.sort(children);
    }

    public void addToParent(Entity parent) {
        Global.getCurrentScene().getEntities().remove(this);
        setParent(parent);
        parent.children.add(this);
    }

    public Entity[] getAllChildren() {
        List<Entity> entities = new ArrayList<>(getChildren());
        for (Entity e : getChildren()) {
            Collections.addAll(entities, e.getAllChildren());
        }
        return entities.toArray(new Entity[0]);
    }

    public void removeFromParent() {
        parent.children.remove(this);
        setParent(null);
    }

    public void addToSceneRoot() {
        if (parent != null) removeFromParent();
        Global.getCurrentScene().getEntities().add(this);
    }

    @Override
    public void onCreate() {
        sortChildren();
        components.forEach(c -> {
            c.setEntity(this);
            c.setWorldTransform(worldTransform);
        });
        components.forEach(Component::onCreate);
        children.forEach(Entity::onCreate);
    }

    @Override
    public void onStart() {
        if (!header.isEnabled()) return;
        sortChildren();
        updateWorldTransform();
        components.forEach(Component::onStart);
        children.forEach(Entity::onStart);
        started = true;
    }

    @Override
    public void onUpdate() {
        if (!header.isEnabled()) return;
        if (!started) onStart();
        sortChildren();
        components.forEach(c -> {
            if (!Global.getCurrentScene().isStopComponentUpdate()) c.onUpdate();
            c.onUnstoppableUpdate();
        });
        children.forEach(Entity::onUpdate);
        setFrames(getFrames() + 1);
    }

    @Override
    public void onLateUpdate() {
        if (!header.isEnabled()) return;
        updateWorldTransform();
        components.forEach(c -> {
            if (!Global.getCurrentScene().isStopComponentUpdate()) c.onLateUpdate();
        });
        children.forEach(Entity::onLateUpdate);
    }

    @Override
    public void dispose() {
        if (!header.isEnabled()) return;
        super.dispose();
        components.forEach(Component::dispose);
        children.forEach(Entity::dispose);
        children.clear();
    }

    @Override
    public int compareTo(Entity o) {
        return Integer.compare(o.preferredIndex, preferredIndex);
    }

    public boolean containsTag(String tag) {
        for (String t : header.getTags()) if (tag.hashCode() == t.hashCode() && tag.equals(t)) return true;
        return false;
    }

    public Component getComponent(Class<? extends Component> componentClass, int index) {
        int i = 0;
        for (Component component : components) {
            if (component.getClass().isAssignableFrom(componentClass)) {
                if (i++ >= index) {
                    return component;
                }
            }
        }
        return null;
    }

    public Component getComponent(Class<? extends Component> componentClass) {
        return getComponent(componentClass, 0);
    }

    public boolean containsComponent(Class<? extends Component> componentClass) {
        return getComponent(componentClass) != null;
    }

    public void updateWorldTransform() {
        worldTransform.getTransformMatrix().set(getNewWorldTransform().getTransformMatrix());
    }

    public Entity addComponents(Component... components) {
        Collections.addAll(getComponents(), components);
        return this;
    }

    private Transform getNewWorldTransform() {
        if (parent == null) return transform;
        Transform worldTransform = new Transform(parent.getNewWorldTransform());
        worldTransform.getTransformMatrix().translationRotateScale(worldTransform.getTranslation(), worldTransform.getRotation().get(new Quaternionf()), worldTransform.getScale());
        return worldTransform;
    }

    public Entity getParent() {
        return parent;
    }

    private Entity setParent(Entity parent) {
        this.parent = parent;
        return this;
    }

    public List<Entity> getChildren() {
        return children;
    }

    public ArrayList<Component> getComponents() {
        return components;
    }

    public Transform getTransform() {
        return transform;
    }

    public int getPreferredIndex() {
        return preferredIndex;
    }

    public Entity setPreferredIndex(int preferredIndex) {
        this.preferredIndex = preferredIndex;
        return this;
    }

    public Transform getWorldTransform() {
        return worldTransform;
    }

    public EntityHeader getHeader() {
        return header;
    }

    public boolean isStarted() {
        return started;
    }

    public Entity setStarted(boolean started) {
        this.started = started;
        return this;
    }
}
