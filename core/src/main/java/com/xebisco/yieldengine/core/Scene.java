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

import com.xebisco.yieldengine.core.camera.ICamera;
import com.xebisco.yieldengine.core.camera.OrthoCamera;
import com.xebisco.yieldengine.core.rendering.Render;

import java.util.TreeSet;

public final class Scene {
    public final class SceneController extends OnSceneBehavior {
        @Override
        public void onCreate() {
            entities.forEach(Entity::onCreate);
        }

        @Override
        public void onStart() {
            entities.forEach(Entity::onStart);
        }

        @Override
        public void onUpdate() {
            System.out.println(1 / Time.getDeltaTime());
            entities.forEach(Entity::onUpdate);
        }

        @Override
        public void dispose() {
            super.dispose();
            entities.forEach(Entity::dispose);
        }
    }

    private ICamera camera = new OrthoCamera();

    private final SceneController sceneController = new SceneController();

    private final TreeSet<Entity> entities = new TreeSet<>();

    public SceneController getSceneController() {
        return sceneController;
    }

    public ICamera getCamera() {
        return camera;
    }

    public Scene setCamera(ICamera camera) {
        this.camera = camera;
        return this;
    }

    public TreeSet<Entity> getEntities() {
        return entities;
    }
}
