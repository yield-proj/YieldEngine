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

package com.xebisco.yield;

import com.xebisco.yield.font.Font;
import com.xebisco.yield.manager.ApplicationManager;
import com.xebisco.yield.manager.PCInputManager;
import com.xebisco.yield.platform.ApplicationModule;
import com.xebisco.yield.platform.ApplicationPlatform;
import com.xebisco.yield.texture.Texture;
import com.xebisco.yield.texture.TextureFilter;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents the main application. It is responsible for managing scenes, rendering, and updating entities.
 * It also provides methods for interacting with the application platform and managing input.
 */
public class Application extends AbstractBehavior {
    public final Vector2D mousePosition = new Vector2D();
    private final ApplicationPlatform applicationPlatform;
    private final PlatformInit platformInit;
    private final ApplicationManager applicationManager;
    private final Vector2D viewportSize;
    private Scene scene;
    private Font defaultFont;
    private Texture defaultTexture;
    private Map<String, Axis> axisMap = new HashMap<>();

    /**
     * Constructs a new Application instance.
     *
     * @param applicationManager The application manager.
     * @param initialScene       The initial scene class.
     * @param applicationPlatform The application platform.
     * @param platformInit       The platform initialization parameters.
     */
    public Application(ApplicationManager applicationManager, Class<? extends Scene> initialScene, ApplicationPlatform applicationPlatform, PlatformInit platformInit) {
        this.applicationManager = applicationManager;
        applicationManager.applications().add(this);
        this.applicationPlatform = applicationPlatform;

        checkPlatform(applicationPlatform, platformInit);

        try {
            setScene(new BlankScene(this));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            //noinspection resource
            setScene(initialScene.getConstructor(Application.class).newInstance(this));
        } catch (IOException | InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        this.platformInit = platformInit;
        viewportSize = new ImmutableVector2D(platformInit.windowSize().width(), platformInit.viewportSize().height());


        axisMap.put(Global.HORIZONTAL, new Axis(Input.Key.VK_D, Input.Key.VK_A, Input.Key.VK_RIGHT, Input.Key.VK_LEFT));
        axisMap.put(Global.VERTICAL, new Axis(Input.Key.VK_W, Input.Key.VK_S, Input.Key.VK_UP, Input.Key.VK_DOWN));
        axisMap.put(Global.JUMP, new Axis(Input.Key.VK_SPACE, Input.Key.VK_SHIFT));
    }

    /**
     * Constructs a new Application instance with a default blank scene.
     *
     * @param applicationManager The application manager.
     * @param applicationPlatform The application platform.
     * @param platformInit       The platform initialization parameters.
     */
    public Application(ApplicationManager applicationManager, ApplicationPlatform applicationPlatform, PlatformInit platformInit) {
        this(applicationManager, BlankScene.class, applicationPlatform, platformInit);
    }


    /**
     * Checks if the application platform contains all required modules.
     *
     * @param platform The application platform.
     * @param init     The platform initialization parameters.
     */
    private void checkPlatform(ApplicationPlatform platform, PlatformInit init) {
        for (ApplicationModule module : init.requiredPlatformModules()) {
            if(!platform.modules().containsKey(module)) throw new ApplicationPlatformModuleException("The application platform does not contain the '" + module.name() + "' module.");
        }
    }

    @Override
    public void onStart() {
        applicationPlatform.graphicsManager().init(platformInit);
        defaultFont = new Font("default-font.ttf", 32, applicationPlatform.fontManager(), applicationPlatform.ioManager());
        try {
            applicationPlatform.graphicsManager().updateWindowIcon(new Texture(platformInit.windowIconPath(), TextureFilter.LINEAR, applicationPlatform.textureManager()), applicationPlatform.ioManager());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (applicationPlatform.modules().containsKey(ApplicationModule.TEXTURE_MANAGER)) {
            try {
                defaultTexture = new Texture("yieldIcon.png", TextureFilter.LINEAR, applicationPlatform.textureManager());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void onUpdate(ContextTime time) {
        if (scene != null) {
            Scene scene = this.scene;
            scene.tick(time);
            applicationPlatform.graphicsManager().setCamera(scene.camera());
            applicationPlatform.graphicsManager().start(scene.backGroundColor());
            scene.render(applicationPlatform.graphicsManager());
            applicationPlatform.graphicsManager().finish();
        }
    }

    @Override
    public void close() throws IOException {
        //noinspection resource
        setScene(null);
        applicationPlatform.close();
    }

    /**
     * Checks if a key is currently pressed.
     *
     * @param key The key to check.
     * @return True if the key is pressed, false otherwise.
     */
    public boolean isKeyPressed(Input.Key key) {
        return applicationPlatform.pcInputManager().getPressingKeys().contains(key);
    }

    /**
     * Checks if a mouse button is currently pressed.
     *
     * @param mouseButton The mouse button to check.
     * @return True if the mouse button is pressed, false otherwise.
     */
    public boolean isMouseButtonPressed(Input.MouseButton mouseButton) {
        return applicationPlatform.pcInputManager().getPressingMouseButtons().contains(mouseButton);
    }


    /**
     * Sets the current scene and disposes of any previous scene and its entities and systems.
     *
     * @param scene The new scene.
     * @throws IOException If an I/O error occurs.
     * @return The application instance for method chaining.
     */
    public Application setScene(Scene scene) throws IOException {
        if(this.scene != null) this.scene.close();
        this.scene = scene;
        return this;
    }

    /**
     * Gets the current mouse position.
     *
     * @return The current mouse position.
     */
    public Vector2D mousePosition() {
        return mousePosition;
    }

    /**
     * Gets the application platform.
     *
     * @return The application platform.
     */
    public ApplicationPlatform applicationPlatform() {
        return applicationPlatform;
    }

    /**
     * Gets the platform initialization parameters.
     *
     * @return The platform initialization parameters.
     */
    public PlatformInit platformInit() {
        return platformInit;
    }

    /**
     * Gets the application manager.
     *
     * @return The application manager.
     */
    public ApplicationManager applicationManager() {
        return applicationManager;
    }

    /**
     * Gets the viewport size.
     *
     * @return The viewport size.
     */
    public Vector2D viewportSize() {
        return viewportSize;
    }

    /**
     * Gets the current scene.
     *
     * @return The current scene.
     */
    public Scene scene() {
        return scene;
    }

    /**
     * Gets the default font.
     *
     * @return The default font.
     */
    public Font defaultFont() {
        return defaultFont;
    }

    /**
     * Sets the default font.
     *
     * @param defaultFont The new default font.
     * @return The application instance for method chaining.
     */
    public Application setDefaultFont(Font defaultFont) {
        this.defaultFont = defaultFont;
        return this;
    }

    /**
     * Gets the default texture.
     *
     * @return The default texture.
     */
    public Texture defaultTexture() {
        return defaultTexture;
    }

    /**
     * Sets the default texture.
     *
     * @param defaultTexture The new default texture.
     * @return The application instance for method chaining.
     */
    public Application setDefaultTexture(Texture defaultTexture) {
        this.defaultTexture = defaultTexture;
        return this;
    }

    /**
     * Gets the value of an axis based on the specified axis name.
     *
     * @param axisName The name of the axis.
     * @return The value of the axis (-1, 0, or 1).
     */
    public double axis(String axisName) {
        Axis axis = axisMap().get(axisName);
        if(isKeyPressed(axis.positiveKey()) || isKeyPressed(axis.altPositiveKey())) {
            return 1;
        }
        else if(isKeyPressed(axis.negativeKey()) || isKeyPressed(axis.altNegativeKey())) {
            return -1;
        }
        return 0;
    }

    /**
     * Gets a 2D axis vector based on the specified x and y-axis names.
     *
     * @param xAxis The name of the x-axis.
     * @param yAxis The name of the y-axis.
     * @return The 2D axis vector.
     */
    public Vector2D axis2D(String xAxis, String yAxis) {
        return new Vector2D(axis(xAxis), axis(yAxis));
    }

    /**
     * Gets the map of axis configurations.
     *
     * @return The map of axis configurations.
     */
    public Map<String, Axis> axisMap() {
        return axisMap;
    }

    /**
     * Sets the map of axis configurations.
     *
     * @param axisMap The new map of axis configurations.
     * @return The application instance for method chaining.
     */
    public Application setAxisMap(Map<String, Axis> axisMap) {
        this.axisMap = axisMap;
        return this;
    }
}
