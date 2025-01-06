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

import com.xebisco.yieldengine.concurrency.LockProcess;
import com.xebisco.yieldengine.core.camera.ICamera;
import com.xebisco.yieldengine.core.camera.OrthoCamera;
import com.xebisco.yieldengine.core.graphics.Graphics;
import com.xebisco.yieldengine.core.graphics.IPainter;
import com.xebisco.yieldengine.core.graphics.yldg1.Paint;
import com.xebisco.yieldengine.core.input.Input;
import com.xebisco.yieldengine.core.io.IO;
import com.xebisco.yieldengine.utils.Color4f;
import com.xebisco.yieldengine.utils.Editable;
import com.xebisco.yieldengine.utils.Visible;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public final class Scene implements IDispose, Serializable {
    public Scene(ArrayList<EntityFactory> entityFactories) {
        this.entityFactories = entityFactories;
    }

    public final class SceneController extends OnSceneBehavior implements Serializable {
        @Override
        public void onCreate() {
            Logger.getInstance().engineDebug("Creating " + this + ".");
            if (entityFactories != null)
                entityFactories.forEach(fac -> entities.add(fac.createEntity()));
            Logger.getInstance().engineDebug("Creating " + this + " entities.");
            entities.forEach(Entity::onCreate);
            Logger.getInstance().engineDebug(this + " created.");
        }

        @Override
        public void onStart() {
            Logger.getInstance().engineDebug("Starting " + this + ".");
            entities.forEach(Entity::onStart);
            Logger.getInstance().engineDebug(this + " started.");
        }

        private final LockProcess renderLockProcess = new LockProcess(false);

        @Override
        public void onUpdate() {
            Input input = Input.getInstance();
            if (input != null) {
                input.updateKeyList();
                input.updateMouseButtonList();
            }

            Queue<IPainter> painters = new LinkedList<>();

            painters.add(g -> g.getG1().clearScreen(new Paint().setColor(backgroundColor)));

            entities.forEach(e -> {
                e.onUpdate();
                addAllPainters(e, painters);
            });

            try {
                renderLockProcess.aWait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            renderLockProcess.getRunning().set(true);

            CompletableFuture.runAsync(() -> {
                Graphics.getPainterReceiver().paint(painters);
                renderLockProcess.unlock();
            });
        }

        private void addAllPainters(Entity e, Collection<IPainter> painters) {
            for (Component c : e.getComponents()) {
                if (c instanceof IPainter)
                    painters.add((IPainter) c);
            }
            e.getChildren().forEach(child -> addAllPainters(child, painters));
        }

        @Override
        public void onLateUpdate() {
            entities.forEach(Entity::onLateUpdate);
        }

        @Override
        public void dispose() {
            super.dispose();
            Logger.getInstance().engineDebug("Disposing " + this + ".");
            entities.forEach(Entity::dispose);
            IO.getInstance().unloadAllTextures();
            IO.getInstance().unloadAllFonts();
            Logger.getInstance().engineDebug(this + " disposed.");
        }
    }

    private ICamera camera = new OrthoCamera();

    private final SceneController sceneController = new SceneController();

    private final transient List<Entity> entities = new ArrayList<>();
    private final ArrayList<EntityFactory> entityFactories;

    @Visible
    @Editable
    private String name;

    @Visible
    @Editable
    private Color4f backgroundColor = new Color4f(0, 0, 0);

    @Override
    public void dispose() {
        sceneController.dispose();
    }

    public void create() {
        sceneController.onCreate();
    }

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

    public List<Entity> getEntities() {
        return entities;
    }

    public Color4f getBackgroundColor() {
        return backgroundColor;
    }

    public Scene setBackgroundColor(Color4f backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public String getName() {
        return name;
    }

    public Scene setName(String name) {
        this.name = name;
        return this;
    }

    public ArrayList<EntityFactory> getEntityFactories() {
        return entityFactories;
    }
}
