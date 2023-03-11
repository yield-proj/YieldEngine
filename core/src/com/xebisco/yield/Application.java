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

import com.studiohartman.jamepad.ControllerManager;
import com.studiohartman.jamepad.ControllerState;
import com.studiohartman.jamepad.ControllerUnpluggedException;

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
    private final ControllerManager controllerManager;
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
            setScene(initialScene.getConstructor(Application.class).newInstance(this));
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
            drawInstruction.setPosition(platformGraphics.getCamera());
            drawInstruction.setSize(platformInit.getResolution());
            platformGraphics.draw(drawInstruction);
            try {
                for (Entity2D entity : scene.getEntities()) {
                    entity.render(platformGraphics);
                }
            } catch (ConcurrentModificationException ignore) {

            }
            platformGraphics.conclude();
        };
        axes.add(new Axis(Global.HORIZONTAL, Input.Key.VK_D, Input.Key.VK_A, Input.Key.VK_RIGHT, Input.Key.VK_LEFT));
        axes.add(new Axis(Global.VERTICAL, Input.Key.VK_W, Input.Key.VK_S, Input.Key.VK_UP, Input.Key.VK_DOWN));
        axes.add(new Axis("Fire", Input.Key.VK_SPACE, null, null, null));
        axes.add(new Axis("Back", Input.Key.VK_BACK_SPACE, null, null, null));
        axes.add(new Axis("Action", Input.Key.VK_E, null, null, null));
        axes.add(new Axis("Inventory", Input.Key.VK_TAB, null, null, null));
        axes.add(new Axis("Start",  Input.Key.VK_ESCAPE, null, null, null));
        axes.add(new Axis("RightFire", Input.Key.VK_1, null, null, null));
        axes.add(new Axis("LeftFire", Input.Key.VK_3, null, null, null));
        controllerManager = new ControllerManager(4);
        controllerManager.initSDLGamepad();
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
        for (int i = 0; i < 4; i++) {
            String a = String.valueOf((i + 1));
            if (i == 0)
                a = "";
            if (i > 0) {
                axes.add(new Axis(Global.HORIZONTAL + a, null, null, null, null));
                axes.add(new Axis(Global.VERTICAL + a, null, null, null, null));
                axes.add(new Axis("Fire" + a, null, null, null, null));
                axes.add(new Axis("Back" + a, null, null, null, null));
                axes.add(new Axis("Action" + a, null, null, null, null));
                axes.add(new Axis("Inventory" + a, null, null, null, null));
                axes.add(new Axis("Start" + a, null, null, null, null));
                axes.add(new Axis("RightFire" + a, null, null, null, null));
                axes.add(new Axis("LeftFire" + a, null, null, null, null));
            }
            axes.add(new Axis("HorizontalCam" + a, null, null, null, null));
            axes.add(new Axis("VerticalCam" + a, null, null, null, null));
            axes.add(new Axis("HorizontalPad" + a, null, null, null, null));
            axes.add(new Axis("VerticalPad" + a, null, null, null, null));
            axes.add(new Axis("RightBumper" + a, null, null, null, null));
            axes.add(new Axis("LeftBumper" + a, null, null, null, null));
            axes.add(new Axis("RightThumb" + a, null, null, null, null));
            axes.add(new Axis("LeftThumb" + a, null, null, null, null));
            axes.add(new Axis("View" + a, null, null, null, null));
        }
    }


    public void vibrateController(int playerIndex, double leftMagnitude, double rightMagnitude, double duration) {
        try {
            controllerManager.getControllerIndex(playerIndex).doVibration((float) leftMagnitude, (float) rightMagnitude, (int) duration * 1000);
        } catch (ControllerUnpluggedException e) {
            throw new RuntimeException(e);
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
        controllerManager.update();
        for (int i = 0; i < controllerManager.getNumControllers(); i++) {
            ControllerState device = controllerManager.getState(i);
            if (device.isConnected) {
                String a = String.valueOf(i + 1);
                if (i == 0)
                    a = "";
                for (Axis axis : this.axes) {
                    if (axis.getName().equals(Global.HORIZONTAL + a)) {
                        axis.setValue(device.leftStickX);
                        if (Math.abs(axis.getValue()) < 0.1)
                            axis.setValue(0);
                    } else if (axis.getName().equals(Global.VERTICAL + a)) {
                        axis.setValue(device.leftStickY);
                        if (Math.abs(axis.getValue()) < 0.1)
                            axis.setValue(0);
                    } else if (axis.getName().equals("HorizontalCam" + a)) {
                        axis.setValue(device.rightStickX);
                        if (Math.abs(axis.getValue()) < 0.1)
                            axis.setValue(0);
                    } else if (axis.getName().equals("VerticalCam" + a)) {
                        axis.setValue(device.rightStickY);
                        if (Math.abs(axis.getValue()) < 0.1)
                            axis.setValue(0);
                    } else if (axis.getName().equals("RightFire" + a)) {
                        axis.setValue(device.rightTrigger);
                    } else if (axis.getName().equals("LeftFire" + a)) {
                        axis.setValue(device.leftTrigger);
                    } else if (axis.getName().equals("HorizontalPad" + a)) {
                        if (device.dpadRight) axis.setValue(1);
                        else if (device.dpadLeft) axis.setValue(-1);
                        else axis.setValue(0);
                    } else if (axis.getName().equals("VerticalPad" + a)) {
                        if (device.dpadUp) axis.setValue(1);
                        else if (device.dpadDown) axis.setValue(-1);
                        else axis.setValue(0);
                    } else if (axis.getName().equals("Fire" + a)) {
                        if (device.a) axis.setValue(1);
                        else axis.setValue(0);
                    } else if (axis.getName().equals("Action" + a)) {
                        if (device.x) axis.setValue(1);
                        else axis.setValue(0);
                    } else if (axis.getName().equals("Back" + a)) {
                        if (device.b) axis.setValue(1);
                        else axis.setValue(0);
                    } else if (axis.getName().equals("Inventory" + a)) {
                        if (device.y) axis.setValue(1);
                        else axis.setValue(0);
                    } else if (axis.getName().equals("RightBumper" + a)) {
                        if (device.rb) axis.setValue(1);
                        else axis.setValue(0);
                    } else if (axis.getName().equals("LeftBumper" + a)) {
                        if (device.lb) axis.setValue(1);
                        else axis.setValue(0);
                    } else if (axis.getName().equals("RightThumb" + a)) {
                        if (device.rightStickClick) axis.setValue(1);
                        else axis.setValue(0);
                    } else if (axis.getName().equals("LeftThumb" + a)) {
                        if (device.leftStickClick) axis.setValue(1);
                        else axis.setValue(0);
                    } else if (axis.getName().equals("Start" + a)) {
                        if (device.start) axis.setValue(1);
                        else axis.setValue(0);
                    } else if (axis.getName().equals("View" + a)) {
                        if (device.back) axis.setValue(1);
                        else axis.setValue(0);
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
        controllerManager.quitSDLGamepad();
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
            System.out.println(platformGraphics.getCamera());
            for (SystemBehavior system : this.scene.getSystems()) {
                system.dispose();
            }
            this.scene.dispose();
        }
        this.scene = scene;
        platformGraphics.setCamera(scene.getCamera());
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
