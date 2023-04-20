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

import com.studiohartman.jamepad.ControllerManager;
import com.studiohartman.jamepad.ControllerState;
import com.studiohartman.jamepad.ControllerUnpluggedException;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * It's a class that implements the `Behavior` interface and is responsible for rendering the scene and updating the
 * entities
 */
public class Application implements Behavior {
    public final Vector2D mousePosition = new Vector2D();
    private final PlatformGraphics platformGraphics;
    private final PlatformInit platformInit;
    private final FontLoader fontLoader;
    private final TextureManager textureManager;
    private final InputManager inputManager;
    private final DrawInstruction drawInstruction = new DrawInstruction();
    private final Set<Axis> axes = new HashSet<>();
    private final ApplicationManager applicationManager;
    private final Size2D resolution;
    private final Runnable renderer;
    private final AudioManager audioManager;
    private final Texture controllerTexture;
    private final Texture translucentControllerTexture;
    private final ViewportZoomScale viewportZoomScale;
    private final Function<Throwable, Void> exceptionThrowFunction = throwable -> {
        throwable.printStackTrace();
        return null;
    };
    private final ControllerManager controllerManager;
    private int frames;
    private Scene scene;
    private double physicsPpm = 16;
    private Font defaultFont;
    private Texture defaultTexture;

    public Application(ApplicationManager applicationManager, Class<? extends Scene> initialScene, PlatformGraphics platformGraphics, PlatformInit platformInit) {
        this.applicationManager = applicationManager;
        applicationManager.getApplications().add(this);
        this.platformGraphics = platformGraphics;
        if (platformGraphics instanceof FontLoader)
            fontLoader = (FontLoader) platformGraphics;
        else fontLoader = null;
        if (platformGraphics instanceof TextureManager)
            textureManager = (TextureManager) platformGraphics;
        else textureManager = null;
        if (platformGraphics instanceof InputManager)
            inputManager = (InputManager) platformGraphics;
        else inputManager = null;
        if (platformGraphics instanceof AudioManager)
            audioManager = (AudioManager) platformGraphics;
        else audioManager = null;
        if (platformGraphics instanceof ViewportZoomScale)
            viewportZoomScale = (ViewportZoomScale) platformGraphics;
        else viewportZoomScale = null;

        setScene(new BlankScene(this));
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    setScene(initialScene.getConstructor(Application.class).newInstance(Application.this));
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                         NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
                t.cancel();
            }
        }, 2000L);

        this.platformInit = platformInit;
        renderer = () -> {
            platformGraphics.frame();
            platformGraphics.resetRotation();
            drawInstruction.setBorderColor(null);
            drawInstruction.setFilled(true);
            drawInstruction.setInnerColor(scene.getBackGroundColor());
            drawInstruction.setType(DrawInstruction.Type.RECTANGLE);
            drawInstruction.setPosition(platformGraphics.getCamera());
            drawInstruction.setSize(platformInit.getGameResolution());
            platformGraphics.draw(drawInstruction);
            try {
                for (int i = 0; i < scene.getEntities().size(); i++) {
                    Entity2D e = null;
                    try {
                        e = scene.getEntities().get(i);
                    } catch (IndexOutOfBoundsException ignore) {

                    }
                    if (e != null)
                        e.render(platformGraphics);
                }
            } catch (ConcurrentModificationException ignore) {

            }
            platformGraphics.conclude();
        };
        resolution = new ImmutableSize2D(platformInit.getGameResolution().getWidth(), platformInit.getGameResolution().getHeight());
        axes.add(new Axis(Global.HORIZONTAL, Input.Key.VK_D, Input.Key.VK_A, Input.Key.VK_RIGHT, Input.Key.VK_LEFT));
        axes.add(new Axis(Global.VERTICAL, Input.Key.VK_W, Input.Key.VK_S, Input.Key.VK_UP, Input.Key.VK_DOWN));
        axes.add(new Axis("HorizontalPad", Input.Key.VK_D, Input.Key.VK_A, Input.Key.VK_RIGHT, Input.Key.VK_LEFT));
        axes.add(new Axis("VerticalPad", Input.Key.VK_W, Input.Key.VK_S, Input.Key.VK_UP, Input.Key.VK_DOWN));
        axes.add(new Axis("Fire", Input.Key.VK_SPACE, null, null, null));
        axes.add(new Axis("Back", Input.Key.VK_BACK_SPACE, null, null, null));
        axes.add(new Axis("Action", Input.Key.VK_E, null, null, null));
        axes.add(new Axis("Inventory", Input.Key.VK_TAB, null, null, null));
        axes.add(new Axis("Start", Input.Key.VK_ESCAPE, null, null, null));
        axes.add(new Axis("RightFire", Input.Key.VK_3, null, null, null));
        axes.add(new Axis("LeftFire", Input.Key.VK_1, null, null, null));
        axes.add(new Axis("RightBumper", Input.Key.VK_F, null, null, null));
        axes.add(new Axis("Run", Input.Key.VK_SHIFT, null, null, null));
        axes.add(new Axis("LeftBumper", Input.Key.VK_G, null, null, null));
        if (textureManager != null) {
            controllerTexture = new Texture("controller.png", textureManager);
            translucentControllerTexture = new Texture("controller.png", textureManager);
            translucentControllerTexture.process(pixel -> {
                pixel.getColor().setAlpha(pixel.getColor().getAlpha() - .6);
                return pixel;
            });
        } else {
            controllerTexture = null;
            translucentControllerTexture = null;
        }
        controllerManager = new ControllerManager(4);
        controllerManager.initSDLGamepad();
    }

    @Override
    public void onStart() {
        defaultFont = new Font("OpenSans-Regular.ttf", 48, fontLoader);
        if (platformInit.getWindowIcon() == null)
            platformInit.setWindowIcon(new Texture(platformInit.getWindowIconPath(), getTextureManager()));
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
                axes.add(new Axis("HorizontalPad" + a, null, null, null, null));
                axes.add(new Axis("VerticalPad" + a, null, null, null, null));
                axes.add(new Axis("RightBumper" + a, null, null, null, null));
                axes.add(new Axis("LeftBumper" + a, null, null, null, null));
                axes.add(new Axis("Run" + a, null, null, null, null));
            }
            axes.add(new Axis("HorizontalCam" + a, null, null, null, null));
            axes.add(new Axis("VerticalCam" + a, null, null, null, null));
            axes.add(new Axis("RightThumb" + a, null, null, null, null));
            axes.add(new Axis("LeftThumb" + a, null, null, null, null));
            axes.add(new Axis("View" + a, null, null, null, null));
        }
        if(textureManager != null) {
            defaultTexture = new Texture("yieldIcon.png", textureManager);
        }
    }

    /**
     * This Java function vibrates a game controller for a specified player with left and right magnitudes and a duration.
     *
     * @param playerIndex    The index of the player's controller. It is used to identify which controller to vibrate.
     * @param leftMagnitude  The intensity of the vibration on the left side of the controller. It is a double value between
     *                       0.0 and 1.0, where 0.0 means no vibration and 1.0 means maximum vibration intensity.
     * @param rightMagnitude The rightMagnitude parameter in the vibrateController method represents the intensity of the
     *                       vibration on the right side of the controller. It is a double value that ranges from 0.0 (no vibration) to 1.0
     *                       (maximum vibration).
     * @param duration       The duration parameter is the length of time in seconds that the controller will vibrate for.
     */
    public void vibrateController(int playerIndex, double leftMagnitude, double rightMagnitude, double duration) {
        try {
            controllerManager.getControllerIndex(playerIndex).doVibration((float) leftMagnitude, (float) rightMagnitude, (int) duration * 1000);
        } catch (ControllerUnpluggedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This function updates the values of various input axes based on keyboard and controller inputs.
     */
    public void updateAxes() {
        mousePosition.set(getInputManager().getMouseX(), getInputManager().getMouseY());
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
                    } else if (axis.getName().equals("Run" + a)) {
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
        if (scene != null && !(scene instanceof BlankScene)) {
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
            scene.getEntities().sort(Comparator.comparing(Entity2D::getIndex));
            if (inputManager != null) {
                updateAxes();
                inputManager.getPressingMouseButtons().remove(Input.MouseButton.SCROLL_UP);
                inputManager.getPressingMouseButtons().remove(Input.MouseButton.SCROLL_DOWN);
            }
            renderer.run();
        }
    }

    @Override
    public void dispose() {
        setScene(null);
        controllerManager.quitSDLGamepad();
        platformGraphics.dispose();
    }

    /**
     * This Java function returns the value of an axis with a given name, or throws an exception if no axis with that name
     * exists.
     *
     * @param name The parameter "name" is a String representing the name of the axis that the method is trying to retrieve
     *             the value for.
     * @return The method is returning a double value which represents the value of the axis with the given name.
     */
    public double getAxis(String name) {
        for (Axis axis : axes) {
            if (axis.getName().hashCode() == name.hashCode() && axis.getName().equals(name))
                return axis.getValue();
        }
        throw new IllegalArgumentException("none axis with the name: '" + name + "'");
    }

    /**
     * This function returns a TwoAnchorRepresentation object with the X and Y axis obtained from calling the getAxis
     * method.
     *
     * @param axisX It is a String parameter representing the name or label of the X-axis.
     * @param axisY It is a String parameter representing the name or label of the Y-axis.
     * @return A new instance of the `TwoAnchorRepresentation` class, which is constructed using the `getAxis` method with
     * the `axisX` and `axisY` parameters. The `getAxis` method is called twice, once with `axisX` and once with `axisY`,
     * and the results are used to create the `TwoAnchorRepresentation` object.
     */
    public TwoAnchorRepresentation getAxis(String axisX, String axisY) {
        return new TwoAnchorRepresentation(getAxis(axisX), getAxis(axisY));
    }

    /**
     * The function returns a set of Axis objects.
     *
     * @return A Set of Axis objects is being returned.
     */
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
     * The function returns the texture manager object.
     *
     * @return The method is returning this application texture manager.
     */
    public TextureManager getTextureManager() {
        return textureManager;
    }

    /**
     * The function returns the resolution as a Size2D object.
     *
     * @return The method is returning an object of type `Size2D`, which represents the resolution of this application.
     */
    public Size2D getResolution() {
        return resolution;
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
            for (int i = this.scene.getEntities().size() - 1; i >= 0; i--) {
                this.scene.getEntities().get(i).dispose();
            }
            for (SystemBehavior system : this.scene.getSystems()) {
                system.dispose();
            }
            this.scene.dispose();
        }
        this.scene = scene;
        if (viewportZoomScale != null)
            viewportZoomScale.getZoomScale().set(1, 1);
        if (scene != null) {
            scene.setFrames(0);
            platformGraphics.setCamera(scene.getCamera());
        }
    }

    /**
     * This function checks if a specific key is being pressed by the user.
     *
     * @param key The parameter "key" is of type Input.Key, which is an enum that represents a specific key on a standard
     *            keyboard. This method checks if the key is currently being pressed down by the user.
     * @return The method is returning a boolean value which indicates whether the specified key is being pressed or not.
     * It checks if the list of currently pressing keys obtained from the input manager contains the specified key, and
     * returns true if it does, false otherwise.
     */
    public boolean isPressingKey(Input.Key key) {
        return getInputManager().getPressingKeys().contains(key);
    }

    /**
     * This function checks if a specific mouse button is currently being pressed.
     *
     * @param button The parameter "button" is of type Input.MouseButton, which is likely an enum that represents the
     *               different mouse buttons. This method checks if the specified mouse button is currently
     *               being pressed down by the user.
     * @return The method `isPressingButton` returns a boolean value indicating whether the specified `MouseButton` is
     * currently being pressed or not. It returns `true` if the button is being pressed and `false` otherwise.
     */
    public boolean isPressingButton(Input.MouseButton button) {
        return getInputManager().getPressingMouseButtons().contains(button);
    }

    /**
     * This function returns a ViewportZoomScale object.
     *
     * @return The method is returning an object of type `ViewportZoomScale`.
     */
    public ViewportZoomScale getViewportZoomScale() {
        return viewportZoomScale;
    }

    /**
     * The function returns the platform initialization object.
     *
     * @return The method is returning an object of type `PlatformInit`.
     */
    public PlatformInit getPlatformInit() {
        return platformInit;
    }

    /**
     * The function returns a FontLoader object.
     *
     * @return The `getFontLoader()` method is returning an object of type `FontLoader`.
     */
    public FontLoader getFontLoader() {
        return fontLoader;
    }

    /**
     * The function returns an instance of the InputManager class.
     *
     * @return The method is returning an object of type `InputManager`.
     */
    public InputManager getInputManager() {
        return inputManager;
    }

    /**
     * The function returns a DrawInstruction object.
     *
     * @return The method is returning an object of type `DrawInstruction`.
     */
    public DrawInstruction getDrawInstruction() {
        return drawInstruction;
    }

    /**
     * The function returns a Runnable object named "renderer".
     *
     * @return A Runnable object named "renderer" is being returned.
     */
    public Runnable getRenderer() {
        return renderer;
    }

    /**
     * The function returns a Function object that takes a Throwable as input and returns Void.
     *
     * @return A `Function` object that takes a `Throwable` as input and returns `Void` as output. The
     * `exceptionThrowFunction` variable is being returned.
     */
    public Function<Throwable, Void> getExceptionThrowFunction() {
        return exceptionThrowFunction;
    }

    /**
     * This function returns an instance of the ApplicationManager class.
     *
     * @return The method is returning an object of type `ApplicationManager`.
     */
    public ApplicationManager getApplicationManager() {
        return applicationManager;
    }

    /**
     * The function returns the value of the variable physicsPpm as a double.
     *
     * @return The method is returning a double value which is the value of the variable `physicsPpm`.
     */
    public double getPhysicsPpm() {
        return physicsPpm;
    }

    /**
     * This function sets the value of the physicsPpm variable.
     *
     * @param physicsPpm physicsPpm stands for "physics pixels per meter". It is a measure of the scale used in a physics
     *                   simulation, where the size of objects and distances between them are represented in pixels and meters. By setting
     *                   the physicsPpm value, you can adjust the scale of the physics simulation.
     */
    public void setPhysicsPpm(double physicsPpm) {
        this.physicsPpm = physicsPpm;
    }

    /**
     * This function returns an instance of the AudioManager class in Yield.
     *
     * @return The method is returning an instance of the AudioManager class.
     */
    public AudioManager getAudioManager() {
        return audioManager;
    }

    /**
     * The function returns the controller manager object.
     *
     * @return The method is returning an object of type `ControllerManager`.
     */
    public ControllerManager getControllerManager() {
        return controllerManager;
    }

    /**
     * The function returns a texture object representing the controller texture.
     *
     * @return The method is returning a Texture object named "controllerTexture".
     */
    public Texture getControllerTexture() {
        return controllerTexture;
    }

    /**
     * This function returns the default font.
     *
     * @return The method is returning a Font object named "defaultFont".
     */
    public Font getDefaultFont() {
        return defaultFont;
    }

    /**
     * This function sets the default font for a Java program.
     *
     * @param defaultFont The parameter `defaultFont` is of type `Font`, which represents a font that can be used to render
     *                    text. This method sets the default font to be used in the application.
     */
    public void setDefaultFont(Font defaultFont) {
        this.defaultFont = defaultFont;
    }

    /**
     * The function returns the default texture.
     *
     * @return The method is returning a Texture object named "defaultTexture".
     */
    public Texture getDefaultTexture() {
        return defaultTexture;
    }

    /**
     * This function sets the default texture for an object.
     *
     * @param defaultTexture The parameter `defaultTexture` is of type `Texture` and represents the default texture that
     *                       will be set for an object. This method allows the user to set a default texture that will be used if no other texture is specified.
     */
    public void setDefaultTexture(Texture defaultTexture) {
        this.defaultTexture = defaultTexture;
    }

    /**
     * This function returns a Texture object representing a translucent controller texture.
     *
     * @return The method is returning a Texture object named "translucentControllerTexture".
     */
    public Texture getTranslucentControllerTexture() {
        return translucentControllerTexture;
    }
}
