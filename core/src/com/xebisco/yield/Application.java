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
 * It's a class that implements the `Behavior` interface and is responsible for rendering the scene and updating the
 * entities
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

    public Application(ApplicationManager applicationManager, ApplicationPlatform applicationPlatform, PlatformInit platformInit) {
        this(applicationManager, BlankScene.class, applicationPlatform, platformInit);
    }


    private void checkPlatform(ApplicationPlatform platform, PlatformInit init) {
        for (ApplicationModule module : init.requiredPlatformModules()) {
            if(!platform.modules().containsKey(module)) throw new ApplicationPlatformModuleException("The application platform does not contain the '" + module.name() + "' module.");
        }
    }

    @Override
    public void onStart() {
        applicationPlatform.graphicsManager().init(platformInit);
        defaultFont = new Font("OpenSans-Regular.ttf", 32, applicationPlatform.fontManager());
        try {
            applicationPlatform.graphicsManager().updateWindowIcon(new Texture(platformInit.windowIconPath(), TextureFilter.LINEAR, applicationPlatform.textureManager()));
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

    public boolean isKeyPressed(Input.Key key) {
        return applicationPlatform.pcInputManager().getPressingKeys().contains(key);
    }

    public boolean isMouseButtonPressed(Input.MouseButton mouseButton) {
        return applicationPlatform.pcInputManager().getPressingMouseButtons().contains(mouseButton);
    }


    /**
     * This function sets the scene and disposes of any previous scene and its entities and systems.
     *
     * @param scene The scene parameter is an object of the Scene class, which represents a collection of entities and
     *              systems that make up an application scene. This method sets the current scene to the specified scene object.
     */
    public Application setScene(Scene scene) throws IOException {
        if(this.scene != null) this.scene.close();
        this.scene = scene;
        return this;
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

    public ApplicationManager applicationManager() {
        return applicationManager;
    }

    public Vector2D viewportSize() {
        return viewportSize;
    }

    public Scene scene() {
        return scene;
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

    public Vector2D axis2D(String xAxis, String yAxis) {
        return new Vector2D(axis(xAxis), axis(yAxis));
    }

    public Map<String, Axis> axisMap() {
        return axisMap;
    }

    public Application setAxisMap(Map<String, Axis> axisMap) {
        this.axisMap = axisMap;
        return this;
    }
}
