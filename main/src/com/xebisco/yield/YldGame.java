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
import com.xebisco.yield.engine.GameHandler;
import com.xebisco.yield.engine.YldEngineAction;
import com.xebisco.yield.systems.MiddlePointSystem;
import com.xebisco.yield.systems.PhysicsSystem;
import com.xebisco.yield.systems.YldTimeSystem;

import java.io.InputStream;
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
        game.setConfiguration(configuration);
        game.addScene(game);
        game.setScene(game);
        game.setInput(new YldInput(game));
        new GameHandler(game);
        game.loadTexture(configuration.icon);
        game.setWindow(game.getHandler().getRenderMaster().initWindow(game.getConfiguration()));
        game.yieldLogo = new Texture("com/xebisco/yield/assets/yieldlogo.png", TexType.NATIVE);
        game.loadTexture(game.yieldLogo);
        game.loadFont("arial", 30, 0);
        game.getHandler().getThread().start();
    }

    public final void updateScene(float delta, SampleGraphics graphics) {
        if (scene.getFrames() == 0) {
            for(YldSystem system : scene.getSystems()) {
                if(system instanceof SystemCreateMethod) {
                    ((SystemCreateMethod) system).create();
                }
            }
            scene.create();
        }
        scene.setFrames(scene.getFrames() + 1);
        if (scene.isCallStart()) {
            scene.setCallStart(false);
            scene.start();
        }
        scene.update(delta);
        scene.process(delta, graphics);
    }

    public Engine processFilters(Texture texture, int fps, boolean stopAfterSame) {
        /*int enginesSize = division * 6;
        Engine[] engines = new Engine[enginesSize];
        Yld.log("w - " + texture.getWidth() + ", h - " + texture.getHeight());
        int i = 0;
        for (int x = 0; x < enginesSize / 3; x++) {
            int xm = texture.getWidth() / (enginesSize / 3) * x;
            for (int y = 0; y < 3; y++) {
                int ym = texture.getHeight() / (enginesSize / (3 + division - 1)) * y;
                Yld.log(x + ", " + y);
                engines[i] = new Engine(null);
                engines[i].getThread().create();
                engines[i].setTargetTime(1000 / fps);
                engines[i].getTodoList().add(new YldEngineAction(() -> texture.processFilters(xm, xm + texture.getWidth() / (enginesSize / 3) - 1, ym, ym + texture.getHeight() / (enginesSize / (3 + division - 1)) - 1), 0, true, Yld.RAND.nextLong()));
                i++;
            }
        }*/
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

    public Texture overlayTexture(Texture tex1, Texture tex2, Vector2 pos1, Vector2 pos2) {
        return handler.getRenderMaster().overlayTexture(tex1, tex2, pos1, pos2);
    }

    public Texture overlayTexture(Texture tex1, Texture tex2, Vector2 pos) {
        return overlayTexture(tex1, tex2, new Vector2(), pos);
    }

    public Texture overlayTexture(Texture tex1, Texture tex2) {
        return overlayTexture(tex1, tex2, new Vector2(), new Vector2());
    }

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

    public Color predominantColor(Texture texture) {
        return predominantColor(getTextureColors(texture));
    }

    private Color[][] processColorsSTD;

    private boolean checkTexToSTDColors(Texture tex) {
        boolean toReturn = false;
        if (processColorsSTD != null) {
            toReturn = equalsColors(getTextureColors(tex), processColorsSTD);
        }
        processColorsSTD = getTextureColors(tex);
        return toReturn;
    }

    public boolean equalsTexture(Texture texture1, Texture texture2) {
        return equalsColors(getTextureColors(texture1), getTextureColors(texture2));
    }

    public boolean equalsColors(Color[][] colors1, Color[][] colors2) {
        return Arrays.deepHashCode(colors1) == Arrays.deepHashCode(colors2);
    }

    public Engine processFilters(Texture texture) {
        return processFilters(texture, (int) time.getTargetFPS(), true);
    }

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

    public SampleWindow getWindow() {
        return window;
    }

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

    public Texture loadTexture(Texture texture) {
        handler.getRenderMaster().loadTexture(texture);
        return texture;
    }

    public Texture duplicateTexture(Texture texture) {
        return game.getHandler().getRenderMaster().duplicate(texture);
    }

    public Texture clearTexture(Texture texture) {
        game.getHandler().getRenderMaster().clearTexture(texture);
        return texture;
    }

    public void unloadTexture(Texture texture) {
        handler.getRenderMaster().unloadTexture(texture);
    }

    public Texture cutTexture(Texture texture, Vector2 pos, Vector2 size) {
        return handler.getRenderMaster().cutTexture(texture, (int) pos.x, (int) pos.y, (int) size.x, (int) size.y);
    }

    public Texture scaleTexture(Texture texture, Vector2 size) {
        return handler.getRenderMaster().scaleTexture(texture, (int) size.x, (int) size.y);
    }

    public Color[][] getTextureColors(Texture texture) {
        return handler.getRenderMaster().getTextureColors(texture);
    }

    public void setTexturePixels(Texture texture, Color[][] colors) {
        handler.getRenderMaster().setTextureColors(texture, colors);
    }

    public void loadFont(String fontName, float size, int format, RelativeFile fontFile) {
        loadFont(fontName, size, size, format, fontFile.getInputStream());
        fontFile.flush();
    }

    public void loadFont(String fontName, float size, float sizeToLoad, int format, RelativeFile fontFile) {
        handler.getRenderMaster().loadFont(fontName, size, sizeToLoad, format, fontFile.getInputStream());
        fontFile.flush();
    }

    public void loadFont(String fontName, float size, int format, InputStream inputStream) {
        loadFont(fontName, size, size, format, inputStream);
    }

    public void loadFont(String fontName, float size, float sizeToLoad, int format, InputStream inputStream) {
        handler.getRenderMaster().loadFont(fontName, size, sizeToLoad, format, inputStream);
    }

    public void loadFont(String fontName, int size, int style) {
        handler.getRenderMaster().loadFont(fontName, fontName, size, style);
    }

    public void loadFont(String saveName, String fontName, int size, int style) {
        handler.getRenderMaster().loadFont(saveName, fontName, size, style);
    }

    public void unloadFont(String fontName) {
        handler.getRenderMaster().unloadFont(fontName);
    }

    /**
     * Method to instantiate a scene and set it as the actual scene.
     *
     * @param type The scene type to be instantiated.
     * @param how  What to do with last scene.
     */
    public <T extends YldScene> void setScene(Class<T> type, ChangeScene how) {
        YldScene scene = null;
        int i = 0;
        while (i < scenes.size()) {
            if (scenes.get(i).getClass().getName().hashCode() == type.getName().hashCode()) {
                if (scenes.get(i).getClass().getName().equals(type.getName())) {
                    if (getScene() != null) {
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
                    scene = scenes.get(i);
                    break;
                }
            }
            i++;
        }
        if (scene == null)
            throw new NullPointerException("none scene with name: " + type.getName());
        setScene(scene);
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

    public void setStarted(boolean started) {
        this.started = started;
    }

    public Texture getYieldLogo() {
        return yieldLogo;
    }
}
