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
import com.xebisco.yield.shader.VertexShader;
import com.xebisco.yield.shader.DefaultVertexShader;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

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
    private final Vector2D viewportSize;
    private final RenderingThread renderingThread;
    private Texture controllerTexture;
    private Texture translucentControllerTexture;
    private final Map<String, VertexShader> vertexShaderMap = new HashMap<>();
    private ControllerManager controllerManager;
    private int frames;
    private Scene scene;
    private double physicsPpm;
    private Font defaultFont;
    private Texture defaultTexture;
    private ChangeSceneTransition changeSceneTransition;
    private final List<DrawInstruction> drawInstructions = new ArrayList<>(), toSendDrawInstructions = new ArrayList<>();
    private Scene toChangeScene;

    private Runnable physicsProcess;

    private final Context physicsContext;

    public Application(ApplicationManager applicationManager, Class<? extends Scene> initialScene, ApplicationPlatform applicationPlatform, PlatformInit platformInit) {
        physicsPpm = platformInit.startPhysicsPpm();
        this.applicationManager = applicationManager;
        applicationManager.applications().add(this);
        this.applicationPlatform = applicationPlatform;

        physicsProcess = () -> {
            if (scene != null) {
                scene.getPhysicsMain().process(!platformInit.physicsOnMainContext() ? (float) (platformInit.physicsContextTime().getTargetSleepTime() * platformInit.physicsContextTime().getTimeScale() * applicationManager().managerContext().getContextTime().getTimeScale() / 1_000_000.) : (float) (applicationManager().managerContext().getContextTime().getTargetSleepTime() * applicationManager().managerContext().getContextTime().getTimeScale() / 1_000_000.));
                try {
                    for (Entity2D entity : scene.getEntities()) {
                        entity.processPhysics();
                    }
                } catch (ConcurrentModificationException ignore) {

                }
            }
        };

        if (!platformInit.physicsOnMainContext()) {
            physicsContext = new Context(platformInit.physicsContextTime(), physicsProcess, null, "PhysicsMain");
            physicsProcess = null;
        } else {
            physicsContext = null;
        }

        checkPlatform(applicationPlatform, platformInit);

        setScene(new BlankScene(this));

        changeScene(initialScene);

        vertexShaderMap.put("default-shader", new DefaultVertexShader());

        this.platformInit = platformInit;
        renderingThread = new RenderingThread(applicationPlatform.graphicsManager());
        viewportSize = new ImmutableVector2D(platformInit.windowSize().width(), platformInit.viewportSize().height());
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

    public Application(ApplicationManager applicationManager, ApplicationPlatform applicationPlatform, PlatformInit platformInit) {
        this(applicationManager, BlankScene.class, applicationPlatform, platformInit);
    }


    private void checkPlatform(ApplicationPlatform platform, PlatformInit init) {
        for (Class<?> c : init.requiredPlatformModules()) {
            if (c.equals(FontManager.class)) {
                if (platform.fontManager() == null)
                    throw new ApplicationPlatformModuleException("The application platform does not contain the '" + c.getSimpleName() + "' module.");
            } else if (c.equals(TextureManager.class)) {
                if (platform.textureManager() == null)
                    throw new ApplicationPlatformModuleException("The application platform does not contain the '" + c.getSimpleName() + "' module.");
            } else if (c.equals(InputManager.class)) {
                if (platform.inputManager() == null)
                    throw new ApplicationPlatformModuleException("The application platform does not contain the '" + c.getSimpleName() + "' module.");
            } else if (c.equals(KeyCheck.class)) {
                if (platform.keyCheck() == null)
                    throw new ApplicationPlatformModuleException("The application platform does not contain the '" + c.getSimpleName() + "' module.");
            } else if (c.equals(MouseCheck.class)) {
                if (platform.mouseCheck() == null)
                    throw new ApplicationPlatformModuleException("The application platform does not contain the '" + c.getSimpleName() + "' module.");
            } else if (c.equals(AudioManager.class)) {
                if (platform.audioManager() == null)
                    throw new ApplicationPlatformModuleException("The application platform does not contain the '" + c.getSimpleName() + "' module.");
            } else if (c.equals(ViewportZoomScale.class)) {
                if (platform.viewportZoomScale() == null)
                    throw new ApplicationPlatformModuleException("The application platform does not contain the '" + c.getSimpleName() + "' module.");
            } else if (c.equals(ToggleFullScreen.class)) {
                if (platform.toggleFullScreen() == null)
                    throw new ApplicationPlatformModuleException("The application platform does not contain the '" + c.getSimpleName() + "' module.");
            } else if (c.equals(GraphicsManager.class)) {
                if (platform.graphicsManager() == null)
                    throw new ApplicationPlatformModuleException("The application platform does not contain the '" + c.getSimpleName() + "' module.");
            } else if (c.equals(SpritesheetTextureManager.class)) {
                if (platform.spritesheetTextureManager() == null)
                    throw new ApplicationPlatformModuleException("The application platform does not contain the '" + c.getSimpleName() + "' module.");
            } else {
                throw new ApplicationPlatformModuleException("Not supported application module. '" + c.getSimpleName() + "'");
            }
        }
    }

    @Override
    public void onStart() {
        applicationPlatform.graphicsManager().init(platformInit);
        renderingThread.start();
        defaultFont = new Font("com/xebisco/yield/OpenSans-Regular.ttf", 48, applicationPlatform.fontManager());
        if (platformInit.windowIcon() == null)
            platformInit.setWindowIcon(new Texture(platformInit.windowIconPath(), applicationPlatform.textureManager()));
        applicationPlatform.graphicsManager().updateWindowIcon(platformInit.windowIcon());
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
        if (applicationPlatform.textureManager() != null) {
            defaultTexture = new Texture("com/xebisco/yield/yieldIcon.png", applicationPlatform.textureManager());
            controllerTexture = new Texture("com/xebisco/yield/controller.png", applicationPlatform.textureManager());
            translucentControllerTexture = new Texture("com/xebisco/yield/translucentController.png", applicationPlatform.textureManager());
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

        if (physicsContext != null)
            physicsContext.getThread().start();
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
        mousePosition.set(applicationPlatform.mouseCheck().getMouseX(), applicationPlatform.mouseCheck().getMouseY());
        for (Axis axis : axes) {
            if ((axis.positiveKey() != null && pressingKey(axis.positiveKey())) || (axis.altPositiveKey() != null && pressingKey(axis.altPositiveKey()))) {
                axis.setValue(1);
            } else if ((axis.negativeKey() != null && pressingKey(axis.negativeKey())) || (axis.altNegativeKey() != null && pressingKey(axis.altNegativeKey()))) {
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
                        if (axis.name().equals(HORIZONTAL + a)) {
                            axis.setValue(device.leftStickX);
                            if (Math.abs(axis.value()) < 0.1)
                                axis.setValue(0);
                        } else if (axis.name().equals(VERTICAL + a)) {
                            axis.setValue(device.leftStickY);
                            if (Math.abs(axis.value()) < 0.1)
                                axis.setValue(0);
                        } else if (axis.name().equals(HORIZONTAL_CAM + a)) {
                            axis.setValue(device.rightStickX);
                            if (Math.abs(axis.value()) < 0.1)
                                axis.setValue(0);
                        } else if (axis.name().equals(VERTICAL_CAM + a)) {
                            axis.setValue(device.rightStickY);
                            if (Math.abs(axis.value()) < 0.1)
                                axis.setValue(0);
                        } else if (axis.name().equals(RIGHT_FIRE + a)) {
                            axis.setValue(device.rightTrigger);
                        } else if (axis.name().equals(RUN + a)) {
                            axis.setValue(device.rightTrigger);
                        } else if (axis.name().equals(LEFT_FIRE + a)) {
                            axis.setValue(device.leftTrigger);
                        } else if (axis.name().equals(HORIZONTAL_PAD + a)) {
                            if (device.dpadRight) axis.setValue(1);
                            else if (device.dpadLeft) axis.setValue(-1);
                            else axis.setValue(0);
                        } else if (axis.name().equals(VERTICAL_PAD + a)) {
                            if (device.dpadUp) axis.setValue(1);
                            else if (device.dpadDown) axis.setValue(-1);
                            else axis.setValue(0);
                        } else if (axis.name().equals(FIRE + a)) {
                            if (device.a) axis.setValue(1);
                            else axis.setValue(0);
                        } else if (axis.name().equals(ACTION + a)) {
                            if (device.x) axis.setValue(1);
                            else axis.setValue(0);
                        } else if (axis.name().equals(BACK + a)) {
                            if (device.b) axis.setValue(1);
                            else axis.setValue(0);
                        } else if (axis.name().equals(INVENTORY + a)) {
                            if (device.y) axis.setValue(1);
                            else axis.setValue(0);
                        } else if (axis.name().equals(RIGHT_BUMPER + a)) {
                            if (device.rb) axis.setValue(1);
                            else axis.setValue(0);
                        } else if (axis.name().equals(LEFT_BUMPER + a)) {
                            if (device.lb) axis.setValue(1);
                            else axis.setValue(0);
                        } else if (axis.name().equals(RIGHT_THUMB + a)) {
                            if (device.rightStickClick) axis.setValue(1);
                            else axis.setValue(0);
                        } else if (axis.name().equals(LEFT_THUMB + a)) {
                            if (device.leftStickClick) axis.setValue(1);
                            else axis.setValue(0);
                        } else if (axis.name().equals(START + a)) {
                            if (device.start) axis.setValue(1);
                            else axis.setValue(0);
                        } else if (axis.name().equals(VIEW + a)) {
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
        boolean rendering = false;
        if (scene != null) {
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
                for (Entity2D entity : scene.getEntities()) {
                    DrawInstruction di = entity.process();
                    entity.updateEntityList();
                    if (di != null && scene.getFrames() >= 2) drawInstructions.add(di);

                    if (scene != this.scene) {
                        return;
                    }
                }

                if(physicsProcess != null)
                    physicsProcess.run();
            }

            scene.updateEntityList();

            if (applicationPlatform.inputManager() != null) {
                applicationPlatform.inputManager().getPressingMouseButtons().remove(Input.MouseButton.SCROLL_UP);
                applicationPlatform.inputManager().getPressingMouseButtons().remove(Input.MouseButton.SCROLL_DOWN);
            }

        }
        if (changeSceneTransition != null) {
            changeSceneTransition.setApplication(this);
            changeSceneTransition.setDeltaTime(applicationManager().managerContext().getContextTime().getDeltaTime());
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

    @Override
    public void dispose() {
        renderingThread.getRunning().set(false);
        //Wake up thread
        renderingThread.renderAsync(null);
        if (physicsContext != null)
            physicsContext.getRunning().set(false);
        setScene(null);
        if (controllerManager != null)
            controllerManager.quitSDLGamepad();
        applicationPlatform.graphicsManager().dispose();
    }

    /**
     * This function returns the value of an axis with a given name, or throws an exception if no axis with that name
     * exists.
     *
     * @param name The parameter "name" is a String representing the name of the axis that the method is trying to retrieve
     *             the value for.
     * @return The method is returning a double value which represents the value of the axis with the given name.
     */
    public double axis(String name) {
        for (Axis axis : axes) {
            if (axis.name().hashCode() == name.hashCode() && axis.name().equals(name))
                return axis.value();
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
    public Vector2D axis2D(String axisX, String axisY) {
        return new Vector2D(axis(axisX), axis(axisY));
    }


    /**
     * This function sets the scene and disposes of any previous scene and its entities and systems.
     *
     * @param scene The scene parameter is an object of the Scene class, which represents a collection of entities and
     *              systems that make up an application scene. This method sets the current scene to the specified scene object.
     */
    public Application setScene(Scene scene) {
        if (this.scene != null) {
            for (int i = this.scene.getEntities().size() - 1; i >= 0; i--) {
                this.scene.getEntities().get(i).dispose();
            }
            this.scene.updateEntityList();
            for (SystemBehavior system : this.scene.getSystems()) {
                system.dispose();
            }
            this.scene.dispose();
        }
        this.scene = scene;
        if (scene != null) {
            scene.setFrames(0);
            applicationPlatform.graphicsManager().setCamera(scene.getCamera());
            applicationPlatform.viewportZoomScale().setZoomScale(scene.getZoomScale());
        }
        return this;
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
            setToChangeScene(scene).setChangeSceneTransition(changeSceneTransition);
        }
    }

    public Application setChangeSceneTransition(ChangeSceneTransition changeSceneTransition) {
        this.changeSceneTransition = changeSceneTransition;
        return this;
    }

    public Application setToChangeScene(Scene toChangeScene) {
        this.toChangeScene = toChangeScene;
        return this;
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
    public boolean pressingKey(Input.Key key) {
        if (applicationPlatform.keyCheck() == null && applicationPlatform.inputManager() != null)
            return applicationPlatform.inputManager().getPressingKeys().contains(key);
        if (applicationPlatform.keyCheck() != null && applicationPlatform.inputManager() == null)
            return applicationPlatform.keyCheck().checkKey(key);
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
    public boolean pressingButton(Input.MouseButton button) {
        if (applicationPlatform.keyCheck() == null && applicationPlatform.inputManager() != null)
            return applicationPlatform.inputManager().getPressingMouseButtons().contains(button);
        if (applicationPlatform.keyCheck() != null && applicationPlatform.inputManager() == null)
            return applicationPlatform.keyCheck().checkMouseButton(button);
        return false;
    }

    public Vector2D mousePosition() {
        return mousePosition;
    }

    public ApplicationPlatform applicationPlatform() {
        return applicationPlatform;
    }

    public PlatformInit platformInit() {
        return platformInit;
    }

    public DrawInstruction backGroundDrawInstruction() {
        return backGroundDrawInstruction;
    }

    public Set<Axis> axes() {
        return axes;
    }

    public ApplicationManager applicationManager() {
        return applicationManager;
    }

    public Vector2D viewportSize() {
        return viewportSize;
    }

    public RenderingThread renderingThread() {
        return renderingThread;
    }

    public Texture controllerTexture() {
        return controllerTexture;
    }

    public Application setControllerTexture(Texture controllerTexture) {
        this.controllerTexture = controllerTexture;
        return this;
    }

    public Texture translucentControllerTexture() {
        return translucentControllerTexture;
    }

    public Application setTranslucentControllerTexture(Texture translucentControllerTexture) {
        this.translucentControllerTexture = translucentControllerTexture;
        return this;
    }

    public Map<String, VertexShader> vertexShaderMap() {
        return vertexShaderMap;
    }

    public ControllerManager controllerManager() {
        return controllerManager;
    }

    public Application setControllerManager(ControllerManager controllerManager) {
        this.controllerManager = controllerManager;
        return this;
    }

    public int frames() {
        return frames;
    }

    public Scene scene() {
        return scene;
    }

    public double physicsPpm() {
        return physicsPpm;
    }

    public Application setPhysicsPpm(double physicsPpm) {
        this.physicsPpm = physicsPpm;
        return this;
    }

    public Font defaultFont() {
        return defaultFont;
    }

    public Application setDefaultFont(Font defaultFont) {
        this.defaultFont = defaultFont;
        return this;
    }

    public Texture defaultTexture() {
        return defaultTexture;
    }

    public Application setDefaultTexture(Texture defaultTexture) {
        this.defaultTexture = defaultTexture;
        return this;
    }

    public ChangeSceneTransition changeSceneTransition() {
        return changeSceneTransition;
    }

    public List<DrawInstruction> drawInstructions() {
        return drawInstructions;
    }

    public List<DrawInstruction> toSendDrawInstructions() {
        return toSendDrawInstructions;
    }

    public Scene toChangeScene() {
        return toChangeScene;
    }

    public Runnable physicsProcess() {
        return physicsProcess;
    }

    public Application setPhysicsProcess(Runnable physicsProcess) {
        this.physicsProcess = physicsProcess;
        return this;
    }

    public Context physicsContext() {
        return physicsContext;
    }

    public Application setFrames(int frames) {
        this.frames = frames;
        return this;
    }
}
