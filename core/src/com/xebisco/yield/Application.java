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

import com.github.strikerx3.jxinput.XInputAxes;
import com.github.strikerx3.jxinput.XInputButtons;
import com.github.strikerx3.jxinput.XInputComponents;
import com.github.strikerx3.jxinput.XInputDevice;
import com.github.strikerx3.jxinput.exceptions.XInputNotLoadedException;

import java.lang.reflect.InvocationTargetException;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * It's a class that implements the `Behavior` interface and is responsible for rendering the scene and updating the
 * entities
 */
public class Application implements Behavior {
    private final PlatformGraphics platformGraphics;
    private final PlatformInit platformInit;
    private final Object renderLock = new Object();
    private final FontLoader fontLoader;
    private final TextureLoader textureLoader;
    private final InputManager inputManager;
    private final DrawInstruction drawInstruction = new DrawInstruction();
    private final Set<Axis> axes = new HashSet<>();
    private final ApplicationManager applicationManager;
    private final Runnable renderer;
    private final Function<Throwable, Void> exceptionThrowFunction = throwable -> {
        throwable.printStackTrace();
        return null;
    };
    private final XInputDevice[] gamePads;
    private int frames;
    private Scene scene;
    private double physicsPpm = 16;

    public Application(ApplicationManager applicationManager, Class<? extends Scene> initialScene, PlatformGraphics platformGraphics, PlatformInit platformInit) {
        this.applicationManager = applicationManager;
        this.platformGraphics = platformGraphics;
        if (platformGraphics instanceof FontLoader)
            fontLoader = (FontLoader) platformGraphics;
        else fontLoader = null;
        if (platformGraphics instanceof TextureLoader)
            textureLoader = (TextureLoader) platformGraphics;
        else textureLoader = null;
        if (platformGraphics instanceof InputManager)
            inputManager = (InputManager) platformGraphics;
        else inputManager = null;
        try {
            scene = initialScene.getConstructor(Application.class).newInstance(this);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        this.platformInit = platformInit;
        renderer = () -> {
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
        };
        axes.add(new Axis(Global.HORIZONTAL, Input.Key.VK_D, Input.Key.VK_A, Input.Key.VK_RIGHT, Input.Key.VK_LEFT));
        axes.add(new Axis(Global.VERTICAL, Input.Key.VK_W, Input.Key.VK_S, Input.Key.VK_UP, Input.Key.VK_DOWN));
        if (XInputDevice.isAvailable()) {
            try {
                gamePads = XInputDevice.getAllDevices();
            } catch (XInputNotLoadedException e) {
                throw new RuntimeException(e);
            }

        } else {
            gamePads = null;
            System.err.println("WARNING: XInput is not available!");
        }
    }

    @Override
    public void onStart() {
        if (Global.getDefaultFont() == null)
            Global.setDefaultFont(new Font("OpenSans-Regular.ttf", 48, fontLoader));
        if (Global.getDefaultTexture() == null)
            Global.setDefaultTexture(new Texture("yieldIcon.png", textureLoader));
        if (platformInit.getWindowIcon() == null)
            platformInit.setWindowIcon(Global.getDefaultTexture());
        platformGraphics.init(platformInit);
        for (XInputDevice device : gamePads) {
            if (device.getPlayerNum() > 0) {
                axes.add(new Axis(Global.HORIZONTAL + (device.getPlayerNum() + 1), null, null, null, null));
                axes.add(new Axis(Global.VERTICAL + (device.getPlayerNum() + 1), null, null, null, null));
            }
            axes.add(new Axis("CamHorizontal" + (device.getPlayerNum() + 1), null, null, null, null));
            axes.add(new Axis("CamVertical" + (device.getPlayerNum() + 1), null, null, null, null));
        }
    }

    public void updateAxes() {
        for (Axis axis : axes) {
            if ((axis.getPositiveKey() != null && inputManager.getPressingKeys().contains(axis.getPositiveKey())) || (axis.getAltPositiveKey() != null && inputManager.getPressingKeys().contains(axis.getAltPositiveKey()))) {
                axis.setValue(1);
            } else if ((axis.getNegativeKey() != null && inputManager.getPressingKeys().contains(axis.getNegativeKey())) || (axis.getAltNegativeKey() != null && inputManager.getPressingKeys().contains(axis.getAltNegativeKey()))) {
                axis.setValue(-1);
            } else {
                axis.setValue(0);
            }
        }
        for (XInputDevice device : gamePads) {
            device.poll();
            if (device.isConnected()) {
                XInputComponents components = device.getComponents();
                XInputButtons buttons = components.getButtons();
                XInputAxes axes = components.getAxes();
                String a = String.valueOf((device.getPlayerNum() + 1));
                if (device.getPlayerNum() == 0)
                    a = "";
                for (Axis axis : this.axes) {
                    if (axis.getName().equals(Global.HORIZONTAL + a)) {
                        axis.setValue(axes.lx);
                        if (Math.abs(axis.getValue()) < 0.1)
                            axis.setValue(0);
                    } else if (axis.getName().equals(Global.VERTICAL + a)) {
                        axis.setValue(axes.ly);
                        if (Math.abs(axis.getValue()) < 0.1)
                            axis.setValue(0);
                    } else if (axis.getName().equals("CamHorizontal" + a)) {
                        axis.setValue(axes.rx);
                        if (Math.abs(axis.getValue()) < 0.1)
                            axis.setValue(0);
                    } else if (axis.getName().equals("CamVertical" + a)) {
                        axis.setValue(axes.ry);
                        if (Math.abs(axis.getValue()) < 0.1)
                            axis.setValue(0);
                    }
                }
            }
        }
    }

    @Override
    public void onUpdate() {
        if (scene != null) {
            CompletableFuture<Void> renderAsync = CompletableFuture.runAsync(renderer).exceptionally(exceptionThrowFunction);
            scene.setFrames(scene.getFrames() + 1);
            if (scene.getFrames() == 1) {
                scene.onStart();
            }
            try {
                for (SystemBehavior system : scene.getSystems()) {
                    system.setScene(scene);
                    system.setFrames(system.getFrames() + 1);
                    if (system.getFrames() == 1) {
                        system.onStart();
                    }
                    system.onUpdate();
                }
            } catch (ConcurrentModificationException ignore) {

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
            if (inputManager != null) {
                updateAxes();
                inputManager.getPressingMouseButtons().remove(Input.MouseButton.SCROLL_UP);
                inputManager.getPressingMouseButtons().remove(Input.MouseButton.SCROLL_DOWN);
            }
        }
    }

    @Override
    public void dispose() {
        setScene(null);
        platformGraphics.dispose();
    }

    public double getAxis(String name) {
        for (Axis axis : axes) {
            if (axis.getName().hashCode() == name.hashCode() && axis.getName().equals(name))
                return axis.getValue();
        }
        throw new IllegalArgumentException("none axis with the name: '" + name + "'");
    }

    public TwoAnchorRepresentation getAxis(String axisX, String axisY) {
        return new TwoAnchorRepresentation(getAxis(axisX), getAxis(axisY));
    }

    public Set<Axis> getAxes() {
        return axes;
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
        if (this.scene != null) {
            for (SystemBehavior system : this.scene.getSystems()) {
                system.dispose();
            }
            this.scene.dispose();
        }
        this.scene = scene;
    }

    public PlatformInit getPlatformInit() {
        return platformInit;
    }

    public Object getRenderLock() {
        return renderLock;
    }

    public FontLoader getFontLoader() {
        return fontLoader;
    }

    public TextureLoader getTextureLoader() {
        return textureLoader;
    }

    public InputManager getInputManager() {
        return inputManager;
    }

    public DrawInstruction getDrawInstruction() {
        return drawInstruction;
    }

    public Runnable getRenderer() {
        return renderer;
    }

    public Function<Throwable, Void> getExceptionThrowFunction() {
        return exceptionThrowFunction;
    }

    public ApplicationManager getApplicationManager() {
        return applicationManager;
    }

    public double getPhysicsPpm() {
        return physicsPpm;
    }

    public void setPhysicsPpm(double physicsPpm) {
        this.physicsPpm = physicsPpm;
    }
}
