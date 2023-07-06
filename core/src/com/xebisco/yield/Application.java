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
import java.util.function.Function;

/**
 * It's a class that implements the `Behavior` interface and is responsible for rendering the scene and updating the
 * entities
 */
public class Application implements Behavior {
    public final Vector2D mousePosition = new Vector2D();
    private final ApplicationPlatform applicationPlatform;
    private final PlatformInit platformInit;
    private final DrawInstruction backGroundDrawInstruction = new DrawInstruction(null, null);
    private final Set<Axis> axes = new HashSet<>();
    private final ApplicationManager applicationManager;
    private final Size2D viewportSize;
    private final RenderingThread renderingThread;
    private Texture controllerTexture;
    private Texture translucentControllerTexture;
    private final Function<Throwable, Void> exceptionThrowFunction = throwable -> {
        throwable.printStackTrace();
        return null;
    };
    private ControllerManager controllerManager;
    private int frames;
    private Scene scene;
    private double physicsPpm;
    private Font defaultFont;
    private Texture defaultTexture;
    private ChangeSceneTransition changeSceneTransition;
    private final List<DrawInstruction> drawInstructions = new ArrayList<>(), toSendDrawInstructions = new ArrayList<>();
    private Scene toChangeScene;

    public Application(ApplicationManager applicationManager, Class<? extends Scene> initialScene, ApplicationPlatform applicationPlatform, PlatformInit platformInit) {
        physicsPpm = platformInit.getStartPhysicsPpm();
        this.applicationManager = applicationManager;
        applicationManager.getApplications().add(this);
        this.applicationPlatform = applicationPlatform;

        checkPlatform(applicationPlatform, platformInit);

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
        renderingThread = new RenderingThread(applicationPlatform.getGraphicsManager());
        viewportSize = new ImmutableSize2D(platformInit.getWindowSize().getWidth(), platformInit.getViewportSize().getHeight());
        axes.add(new Axis(HORIZONTAL, Input.Key.VK_D, Input.Key.VK_A, Input.Key.VK_RIGHT, Input.Key.VK_LEFT));
        axes.add(new Axis(VERTICAL, Input.Key.VK_W, Input.Key.VK_S, Input.Key.VK_UP, Input.Key.VK_DOWN));
        axes.add(new Axis(HORIZONTAL_PAD, Input.Key.VK_D, Input.Key.VK_A, Input.Key.VK_RIGHT, Input.Key.VK_LEFT));
        axes.add(new Axis(VERTICAL_PAD, Input.Key.VK_W, Input.Key.VK_S, Input.Key.VK_UP, Input.Key.VK_DOWN));
        axes.add(new Axis(FIRE, Input.Key.VK_SPACE, null, null, null));
        axes.add(new Axis(BACK, Input.Key.VK_BACK_SPACE, null, null, null));
        axes.add(new Axis(ACTION, Input.Key.VK_E, null, null, null));
        axes.add(new Axis(INVENTORY, Input.Key.VK_TAB, null, null, null));
        axes.add(new Axis(START, Input.Key.VK_ESCAPE, null, null, null));
        axes.add(new Axis(RIGHT_FIRE, Input.Key.VK_3, null, null, null));
        axes.add(new Axis(LEFT_FIRE, Input.Key.VK_1, null, null, null));
        axes.add(new Axis(RIGHT_BUMPER, Input.Key.VK_F, null, null, null));
        axes.add(new Axis(RUN, Input.Key.VK_SHIFT, null, null, null));
        axes.add(new Axis(LEFT_BUMPER, Input.Key.VK_G, null, null, null));
    }

    private void checkPlatform(ApplicationPlatform platform, PlatformInit init) {
        for (Class<?> c : init.getRequiredPlatformModules()) {
            if (c.equals(FontManager.class)) {
                if (platform.getFontManager() == null)
                    throw new ApplicationPlatformModuleException("The application platform does not contain the '" + c.getSimpleName() + "' module.");
            } else if (c.equals(TextureManager.class)) {
                if (platform.getTextureManager() == null)
                    throw new ApplicationPlatformModuleException("The application platform does not contain the '" + c.getSimpleName() + "' module.");
            } else if (c.equals(InputManager.class)) {
                if (platform.getInputManager() == null)
                    throw new ApplicationPlatformModuleException("The application platform does not contain the '" + c.getSimpleName() + "' module.");
            } else if (c.equals(KeyCheck.class)) {
                if (platform.getKeyCheck() == null)
                    throw new ApplicationPlatformModuleException("The application platform does not contain the '" + c.getSimpleName() + "' module.");
            } else if (c.equals(MouseCheck.class)) {
                if (platform.getMouseCheck() == null)
                    throw new ApplicationPlatformModuleException("The application platform does not contain the '" + c.getSimpleName() + "' module.");
            } else if (c.equals(AudioManager.class)) {
                if (platform.getAudioManager() == null)
                    throw new ApplicationPlatformModuleException("The application platform does not contain the '" + c.getSimpleName() + "' module.");
            } else if (c.equals(ViewportZoomScale.class)) {
                if (platform.getViewportZoomScale() == null)
                    throw new ApplicationPlatformModuleException("The application platform does not contain the '" + c.getSimpleName() + "' module.");
            } else if (c.equals(ToggleFullScreen.class)) {
                if (platform.getToggleFullScreen() == null)
                    throw new ApplicationPlatformModuleException("The application platform does not contain the '" + c.getSimpleName() + "' module.");
            } else if (c.equals(GraphicsManager.class)) {
                if (platform.getGraphicsManager() == null)
                    throw new ApplicationPlatformModuleException("The application platform does not contain the '" + c.getSimpleName() + "' module.");
            } else if (c.equals(SpritesheetTextureManager.class)) {
                if (platform.getSpritesheetTextureManager() == null)
                    throw new ApplicationPlatformModuleException("The application platform does not contain the '" + c.getSimpleName() + "' module.");
            } else {
                throw new ApplicationPlatformModuleException("Not supported application module. '" + c.getSimpleName() + "'");
            }
        }
    }

    @Override
    public void onStart() {
        applicationPlatform.getGraphicsManager().init(platformInit);
        renderingThread.start();
        defaultFont = new Font("com/xebisco/yield/OpenSans-Regular.ttf", 48, applicationPlatform.getFontManager());
        if (platformInit.getWindowIcon() == null)
            platformInit.setWindowIcon(new Texture(platformInit.getWindowIconPath(), applicationPlatform.getTextureManager()));
        applicationPlatform.getGraphicsManager().updateWindowIcon(platformInit.getWindowIcon());
        for (int i = 0; i < 4; i++) {
            String a = String.valueOf((i + 1));
            if (i == 0)
                a = "";
            if (i > 0) {
                createNullAxis(a, HORIZONTAL, VERTICAL, FIRE, BACK, ACTION, INVENTORY, START);
                createNullAxis(a, RIGHT_FIRE, LEFT_FIRE, HORIZONTAL_PAD, VERTICAL_PAD, RIGHT_BUMPER, LEFT_BUMPER, RUN);
            }
            axes.add(new Axis(HORIZONTAL_CAM + a, null, null, null, null));
            axes.add(new Axis(VERTICAL_CAM + a, null, null, null, null));
            axes.add(new Axis(RIGHT_THUMB + a, null, null, null, null));
            axes.add(new Axis(LEFT_THUMB + a, null, null, null, null));
            axes.add(new Axis(VIEW + a, null, null, null, null));
        }
        if (applicationPlatform.getTextureManager() != null) {
            defaultTexture = new Texture("com/xebisco/yield/yieldIcon.png", applicationPlatform.getTextureManager());
            controllerTexture = new Texture("com/xebisco/yield/controller.png", applicationPlatform.getTextureManager());
            translucentControllerTexture = new Texture("com/xebisco/yield/translucentController.png", applicationPlatform.getTextureManager());
        } else {
            controllerTexture = null;
            translucentControllerTexture = null;
        }
        try {
            controllerManager = new ControllerManager(4);
        } catch (Exception | UnsatisfiedLinkError ignore) {
            controllerManager = null;
            System.err.println("WARNING: Cannot connect to Gamepad library");
        }
        if (controllerManager != null)
            controllerManager.initSDLGamepad();
    }

    private void createNullAxis(String a, String horizontal, String vertical, String fire, String back, String action, String inventory, String start) {
        axes.add(new Axis(horizontal + a, null, null, null, null));
        axes.add(new Axis(vertical + a, null, null, null, null));
        axes.add(new Axis(fire + a, null, null, null, null));
        axes.add(new Axis(back + a, null, null, null, null));
        axes.add(new Axis(action + a, null, null, null, null));
        axes.add(new Axis(inventory + a, null, null, null, null));
        axes.add(new Axis(start + a, null, null, null, null));
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
        if (controllerManager != null)
            try {
                controllerManager.getControllerIndex(playerIndex).doVibration((float) leftMagnitude, (float) rightMagnitude, (int) duration * 1000);
            } catch (ControllerUnpluggedException e) {
                throw new RuntimeException(e);
            }
        else System.err.println("WARNING: Cannot connect to Gamepad library");
    }

    /**
     * This function updates the values of various input axes based on keyboard and controller inputs.
     */
    public void updateAxes() {
        mousePosition.set(applicationPlatform.getMouseCheck().getMouseX(), applicationPlatform.getMouseCheck().getMouseY());
        for (Axis axis : axes) {
            if ((axis.getPositiveKey() != null && isPressingKey(axis.getPositiveKey())) || (axis.getAltPositiveKey() != null && isPressingKey(axis.getAltPositiveKey()))) {
                axis.setValue(1);
            } else if ((axis.getNegativeKey() != null && isPressingKey(axis.getNegativeKey())) || (axis.getAltNegativeKey() != null && isPressingKey(axis.getAltNegativeKey()))) {
                axis.setValue(-1);
            } else {
                axis.setValue(0);
            }
        }
        if (controllerManager != null) {
            controllerManager.update();
            for (int i = 0; i < controllerManager.getNumControllers(); i++) {
                ControllerState device = controllerManager.getState(i);
                if (device.isConnected) {
                    String a = String.valueOf(i + 1);
                    if (i == 0)
                        a = "";
                    for (Axis axis : this.axes) {
                        if (axis.getName().equals(HORIZONTAL + a)) {
                            axis.setValue(device.leftStickX);
                            if (Math.abs(axis.getValue()) < 0.1)
                                axis.setValue(0);
                        } else if (axis.getName().equals(VERTICAL + a)) {
                            axis.setValue(device.leftStickY);
                            if (Math.abs(axis.getValue()) < 0.1)
                                axis.setValue(0);
                        } else if (axis.getName().equals(HORIZONTAL_CAM + a)) {
                            axis.setValue(device.rightStickX);
                            if (Math.abs(axis.getValue()) < 0.1)
                                axis.setValue(0);
                        } else if (axis.getName().equals(VERTICAL_CAM + a)) {
                            axis.setValue(device.rightStickY);
                            if (Math.abs(axis.getValue()) < 0.1)
                                axis.setValue(0);
                        } else if (axis.getName().equals(RIGHT_FIRE + a)) {
                            axis.setValue(device.rightTrigger);
                        } else if (axis.getName().equals(RUN + a)) {
                            axis.setValue(device.rightTrigger);
                        } else if (axis.getName().equals(LEFT_FIRE + a)) {
                            axis.setValue(device.leftTrigger);
                        } else if (axis.getName().equals(HORIZONTAL_PAD + a)) {
                            if (device.dpadRight) axis.setValue(1);
                            else if (device.dpadLeft) axis.setValue(-1);
                            else axis.setValue(0);
                        } else if (axis.getName().equals(VERTICAL_PAD + a)) {
                            if (device.dpadUp) axis.setValue(1);
                            else if (device.dpadDown) axis.setValue(-1);
                            else axis.setValue(0);
                        } else if (axis.getName().equals(FIRE + a)) {
                            if (device.a) axis.setValue(1);
                            else axis.setValue(0);
                        } else if (axis.getName().equals(ACTION + a)) {
                            if (device.x) axis.setValue(1);
                            else axis.setValue(0);
                        } else if (axis.getName().equals(BACK + a)) {
                            if (device.b) axis.setValue(1);
                            else axis.setValue(0);
                        } else if (axis.getName().equals(INVENTORY + a)) {
                            if (device.y) axis.setValue(1);
                            else axis.setValue(0);
                        } else if (axis.getName().equals(RIGHT_BUMPER + a)) {
                            if (device.rb) axis.setValue(1);
                            else axis.setValue(0);
                        } else if (axis.getName().equals(LEFT_BUMPER + a)) {
                            if (device.lb) axis.setValue(1);
                            else axis.setValue(0);
                        } else if (axis.getName().equals(RIGHT_THUMB + a)) {
                            if (device.rightStickClick) axis.setValue(1);
                            else axis.setValue(0);
                        } else if (axis.getName().equals(LEFT_THUMB + a)) {
                            if (device.leftStickClick) axis.setValue(1);
                            else axis.setValue(0);
                        } else if (axis.getName().equals(START + a)) {
                            if (device.start) axis.setValue(1);
                            else axis.setValue(0);
                        } else if (axis.getName().equals(VIEW + a)) {
                            if (device.back) axis.setValue(1);
                            else axis.setValue(0);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onUpdate() {
        if (scene != null && !(scene instanceof BlankScene)) {

            if (platformInit.isInvertZIndex())
                scene.getEntities().sort(Comparator.comparing(Entity2D::getIndex));
            else
                scene.getEntities().sort(Comparator.comparing(Entity2D::getIndex).reversed());

            boolean rendering = false;
            if (drawInstructions.size() > 0) {
                rendering = true;
                toSendDrawInstructions.clear();
                drawInstructions.forEach((di) -> toSendDrawInstructions.add(di.clone()));
                renderingThread.renderAsync(toSendDrawInstructions);
            }

            Scene scene = this.scene;
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

            updateAxes();

            if (changeSceneTransition == null || !changeSceneTransition.isStopUpdatingScene() || toChangeScene == null) {
                scene.onUpdate();
                drawInstructions.clear();
                if (scene.getFrames() >= 2) {
                    backGroundDrawInstruction.setStroke(0);
                    backGroundDrawInstruction.setColor(scene.getBackGroundColor());
                    drawInstructions.add(backGroundDrawInstruction);
                }
                try {
                    for (Entity2D entity : scene.getEntities()) {
                        DrawInstruction di = entity.process();
                        if (di != null && scene.getFrames() >= 2) drawInstructions.add(di);

                        if (scene != this.scene) {
                            return;
                        }
                    }
                } catch (ConcurrentModificationException ignore) {
                }
            }

            if (applicationPlatform.getInputManager() != null) {
                applicationPlatform.getInputManager().getPressingMouseButtons().remove(Input.MouseButton.SCROLL_UP);
                applicationPlatform.getInputManager().getPressingMouseButtons().remove(Input.MouseButton.SCROLL_DOWN);
            }


            if (changeSceneTransition != null) {
                changeSceneTransition.setApplication(this);
                changeSceneTransition.setDeltaTime(getApplicationManager().getManagerContext().getContextTime().getDeltaTime());
                changeSceneTransition.setPassedTime(changeSceneTransition.getPassedTime() + changeSceneTransition.getDeltaTime());
                changeSceneTransition.setFrames(changeSceneTransition.getFrames() + 1);
                drawInstructions.add(changeSceneTransition.render());
                if (changeSceneTransition.getPassedTime() >= changeSceneTransition.getTimeToWait() && toChangeScene != null) {
                    setScene(toChangeScene);
                    toChangeScene = null;
                }
                if (changeSceneTransition.isFinished())
                    changeSceneTransition = null;
            } else if (toChangeScene != null) {
                setScene(toChangeScene);
                toChangeScene = null;
            }

            if (rendering)
                renderingThread.aWait();
        }
    }

    @Override
    public void dispose() {
        renderingThread.getRunning().set(false);
        //Wake up thread
        renderingThread.renderAsync(null);
        setScene(null);
        if (controllerManager != null)
            controllerManager.quitSDLGamepad();
        applicationPlatform.getGraphicsManager().dispose();
    }

    /**
     * This function returns the value of an axis with a given name, or throws an exception if no axis with that name
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
     * The function returns the size of the viewport as a Size2D object.
     *
     * @return The method is returning an object of type `Size2D`.
     */
    public Size2D getViewportSize() {
        return viewportSize;
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
     * This function sets the scene and disposes of any previous scene and its entities and systems.
     *
     * @param scene The scene parameter is an object of the Scene class, which represents a collection of entities and
     *              systems that make up an application scene. This method sets the current scene to the specified scene object.
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
        if (scene != null) {
            scene.setFrames(0);
            applicationPlatform.getGraphicsManager().setCamera(scene.getCamera());
            applicationPlatform.getViewportZoomScale().setZoomScale(scene.getZoomScale());
        }
    }

    public void changeScene(Class<? extends Scene> sceneClass) {
        changeScene(sceneClass, null);
    }

    public void changeScene(Class<? extends Scene> sceneClass, ChangeSceneTransition changeSceneTransition) {
        try {
            changeScene(sceneClass.getConstructor(Application.class).newInstance(this), changeSceneTransition);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public void changeScene(Scene scene, ChangeSceneTransition changeSceneTransition) {
        if (toChangeScene != null)
            System.err.println("WARNING: Ignoring scene change, a scene change is already in place.");
        else {
            setToChangeScene(scene);
            setChangeSceneTransition(changeSceneTransition);
        }
    }

    /**
     * The function returns a ChangeSceneTransition object.
     *
     * @return The method is returning an object of type `ChangeSceneTransition`.
     */
    public ChangeSceneTransition getChangeSceneTransition() {
        return changeSceneTransition;
    }

    /**
     * The function sets the change scene transition for a given object.
     *
     * @param changeSceneTransition changeSceneTransition is a variable of type ChangeSceneTransition that is being passed
     *                              as a parameter to the method setChangeSceneTransition. The method sets the value of the instance variable
     *                              changeSceneTransition to the value passed as the parameter.
     */
    void setChangeSceneTransition(ChangeSceneTransition changeSceneTransition) {
        this.changeSceneTransition = changeSceneTransition;
    }

    /**
     * The function returns a Scene object to change the current scene.
     *
     * @return The method is returning a Scene object named "toChangeScene".
     */
    public Scene getToChangeScene() {
        return toChangeScene;
    }

    /**
     * The function sets the value of a variable "toChangeScene" to a given Scene object.
     *
     * @param toChangeScene toChangeScene is a variable of type Scene that represents the scene that the current scene will
     *                      change to. This method sets the value of the toChangeScene variable to the passed in Scene object.
     */
    void setToChangeScene(Scene toChangeScene) {
        this.toChangeScene = toChangeScene;
    }

    /**
     * This function checks if a specific key is currently being pressed.
     *
     * @param key The parameter "key" is of type Input.Key, which is an enum that represents a specific key on a standard
     *            keyboard. This method checks if the key is currently being pressed down by the user.
     * @return The method is returning a boolean value which indicates whether the specified key is being pressed or not.
     * It checks if the list of currently pressing keys obtained from the input manager contains the specified key, and
     * returns true if it does, false otherwise.
     */
    public boolean isPressingKey(Input.Key key) {
        if (applicationPlatform.getKeyCheck() == null && applicationPlatform.getInputManager() != null)
            return applicationPlatform.getInputManager().getPressingKeys().contains(key);
        if (applicationPlatform.getKeyCheck() != null && applicationPlatform.getInputManager() == null)
            return applicationPlatform.getKeyCheck().checkKey(key);
        return false;
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
        if (applicationPlatform.getKeyCheck() == null && applicationPlatform.getInputManager() != null)
            return applicationPlatform.getInputManager().getPressingMouseButtons().contains(button);
        if (applicationPlatform.getKeyCheck() != null && applicationPlatform.getInputManager() == null)
            return applicationPlatform.getKeyCheck().checkMouseButton(button);
        return false;
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
     * The function returns a DrawInstruction object.
     *
     * @return The method is returning an object of type `DrawInstruction`.
     */
    public DrawInstruction getBackGroundDrawInstruction() {
        return backGroundDrawInstruction;
    }

    public RenderingThread getRenderingThread() {
        return renderingThread;
    }

    public List<DrawInstruction> getDrawInstructions() {
        return drawInstructions;
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
     * The function returns the controller manager object.
     *
     * @return The method is returning an object of type `ControllerManager`.
     */
    public ControllerManager getControllerManager() {
        return controllerManager;
    }

    /**
     * This function sets the controller manager for the current object.
     *
     * @param controllerManager The parameter "controllerManager" is an object of the class "ControllerManager". This
     *                          method sets the value of the instance variable "controllerManager" to the value passed as the parameter.
     */
    public void setControllerManager(ControllerManager controllerManager) {
        this.controllerManager = controllerManager;
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

    /**
     * The function returns the current position of the mouse as a Vector2D object.
     *
     * @return A Vector2D object representing the position of the mouse.
     */
    public Vector2D getMousePosition() {
        return mousePosition;
    }

    /**
     * This function returns the application platform.
     *
     * @return The method is returning an object of type `ApplicationPlatform`.
     */
    public ApplicationPlatform getApplicationPlatform() {
        return applicationPlatform;
    }

    public void setControllerTexture(Texture controllerTexture) {
        this.controllerTexture = controllerTexture;
    }

    public void setTranslucentControllerTexture(Texture translucentControllerTexture) {
        this.translucentControllerTexture = translucentControllerTexture;
    }
}
