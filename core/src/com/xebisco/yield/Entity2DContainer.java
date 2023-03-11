/*
 * Copyright [2022-2023] [Xebisco]
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

import java.util.ArrayList;
import java.util.List;

public class Entity2DContainer {
    private List<Entity2D> entities = new ArrayList<>();
    private final Application application;

    public Entity2DContainer(Application application) {
        this.application = application;
    }

    public Entity2D instantiate(Entity2DPrefab prefab, EntityStarter entityStarter) {
        Entity2D entity = new Entity2D(application, prefab.getChildren(), prefab.getComponents());
        List<Entity2D> entities = new ArrayList<>(getEntities());
        entities.add(entity);
        entities.sort(Entity2D::compareTo);
        setEntities(entities);
        if(entityStarter != null)
            entityStarter.start(entity);
        return entity;
    }

    public Entity2D instantiate(Entity2DPrefab prefab) {
        return instantiate(prefab, null);
    }

    public boolean remove(Entity2D entity) {
        entity.dispose();
        return getEntities().remove(entity);
    }

    public List<Entity2D> getEntities() {
        return entities;
    }

    public void setEntities(List<Entity2D> entities) {
        this.entities = entities;
    }

    public Application getApplication() {
        return application;
    }
}
