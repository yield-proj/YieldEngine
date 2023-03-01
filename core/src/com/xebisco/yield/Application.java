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

import java.util.ConcurrentModificationException;
import java.util.concurrent.CompletableFuture;

/**
 * It's a class that implements the `Behavior` interface and is responsible for rendering the scene and updating the
 * entities
 */
public class Application implements Behavior {
    private int frames;
    private final PlatformGraphics platformGraphics;
    private Scene scene;
    private final PlatformInit platformInit;
    private final Object renderLock = new Object();
    private final FontLoader fontLoader;
    private final DrawInstruction drawInstruction = new DrawInstruction();

    public Application(Scene initialScene, PlatformGraphics platformGraphics, PlatformInit platformInit) {
        this.platformGraphics = platformGraphics;
        if (platformGraphics instanceof FontLoader)
            fontLoader = (FontLoader) platformGraphics;
        else fontLoader = null;
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
                try {
                    for (Entity2D entity : scene.getEntities()) {
                        platformGraphics.resetRotation();
                        entity.render(platformGraphics);
                    }
                } catch (ConcurrentModificationException ignore) {

                }
                platformGraphics.conclude();
            }).exceptionally(exp -> {
                exp.printStackTrace();
                return null;
            });
            scene.setFrames(scene.getFrames() + 1);
            if (scene.getFrames() == 1) {
                scene.onStart();
            }
            scene.onUpdate();
            try {
                for (Entity2D entity : scene.getEntities()) {
                    entity.setFontLoader(fontLoader);
                    entity.process();
                }
            } catch (ConcurrentModificationException ignore) {

            }
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

    /**
     * This function returns the number of frames already passed in the application.
     *
     * @return The number of frames in the application.
     */
    public int getFrames() {
        return frames;
    }

    /**
     * This function sets the number of frames in the application.
     *
     * @param frames The number of frames in the application.
     */
    public void setFrames(int frames) {
        this.frames = frames;
    }

    /**
     * Returns the platform graphics object for this application.
     *
     * @return The platformGraphics object.
     */
    public PlatformGraphics getPlatformGraphics() {
        return platformGraphics;
    }

    /**
     * This function returns the scene.
     *
     * @return The scene.
     */
    public Scene getScene() {
        return scene;
    }

    /**
     * This function sets the scene to the scene that is passed in.
     *
     * @param scene The scene to be set.
     */
    public void setScene(Scene scene) {
        this.scene = scene;
    }
}
