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
import com.xebisco.yieldengine.core.input.Input;
import com.xebisco.yieldengine.core.io.IO;
import com.xebisco.yieldengine.core.render.DrawInstruction;
import com.xebisco.yieldengine.core.render.Render;
import org.joml.Vector3f;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public final class Scene implements IDispose {
    public final class SceneController extends OnSceneBehavior {
        @Override
        public void onCreate() {
            Logger.getInstance().engineDebug("Creating " + this + ".");
            entities.forEach(Entity::onCreate);
            Logger.getInstance().engineDebug(this + " created.");
        }

        @Override
        public void onStart() {
            Logger.getInstance().engineDebug("Starting " + this + ".");
            entities.forEach(Entity::onStart);
            Logger.getInstance().engineDebug(this + " started.");
        }

        @Override
        public void onUpdate() {
            Input input = Input.getInstance();
            if (input != null) {
                input.updateKeyList();
                input.updateMouseButtonList();
            }

            Render render = Render.getInstance();

            LockProcess renderLock = null;

            if (render != null) {
                renderLock = new LockProcess();
                Render.getInstance().sendRender(renderLock);
                Render.getInstance().clearInstructions();
                Render.getInstance().getInstructionsList().add(
                        new DrawInstruction()
                                .setType(DrawInstruction.DrawInstructionType.CLEAR_SCREEN.getTypeString())
                                .setDrawObjects(new Serializable[]{backgroundColor})
                );
            }
            entities.forEach(Entity::onUpdate);
            if (renderLock != null) {
                try {
                    renderLock.aWait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
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

    private final List<Entity> entities = new ArrayList<>();

    private Vector3f backgroundColor = new Vector3f();

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

    public Vector3f getBackgroundColor() {
        return backgroundColor;
    }

    public Scene setBackgroundColor(Vector3f backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }
}
