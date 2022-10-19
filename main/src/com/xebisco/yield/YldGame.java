/*
 * Copyright [2022] [Xebisco]
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

import com.xebisco.yield.engine.Engine;
import com.xebisco.yield.engine.EngineStop;
import com.xebisco.yield.engine.GameHandler;
import com.xebisco.yield.engine.YldEngineAction;
import com.xebisco.yield.exceptions.MissingWindowPrintException;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class is the starting point for every Yield Game, it contains all the objects of the game.
 *
 * @author Xebisco
 * @since 4_alpha1
 */
public class YldGame extends YldScene {
    private GameConfiguration configuration;
    private boolean started = false;

    private SampleWindow window;

    private Texture yieldLogo;

    private GameHandler handler;
    /**
     * All the scenes that are in this YldGame instance.
     *
     * @since 4_alpha1
     */
    protected ArrayList<YldScene> scenes = new ArrayList<>();
    /**
     * The actual scene that is displaying.
     *
     * @since 4_alpha1
     */
    protected YldScene scene;

    @Override
    public void start() {

    }

    /**
     * This is the method that will create an YldGame instance receiving a GameConfiguration variable, it will set all the game based in the GameConfiguration instance.
     *
     * @param game          The YldGame to be created.
     * @param configuration The YldGame instance of the GameConfiguration class.
     * @since 4_alpha1
     */
    public static void launch(YldGame game, GameConfiguration configuration) {
        Yld.debug(() -> Yld.log("Starting game '" + game.getClass().getSimpleName() + "'."));
        if (configuration == null)
            configuration = (GameConfiguration) new JsonFileWrapper("com/xebisco/yield/assets/stdlaunchconfig.json", GameConfiguration.class).getObject();
        game.setConfiguration(configuration);
        game.addScene(game);
        game.setInput(new YldInput(game));
        new GameHandler(game);
        if (configuration.icon != null)
            game.loadTexture(configuration.icon);
        game.setWindow(game.getHandler().getRenderMaster().initWindow(game.getConfiguration()));
        if (configuration.loadYieldLogo) {
            game.yieldLogo = new Texture("com/xebisco/yield/assets/yieldlogo.png");
            game.loadTexture(game.yieldLogo);
        }
        game.loadFont("arial", 30, 0, new RelativeFile("com/xebisco/yield/assets/ArialNormal.ttf"));
        game.setScene(game.getClass());
        game.getHandler().getThread().start();
        Yld.debug(() -> Yld.log("Game '" + game.getClass().getSimpleName() + "' started."));
    }

    /**
     * This function launches the game.
     *
     * @param game The game object.
     */
    public static void launch(YldGame game) {
        launch(game, null);
    }

    /**
     * Update the scene, and if it's the first frame, call the create() method on all systems and the scene itself.
     * The first thing we do is check if the scene has been updated before. If it hasn't, we call the create() method on
     * all systems and the scene itself. This is done by checking if the system implements the SystemCreateMethod
     * interface, and if it does, we call the create() method on it.
     *
     * @param delta    The time in seconds since the last frame.
     * @param graphics The graphics object that is used to draw the scene.
     */
    public final void updateScene(float delta, SampleGraphics graphics) {
        if (scene.getFrames() == 0) {
            scene.setView(new View(1280, 720));
            for (YldSystem system : scene.getSystems()) {
                if (system instanceof SystemCreateMethod) {
                    ((SystemCreateMethod) system).create();
                }
            }
            scene.create();
        }
        scene.setFrames(scene.getFrames() + 1);
        if (scene.isCallStart()) {
            scene.setCallStart(false);
            scene.start();
            System.gc();
        }
        scene.update(delta);
        globalUpdate(delta);
        scene.process(delta, graphics);
    }

    /**
     * It sets the value of a field in the render master
     *
     * @param property The name of the field you want to set.
     * @param value    The value to set the field to.
     */
    public void setRenderProperty(String property, Object value) {
        try {
            handler.getRenderMaster().getClass().getField(property).set(handler.getRenderMaster(), value);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            Yld.debug(() -> Yld.err("Could not find '" + property + "' in " + handler.getRenderMaster().getClass().getSimpleName() + " render master."));
        }
    }

    /**
     * It gets a property from the render master
     *
     * @param property The name of the property you want to get.
     * @return The value of the field with the passed property name.
     */
    public Object getRenderProperty(String property) {
        try {
            return handler.getRenderMaster().getClass().getField(property).get(handler.getRenderMaster());
        } catch (IllegalAccessException | NoSuchFieldException e) {
            Yld.debug(() -> Yld.err("Could not find '" + property + "' in " + handler.getRenderMaster().getClass().getSimpleName() + " render master."));
        }
        return null;
    }

    /**
     * This function is called every frame, not mattering what scene the game is on.
     *
     * @param delta The time in seconds since the last update.
     */
    public void globalUpdate(float delta) {

    }

    /**
     * It creates an engine that processes the filters on a texture
     *
     * @param texture       The texture to process
     * @param fps           The frames per second of the engine.
     * @param stopAfterSame If true, the engine will stop after the texture is the same as the past colors.
     * @return An engine that processes the filters on the texture.
     */
    public Engine processFilters(Texture texture, int fps, boolean stopAfterSame) {
        Engine engine = new Engine(null);
        engine.setTargetTime(1000 / fps);
        engine.getTodoList().add(new YldEngineAction(texture::processFilters, 0, true, Yld.RAND.nextLong()));
        engine.getThread().start();
        if (stopAfterSame) {
            Engine checkResults = new Engine(null);
            checkResults.setTargetTime(60);
            checkResults.getTodoList().add(new YldEngineAction(() -> {
                if (checkTexToSTDColors(texture)) {
                    engine.setStopOnNext(true);
                    checkResults.setStopOnNext(true);
                }
            }, 0, true, Yld.RAND.nextLong()));
            checkResults.getThread().start();
        }
        return engine;
    }

    /**
     * It overlays two textures on top of each other
     *
     * @param tex1 The first texture to be overlayed.
     * @param tex2 The texture to overlay
     * @param pos1 The position of the first texture.
     * @param pos2 The position of the second texture.
     * @return A texture.
     */
    public Texture overlayTexture(Texture tex1, Texture tex2, Vector2 pos1, Vector2 pos2) {
        return handler.getRenderMaster().overlayTexture(tex1, tex2, pos1, pos2);
    }

    /**
     * It overlays two textures on top of each other
     *
     * @param tex1 The first texture to be overlayed.
     * @param tex2 The texture to overlay
     * @param pos  The position of the second texture.
     * @return A texture.
     */
    public Texture overlayTexture(Texture tex1, Texture tex2, Vector2 pos) {
        return overlayTexture(tex1, tex2, new Vector2(), pos);
    }

    /**
     * It overlays two textures on top of each other
     *
     * @param tex1 The first texture to be overlayed.
     * @param tex2 The texture to overlay
     * @return A texture.
     */
    public Texture overlayTexture(Texture tex1, Texture tex2) {
        return overlayTexture(tex1, tex2, new Vector2(), new Vector2());
    }

    /**
     * It takes a 2D array of colors, and returns the average color of all the non-null, non-transparent colors in the
     * array
     *
     * @param colors The array of colors to get the predominant color from.
     * @return The average color of the 2D array.
     */
    public Color predominantColor(Color[][] colors) {
        Color color = new Color(0, 0, 0);
        float r = 0, g = 0, b = 0;
        float count = 0;
        for (int x = 0; x < colors.length; x++) {
            for (int y = 0; y < colors[0].length; y++) {
                Color c = colors[x][y];
                if (c != null && c.getA() != 0) {
                    count += 1f;
                    r += c.getR();
                    g += c.getG();
                    b += c.getB();
                }
            }
        }
        color.setR(r / count);
        color.setG(g / count);
        color.setB(b / count);
        return color;
    }

    /**
     * It takes a texture and returns the predominant color of that texture
     *
     * @param texture The texture to get the predominant color from.
     * @return The predominant color of the texture.
     */
    public Color predominantColor(Texture texture) {
        return predominantColor(getTextureColors(texture));
    }

    private Color[][] processColorsSTD;

    /**
     * It checks if the colors of the texture are the same as the colors of the last texture processed
     *
     * @param tex The texture to check
     * @return The boolean value of the checkTexToSTDColors method.
     */
    private boolean checkTexToSTDColors(Texture tex) {
        boolean toReturn = false;
        if (processColorsSTD != null) {
            toReturn = equalsColors(getTextureColors(tex), processColorsSTD);
        }
        processColorsSTD = getTextureColors(tex);
        return toReturn;
    }

    /**
     * It compares two textures by comparing the colors of the pixels in the textures
     *
     * @param texture1 The first texture to compare.
     * @param texture2 The texture to compare to.
     * @return A boolean value.
     */
    public boolean equalsTexture(Texture texture1, Texture texture2) {
        return equalsColors(getTextureColors(texture1), getTextureColors(texture2));
    }

    /**
     * It returns true if the two arrays are equal, and false otherwise
     *
     * @param colors1 The first color array to compare.
     * @param colors2 The colors to compare to.
     * @return The hash code of the array.
     */
    public boolean equalsColors(Color[][] colors1, Color[][] colors2) {
        return Arrays.deepHashCode(colors1) == Arrays.deepHashCode(colors2);
    }

    /**
     * It creates an engine that processes the filters on a texture
     *
     * @param texture The texture to process
     * @return An engine that processes the filters on the texture.
     */
    public Engine processFilters(Texture texture) {
        return processFilters(texture, (int) time.getTargetFPS(), true);
    }

    /**
     * Returns true if the game is started, false otherwise.
     *
     * @return The boolean value of the variable started.
     */
    public boolean isStarted() {
        return started;
    }

    /**
     * Getter for the configuration variable.
     *
     * @return The configuration variable.
     */
    public GameConfiguration getConfiguration() {
        return configuration;
    }

    /**
     * Getter for the handler variable.
     *
     * @return The handler variable.
     */
    public GameHandler getHandler() {
        return handler;
    }

    /**
     * Setter for the handler variable.
     */
    public void setHandler(GameHandler handler) {
        this.handler = handler;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * Adds and sets the passed scene to the scenes list.
     *
     * @param scene The scene to be added.
     */
    public void addScene(YldScene scene) {
        scene.setInput(input);
        scene.game = this;
        scene.time = new YldTime(this);
        scene.setMasterEntity(new Entity("MasterEntity", scene, null));
        scenes.add(scene);
    }

    /**
     * Getter for the scenes list.
     *
     * @return The scenes list.
     */
    public ArrayList<YldScene> getScenes() {
        return scenes;
    }

    /**
     * Setter for the scenes list.
     */
    public void setScenes(ArrayList<YldScene> scenes) {
        this.scenes = scenes;
    }

    /**
     * Getter for the actual scene.
     *
     * @return The actual scene.
     */
    public YldScene getScene() {
        return scene;
    }

    /**
     * Setter for the actual scene.
     */
    public void setScene(YldScene scene) {
        this.scene = scene;
        scene.setGraphics(new YldGraphics(scene.getMasterEntity(), this));
        scene.defaultSystems();
    }

    public void loadSceneAssets(YldScene scene, YldProgressScene progressScene) {
        try {
            URL url = YldGame.class.getResource("/" + scene.getClass().getSimpleName());
            File file = null;
            if (url != null) file = new File(url.toURI());
            scene.setAssets(new Assets(file, scene.getClass(), progressScene, this));
        } catch (URISyntaxException e) {
            Yld.throwException(new RuntimeException(e));
        }
    }

    /**
     * This function returns the window object.
     *
     * @return The window object.
     */
    public SampleWindow getWindow() {
        return window;
    }

    public final Engine getEngine(MultiThread multiThread, EngineStop engineStop) {
        if (multiThread == MultiThread.DEFAULT) {
            return getHandler().getDefaultConcurrentEngine();
        } else if (multiThread == MultiThread.ON_GAME_THREAD) {
            return getHandler();
        } else if (multiThread == MultiThread.EXCLUSIVE) {
            Engine engine = new Engine(null);
            engine.getThread().start();
            engine.setStop(engineStop);
            return engine;
        }
        Yld.throwException(new IllegalArgumentException(multiThread.name()));
        return null;
    }

    /**
     * This function sets the window variable to the window that is passed in.
     *
     * @param window The window value to set.
     */
    public void setWindow(SampleWindow window) {
        this.window = window;
    }

    @Deprecated
    public void setScene(String name) {
        YldScene scene = null;
        int i = 0;
        while (i < scenes.size()) {
            if (scenes.get(i).getClass().getSimpleName().hashCode() == name.hashCode()) {
                if (scenes.get(i).getClass().getSimpleName().equals(name)) {
                    scene = scenes.get(i);
                    break;
                }
            }
            i++;
        }
        if (scene == null)
            throw new NullPointerException("none scene with name: " + name);
        setScene(scene);
    }

    /**
     * It loads a texture into memory and then calls the onLoad() function of the texture
     *
     * @param texture The texture to load.
     * @return The texture that was loaded.
     */
    public Texture loadTexture(Texture texture) {
        handler.getRenderMaster().loadTexture(texture);
        texture.onLoad();
        return texture;
    }

    /**
     * It prints the window into a texture
     *
     * @param pos  The middle position of the crop of the window.
     * @param size The size of the crop to be created.
     * @return A texture.
     */
    public Texture print(Vector2 pos, Vector2 size) {
        if (getHandler().getWindowPrint() == null)
            Yld.throwException(new MissingWindowPrintException());
        Texture tex = getHandler().getWindowPrint().print(pos, size);
        tex.onLoad();
        return tex;
    }

    /**
     * It duplicates a texture
     *
     * @param texture The texture to duplicate.
     * @return A new texture object.
     */
    public Texture duplicateTexture(Texture texture) {
        return game.getHandler().getRenderMaster().duplicate(texture);
    }

    /**
     * Clears the specified texture.
     *
     * @param texture The texture to clear.
     * @return The texture that was cleared.
     */
    public Texture clearTexture(Texture texture) {
        game.getHandler().getRenderMaster().clearTexture(texture);
        return texture;
    }

    /**
     * Unloads a texture from the memory.
     *
     * @param texture The texture to unload.
     */
    public void unloadTexture(Texture texture) {
        handler.getRenderMaster().unloadTexture(texture);
    }

    /**
     * It cuts a texture into a new texture
     *
     * @param texture The texture to cut from
     * @param pos     The position of the texture to cut from.
     * @param size    The size of the texture you want to cut out.
     * @return A Texture object.
     */
    public Texture cutTexture(Texture texture, Vector2 pos, Vector2 size) {
        return handler.getRenderMaster().cutTexture(texture, (int) pos.x, (int) pos.y, (int) size.x, (int) size.y);
    }

    /**
     * This function takes a texture and returns a new texture that is the same as the original texture, but scaled to the
     * specified size.
     *
     * @param texture The texture to scale
     * @param size    The size of the texture to be scaled.
     * @return A texture.
     */
    public Texture scaleTexture(Texture texture, Vector2 size) {
        return handler.getRenderMaster().scaleTexture(texture, (int) size.x, (int) size.y);
    }

    /**
     * It returns a 2D array of colors that represent the texture
     *
     * @param texture The texture to get the colors from.
     * @return A 2D array of colors.
     */
    public Color[][] getTextureColors(Texture texture) {
        return handler.getRenderMaster().getTextureColors(texture);
    }

    /**
     * Sets the colors of the pixels of the given texture to the given colors.
     *
     * @param texture The texture to set the pixels of.
     * @param colors  A 2D array of colors. The first dimension is the x coordinate, the second is the y coordinate.
     */
    public void setTexturePixels(Texture texture, Color[][] colors) {
        handler.getRenderMaster().setTextureColors(texture, colors);
    }

    /**
     * Loads a font from a file, and if the file is marked to be flushed after load, flush it.
     *
     * @param fontName The name of the font.
     * @param size     The size of the font.
     * @param format   0 = Vector, 1 = Bitmap.
     * @param fontFile The file that contains the font.
     */
    public void loadFont(String fontName, float size, int format, RelativeFile fontFile) {
        loadFont(fontName, size, size, format, fontFile);
        if (fontFile.isFlushAfterLoad())
            fontFile.flush();
    }


    /**
     * Loads a font from a file, and if the file is marked to be flushed after load, flush it.
     *
     * @param fontName   The name of the font.
     * @param size       The size of the font to be loaded.
     * @param sizeToLoad The size of the font to load.
     * @param format     The format of the font file. This can be one of the following:
     * @param fontFile   The file to load the font from.
     */
    public void loadFont(String fontName, float size, float sizeToLoad, int format, RelativeFile fontFile) {
        handler.getRenderMaster().loadFont(fontName, size, sizeToLoad, format, fontFile);
        if (fontFile.isFlushAfterLoad())
            fontFile.flush();
    }


    /**
     * Loads a font with the given name, size, and style
     *
     * @param fontName The name of the font.
     * @param size     The size of the font.
     * @param style    Font.PLAIN, Font.BOLD, Font.ITALIC, Font.BOLD + Font.ITALIC
     */
    public void loadFont(String fontName, float size, int style) {
        handler.getRenderMaster().loadFont(fontName, fontName, size, style);
    }

    /**
     * Unloads all textures from the memory.
     */
    public void unloadAllTextures() {
        handler.getRenderMaster().unloadAllTextures();
    }

    /**
     * Unloads all audio players
     */
    public void unloadAllAudioPlayers() {
        handler.getRenderMaster().unloadAllPlayers();
    }

    /**
     * Loads a font with the given name, size, and style.
     *
     * @param fontName The name of the font.
     * @param saveName The font name to be saved.
     * @param size     The size of the font.
     * @param style    Font.PLAIN, Font.BOLD, Font.ITALIC, Font.BOLD + Font.ITALIC
     */
    public void loadFont(String saveName, String fontName, float size, int style) {
        handler.getRenderMaster().loadFont(saveName, fontName, size, style);
    }

    /**
     * Unloads a font from the font cache.
     *
     * @param fontName The name of the font to unload.
     */
    public void unloadFont(String fontName) {
        handler.getRenderMaster().unloadFont(fontName);
    }

    /**
     * Method to set a scene from the type specified as the actual scene.
     *
     * @param type The scene type.
     * @param how  What to do with last scene.
     */
    public <T extends YldScene> void setScene(Class<T> type, ChangeScene how) {
        YldScene scene = getScene(type);
        if (scene == null) {
            try {
                addScene(type.getConstructor().newInstance());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                Yld.throwException(new RuntimeException(e));
            }
            setScene(type, how);
        }
        if (scene == null)
            throw new NullPointerException("none scene with name: " + type.getName());
        else {
            if (getScene() != null) {
                if (!(getScene() instanceof YldProgressScene)) {
                    if (configuration.unloadAllTexturesWhenChangeScene)
                        unloadAllTextures();
                    if (configuration.unloadAllAudioPlayersWhenChangeScene)
                        unloadAllAudioPlayers();
                }
                if (how == ChangeScene.DESTROY_LAST) {
                    getScene().destroyScene();
                    for (YldSystem system : getScene().getSystems()) {
                        system.destroy();
                    }
                    getScene().setMasterEntity(new Entity("MasterEntity", getScene(), null));
                    getScene().setFrames(0);
                    getScene().setCallStart(true);
                }
            }
        }
        setScene(scene);
        if (!scene.isHadProgressScene())
            loadSceneAssets(scene, null);
        scene.setHadProgressScene(false);
        Yld.debug(() -> Yld.log("Changed to scene: " + type.getSimpleName()));
    }

    /**
     * If the hashcode and name of the class of the scene at index i is equal to the hashcode and name of the class of the
     * type parameter, return the scene at index i.
     *
     * @param type The class of the scene you want to get.
     * @return The scene that matches the type.
     */
    public YldScene getScene(Class<? extends YldScene> type) {
        int i = 0;
        while (i < scenes.size()) {
            if (scenes.get(i).getClass().getName().hashCode() == type.getName().hashCode() && scenes.get(i).getClass().getName().equals(type.getName())) {
                return scenes.get(i);
            }
            i++;
        }
        return null;
    }

    /**
     * Set the scene to the given type, and use the given progress scene to load it.
     *
     * @param type          The scene to change to.
     * @param progressScene The scene that will be displayed while the assets are being loaded.
     * @param how           What to do with last scene.
     */
    public <T extends YldScene, P extends YldProgressScene> void setScene(Class<T> type, Class<P> progressScene, ChangeScene how) {
        if (configuration.unloadAllTexturesWhenChangeToProcessScene)
            unloadAllTextures();
        setScene(progressScene, how);
        YldProgressScene ps = (YldProgressScene) scene;
        ps.setToChangeScene(type);
        ps.setToChangeScene(type);
        YldScene toChangeScene = getScene(type);
        toChangeScene.setHadProgressScene(true);
        concurrent(() -> {
            loadSceneAssets(toChangeScene, ps);
            ps.change();
        }, MultiThread.EXCLUSIVE);
    }

    /**
     * Set the scene to the given type, using the given progress scene, and destroy the last scene.
     *
     * @param type          The class of the scene you want to change to.
     * @param progressScene The class of the progress scene to be displayed while the scene is loading.
     */
    public <T extends YldScene, P extends YldProgressScene> void setScene(Class<T> type, Class<P> progressScene) {
        setScene(type, progressScene, ChangeScene.DESTROY_LAST);
    }

    /**
     * Method to instantiate a scene and set it as the actual scene, destroying the last one.
     *
     * @param type The scene type to be instantiated.
     */
    public <T extends YldScene> void setScene(Class<T> type) {
        setScene(type, ChangeScene.DESTROY_LAST);
    }

    /**
     * Setter for the configuration variable.
     */
    public void setConfiguration(GameConfiguration configuration) {
        this.configuration = configuration;
    }

    /**
     * This function sets the value of the variable started to the value of the parameter started.
     *
     * @param started This is a boolean value that indicates whether the game has started or not.
     */
    public void setStarted(boolean started) {
        this.started = started;
    }

    /**
     * This function returns the yieldLogo variable.
     *
     * @return The yieldLogo variable is being returned.
     */
    public Texture getYieldLogo() {
        return yieldLogo;
    }
}
