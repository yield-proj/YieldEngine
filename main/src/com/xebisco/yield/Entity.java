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

package com.xebisco.yield;

import com.xebisco.yield.render.Renderable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * An Entity is a combination of Components and other Entities(called children), can contain game logic and be displayed on the screen.
 */
public final class Entity implements Comparable<Entity> {
    private ArrayList<Component> components = new ArrayList<>();
    private List<Entity> children = new ArrayList<>();
    private int index = 0;
    private Entity parent;
    private Transform selfTransform;
    private String name;
    private Material material = new Material();
    private static Material defaultMaterial = new Material();
    private final YldScene scene;
    private String tag = "default";
    private boolean active = true, visible = true;

    /**
     * Creates an entity with a Transform component and a Renderer component.
     *
     * @param name   The name of this Entity.
     * @param scene  The scene of this Entity.
     * @param parent The parent Entity.
     */
    public Entity(String name, YldScene scene, Entity parent) {
        if (name == null)
            name = getClass().getSimpleName();
        this.name = name;
        this.scene = scene;
        this.parent = parent;
        addComponent(selfTransform = new Transform());
        material.setColor(defaultMaterial.getColor());
        material.setTexture(defaultMaterial.getTexture());
        material.setLined(defaultMaterial.isLined());
    }

    /**
     * Updates all the components of this Entity and all the systems in the scene of this Entity.
     *
     * @param delta The time variation between the last frame and the actual one in seconds.
     */
    public void process(float delta, Set<Renderable> renderables) {
        if (active) {
            int i = 0;
            while (i < components.size()) {
                Component component = components.get(i);
                component.setFrames(component.getFrames() + 1);
                if (component.getFrames() == 1)
                    component.start();
                component.update(delta);
                component.render(renderables);
                i++;
            }

            i = 0;
            while (i < scene.getSystems().size()) {
                YldSystem sys = scene.getSystems().get(i);
                sys.receive(this, delta);

                i++;
            }
            for (i = 0; i < children.size(); i++) {
                try {
                    Entity e = children.get(i);
                    if (e != null) {
                        Set<Renderable> renderables1 = renderables;
                        if (!e.isVisible())
                            renderables1 = null;
                        e.process(delta, renderables1);
                    }
                } catch (IndexOutOfBoundsException ignore) {
                }
            }
            selfTransform.getTransformed().reset();
        }
    }

    /**
     * Adds and setts a Component to this Entity.
     *
     * @param component The component to be added.
     */
    public void addComponent(Component component) {
        component.transform = selfTransform;
        component.setGame(scene.game);
        component.setInput(new YldInput(scene.game, scene.game.input));
        component.setEntity(this);
        component.setScene(scene);
        component.setTime(scene.time);
        YldConcurrency.runTask(component::async);
        component.create();
        components.add(component);
    }

    /**
     * Search for all the Components instances in this Entity.
     *
     * @param type The class type of the component that's being searched.
     * @return The component found (null if not found)
     */
    public <T extends Component> T getComponent(Class<T> type) {
        return getComponent(type, 0);
    }

    /**
     * Search for all the Components instances in this Entity.
     *
     * @param type  The class type of the component that's being searched.
     * @param index the index of this component.
     * @return The component found (null if not found)
     */
    public <T extends Component> T getComponent(Class<T> type, int index) {
        T component = null;
        int i = 0;
        while (i < components.size()) {
            if (i < index)
                break;
            Component component1 = components.get(i);
            if (component1.getClass().getName().hashCode() == type.getName().hashCode()) {
                if (component1.getClass().getName().equals(type.getName())) {
                    component = type.cast(component1);
                    break;
                }
            }
            i++;
        }

        return component;
    }

    /**
     * This function returns a list of components of the specified type
     *
     * @param type The type of component you want to get.
     * @return A list of components of the specified type.
     */
    public <T extends Component> List<T> getComponentList(Class<T> type) {
        List<T> components = new ArrayList<>();
        int i = 0;
        while (i < this.components.size()) {
            if (i < index)
                break;
            Component component1 = this.components.get(i);
            if (component1.getClass().getName().hashCode() == type.getName().hashCode()) {
                if (component1.getClass().getName().equals(type.getName())) {
                    components.add(type.cast(component1));
                }
            }
            i++;
        }
        return components;
    }

    public <T extends Component> Component[] getComponents(Class<T> type) {
        return getComponentList(type).toArray(new Component[0]);
    }

    /**
     * Check if this Entity contains a Component with the specified class type.
     *
     * @param type The class type of the Component.
     * @return If the Entity contains the Component.
     */
    public <T extends Component> boolean containsComponent(Class<T> type) {
        return getComponent(type) != null;
    }

    /**
     * Check if this Entity parent contains a Component with the specified class type.
     *
     * @param type The class type of the Component.
     * @return If the Entity parent contains the Component.
     */
    public <T extends Component> T getComponentInParent(Class<T> type) {
        return parent.getComponent(type);
    }

    /**
     * Check if any of this Entity children contains a Component with the specified class type.
     *
     * @param type The class type of the Component.
     * @return If contains the Component.
     */
    public <T extends Component> T getComponentInChildren(Class<T> type) {
        T component = null;
        for (Entity child : children) {
            component = child.getComponent(type);
            if (component != null)
                break;
        }
        return component;
    }

    public void center() {
        getSelfTransform().goTo(scene.getView().mid());
    }

    /**
     * Gets the transform based on all of these parents.
     *
     * @return The global transform.
     */
    public Transform getTransform() {
        Transform toReturn = selfTransform.get();
        if (parent != null) {
            toReturn.translate(parent.getTransform().position);
            toReturn.rotation += parent.getTransform().rotation;
            toReturn.scale.x *= parent.getTransform().scale.x;
            toReturn.scale.y *= parent.getTransform().scale.y;
        }

        return toReturn;
    }

    /**
     * Getter of the selfTransform variable of this Entity.
     *
     * @return The selfTransform variable.
     */
    public Transform getSelfTransform() {
        return selfTransform;
    }

    /**
     * Setter of the selfTransform variable of this Entity.
     */
    public void setSelfTransform(Transform selfTransform) {
        this.selfTransform = selfTransform;
    }

    /**
     * Getter of the components list of this Entity.
     *
     * @return The components list.
     */
    public ArrayList<Component> getComponents() {
        return components;
    }

    /**
     * Setter of the components list of this Entity.
     */
    public void setComponents(ArrayList<Component> components) {
        this.components = components;
    }

    /**
     * Getter of the scene variable of this Entity.
     *
     * @return The scene variable.
     */
    public YldScene getScene() {
        return scene;
    }

    /**
     * Getter for the tag of this entity.
     *
     * @return The tag variable.
     */
    public String getTag() {
        return tag;
    }

    /**
     * Setter for the tag of this entity.
     */
    public void setTag(String tag) {
        this.tag = tag;
    }

    /**
     * Getter of the name variable of this Entity.
     *
     * @return The name variable.
     */
    public String getName() {
        return name;
    }

    /**
     * Setter of the name variable of this Entity.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * It creates an entity, sets its position, and then adds it to the children list.
     *
     * @param prefab The prefab to instantiate.
     * @param pos    The position of the entity.
     * @return The entity that was just created.
     */
    public Entity instantiate(Prefab prefab, Vector2 pos) {
        String name = "Entity";
        if (prefab != null)
            name = prefab.getClass().getName();
        Entity entity = new Entity(name, scene, this);
        if (pos != null) {
            entity.getSelfTransform().position = pos;
        }
        if (prefab != null)
            prefab.create(entity);
        return addChild(entity);
    }

    /**
     * It creates an entity, sets its position, and then adds it to the children list.
     *
     * @param prefab The prefab to instantiate.
     * @return The entity that was just created.
     */
    public Entity instantiate(Prefab prefab) {
        return instantiate(prefab, null);
    }

    /**
     * Sorts the children of this entity and calls 'sortChildren()' on all of his children.
     */
    public void sortChildren() {
        try {
            Collections.sort(children);
        } catch (NullPointerException | ConcurrentModificationException e) {
            ArrayList<Integer> toRemove = new ArrayList<>();
            for (int i = 0; i < children.size(); i++) {
                Entity e1 = children.get(i);
                if (e1 == null) {
                    toRemove.add(i);
                }
            }
            for (int i : toRemove) {
                try {
                    children.remove(i);
                } catch (IndexOutOfBoundsException ignore) {
                }
            }
        }
        for (int i = 0; i < children.size(); i++) {
            Entity e = null;
            try {
                e = children.get(i);
            } catch (ArrayIndexOutOfBoundsException ignore) {
            }
            if (e != null)
                e.sortChildren();
        }
    }

    /**
     * Destroys a child of this Entity based on the given class type.
     *
     * @param type The type of the entity that will be destroyed.
     */
    public <E extends Prefab> void destroy(Class<E> type) {
        for (Entity e : children) {
            if (e.getName().hashCode() == type.getName().hashCode()) {
                if (e.getName().equals(name)) {
                    e.destroy();
                    break;
                }
            }
        }
    }

    /**
     * Destroys this Entity.
     */
    public void destroy() {
        for (int i = 0; i < children.size(); i++) {
            Entity e = children.get(i);
            if (e != null) {
                Yld.debug(() -> Yld.log("Destroyed entity: " + e.name));
                e.destroy();
            }
        }
        while (!children.isEmpty()) {
            children.clear();
        }
        for (int i = 0; i < components.size(); i++) {
            int finalI = i;
            Yld.debug(() -> Yld.log("Called onDestroy on component: " + components.get(finalI).getClass().getSimpleName()));
            components.get(i).onDestroy();
        }
        if (parent != null) {
            parent.getChildren().remove(this);
        }
    }

    /**
     * Getter of the children list of this Entity.
     *
     * @return The children list.
     */
    public List<Entity> getChildren() {
        return children;
    }

    @Deprecated
    public Entity addChildren(Entity e) {
        children.add(e);
        return e;
    }

    /**
     * Adds an Entity to the children list.
     *
     * @param e The Entity to be added.
     * @return The added Entity.
     */
    public Entity addChild(Entity e) {
        e.setParent(this);
        children.add(e);
        return e;
    }

    private final ArrayList<Object> transmitReturn = new ArrayList<>();

    /**
     * Calls the specified method on all the components of this Entity.
     *
     * @param method    The name of the method.
     * @param arguments The arguments of the method.
     */
    public Object[] transmit(String method, Object... arguments) {
        transmitReturn.clear();
        for (Component c : components) {
            Method[] methods = c.getClass().getMethods();
            for (Method m : methods) {
                if (m.getName().hashCode() == method.hashCode() && m.getName().equals(method)) {
                    try {
                        transmitReturn.add(m.invoke(c, arguments));
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        Yld.throwException(e);
                    }
                    break;
                }
            }
        }
        return transmitReturn.toArray(new Object[0]);
    }

    /**
     * Calls the specified method on all the components on all the children of this Entity.
     *
     * @param method    The name of the method.
     * @param arguments The arguments of the method.
     */
    public void transmitToChildren(String method, Object... arguments) {
        for (Entity e : children) {
            e.transmit(method, arguments);
        }
    }

    @Override
    public String toString() {
        return "Entity{" +
                "components=" + components +
                ", children=" + children +
                ", tag='" + tag + '\'' +
                '}';
    }

    /**
     * Setter of the children list of this Entity.
     */
    public void setChildren(List<Entity> children) {
        this.children = children;
    }

    /**
     * Getter of the parent variable of this Entity.
     *
     * @return the parent variable.
     */
    public Entity getParent() {
        return parent;
    }

    /**
     * Setter of the parent variable of this Entity.
     */
    public void setParent(Entity parent) {
        this.parent = parent;
    }

    /**
     * @return The render index of this Entity. (not influenced by the parent Entity)
     */
    public int getIndex() {
        return index;
    }

    /**
     * Setter of the index variable of this Entity.
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * Getter of the material variable of this Entity.
     *
     * @return the material variable.
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * Setter of the material variable of this Entity.
     */
    public void setMaterial(Material material) {
        this.material = material;
    }

    /**
     * Getter of the default material.
     *
     * @return the default material variable.
     */
    public static Material getDefaultMaterial() {
        return defaultMaterial;
    }

    /**
     * Setter of the default material.
     */
    public static void setDefaultMaterial(Material defaultMaterial) {
        Entity.defaultMaterial = defaultMaterial;
    }

    /**
     * Getter of the active material.
     *
     * @return the active material variable.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Setter of the active material.
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public int compareTo(Entity o) {
        return Integer.compare(o.getIndex(), index);
    }

    /**
     * This function sets the visibility of the object to the value of the parameter.
     *
     * @param visible This is a boolean value that determines whether the object is visible or not.
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * Returns true if the object is visible, false otherwise.
     *
     * @return The value of the variable visible.
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * This function returns the transmitReturn ArrayList
     *
     * @return An ArrayList of Objects.
     */
    public ArrayList<Object> getTransmitReturn() {
        return transmitReturn;
    }
}
