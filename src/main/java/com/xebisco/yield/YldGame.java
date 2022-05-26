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

import com.xebisco.yield.engine.GameHandler;
import com.xebisco.yield.utils.ChangeScene;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;

/**
 * This class is the starting point for every Yield Game, it contains all the objects of the game.
 *
 * @author Xebisco
 * @since 4_alpha1
 */
public class YldGame extends YldScene
{
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
    public void start()
    {

    }

    /**
     * This is the method that will start an YldGame instance receiving a GameConfiguration variable, it will set all the game based in the GameConfiguration instance.
     *
     * @param game          The YldGame to be created.
     * @param configuration The YldGame instance of the GameConfiguration class.
     * @since 4_alpha1
     */
    public static void launch(YldGame game, GameConfiguration configuration)
    {
        game.setConfiguration(configuration);
        game.addScene(game);
        game.setScene(game);
        game.setInput(new YldInput(game));
        game.setHandler(new GameHandler(game));
        game.loadTexture(game.yieldLogo = new Texture("com/xebisco/yield/assets/yieldlogo.png"));
        game.loadFont("arial", 30, 0);
        game.getHandler().getThread().start();
    }

    public final void updateScene(float delta, SampleGraphics graphics)
    {
        if (scene.getFrames() == 0)
            scene.create();
        scene.setFrames(scene.getFrames() + 1);
        if (scene.isCallStart())
        {
            scene.start();
            scene.setCallStart(false);
        }
        scene.update(delta);
        scene.process(delta, graphics);
    }

    public boolean isStarted()
    {
        return started;
    }

    /**
     * Getter for the configuration variable.
     *
     * @return The configuration variable.
     */
    public GameConfiguration getConfiguration()
    {
        return configuration;
    }

    /**
     * Getter for the handler variable.
     *
     * @return The handler variable.
     */
    public GameHandler getHandler()
    {
        return handler;
    }

    /**
     * Setter for the handler variable.
     */
    public void setHandler(GameHandler handler)
    {
        this.handler = handler;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    /**
     * Adds and sets the passed scene to the scenes list.
     *
     * @param scene The scene to be added.
     */
    public void addScene(YldScene scene)
    {
        scene.setInput(input);
        scene.game = this;
        scene.time = new YldTime(this);
        scene.setMasterEntity(new Entity("MasterEntity", scene, null));
        scene.setGraphics(new YldGraphics(scene.getMasterEntity()));
        scenes.add(scene);
    }

    /**
     * Getter for the scenes list.
     *
     * @return The scenes list.
     */
    public ArrayList<YldScene> getScenes()
    {
        return scenes;
    }

    /**
     * Setter for the scenes list.
     */
    public void setScenes(ArrayList<YldScene> scenes)
    {
        this.scenes = scenes;
    }

    /**
     * Getter for the actual scene.
     *
     * @return The actual scene.
     */
    public YldScene getScene()
    {
        return scene;
    }

    /**
     * Setter for the actual scene.
     */
    public void setScene(YldScene scene)
    {
        this.scene = scene;
    }

    public SampleWindow getWindow() {
        return window;
    }

    public void setWindow(SampleWindow window) {
        this.window = window;
    }

    @Deprecated
    public void setScene(String name)
    {
        YldScene scene = null;
        int i = 0;
        while (i < scenes.size())
        {
            if (scenes.get(i).getClass().getSimpleName().hashCode() == name.hashCode())
            {
                if (scenes.get(i).getClass().getSimpleName().equals(name))
                {
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

    public void loadTexture(Texture texture) {
        handler.getRenderMaster().loadTexture(texture);
    }

    public void unloadTexture(Texture texture) {
        handler.getRenderMaster().unloadTexture(texture);
    }

    public void loadFont(String fontName, int format, InputStream inputStream) {
        handler.getRenderMaster().loadFont(fontName, format, inputStream);
    }

    public void loadFont(String fontName, int size, int style) {
        handler.getRenderMaster().loadFont(fontName, size, style);
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
    public <T extends YldScene> void setScene(Class<T> type, ChangeScene how)
    {
        YldScene scene = null;
        int i = 0;
        while (i < scenes.size())
        {
            if (scenes.get(i).getClass().getName().hashCode() == type.getName().hashCode())
            {
                if (scenes.get(i).getClass().getName().equals(type.getName()))
                {
                    if (how == ChangeScene.DESTROY_LAST && getScene() != null)
                        getScene().destroyScene();
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
    public <T extends YldScene> void setScene(Class<T> type)
    {
        setScene(type, ChangeScene.DESTROY_LAST);
    }

    /**
     * Setter for the configuration variable.
     */
    public void setConfiguration(GameConfiguration configuration)
    {
        this.configuration = configuration;
    }

    public void setStarted(boolean started)
    {
        this.started = started;
    }

    public Texture getYieldLogo() {
        return yieldLogo;
    }
}
