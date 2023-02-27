/*
 * Copyright [2022-2023] [Xebisco]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.xebisco.yield;

import java.util.concurrent.CompletableFuture;

public class Application implements Behavior {
    private int frames;
    private final PlatformGraphics platformGraphics;
    private Scene scene;
    private final PlatformInit platformInit;
    private final Object renderLock = new Object();
    private final DrawInstruction drawInstruction = new DrawInstruction();

    public Application(Scene initialScene, PlatformGraphics platformGraphics, PlatformInit platformInit) {
        this.platformGraphics = platformGraphics;
        scene = initialScene;
        this.platformInit = platformInit;
    }

    @Override
    public void onStart() {
        platformGraphics.init(platformInit);
    }

    @Override
    public void onUpdate() {
        if (scene != null) {
            CompletableFuture<Void> renderAsync = CompletableFuture.runAsync(() -> {
                platformGraphics.frame();
                platformGraphics.resetRotation();
                drawInstruction.setBorderColor(null);
                drawInstruction.setFilled(true);
                drawInstruction.setInnerColor(scene.getBackGroundColor());
                drawInstruction.setType(DrawInstruction.Type.RECTANGLE);
                drawInstruction.setPosition(new Point2D(0, 0));
                drawInstruction.setSize(platformInit.getResolution());
                platformGraphics.draw(drawInstruction);
                platformGraphics.conclude();
            });
            scene.setFrames(scene.getFrames() + 1);
            if (scene.getFrames() == 1) {
                scene.onStart();
            }
            scene.onUpdate();
            while (!renderAsync.isDone()) {
                synchronized (renderLock) {
                    try {
                        renderLock.wait(1);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    @Override
    public void dispose() {
        scene = null;
        platformGraphics.dispose();
    }

    public int getFrames() {
        return frames;
    }

    public void setFrames(int frames) {
        this.frames = frames;
    }

    public PlatformGraphics getPlatformGraphics() {
        return platformGraphics;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }
}
