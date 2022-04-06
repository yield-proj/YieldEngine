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
import com.xebisco.yield.engine.YldWindow;
import com.xebisco.yield.exceptions.AlreadyStartedException;
import com.xebisco.yield.extensions.YieldOverlay;
import com.xebisco.yield.input.YldInput;
import com.xebisco.yield.slick.SlickGame;
import com.xebisco.yield.utils.ChangeScene;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

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
    private YldWindow window;
    private GameHandler handler;
    private final ArrayList<YldExtension> extensions = new ArrayList<>();
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
    private SlickGame slickGame;
    private AppGameContainer slickApp;
    /**
     * This variable tells if the last YldGame instance is hardware accelerated.
     *
     * @since 4_1.1
     */
    public static boolean lwjgl;

    @Override
    public void start()
    {

    }

    /**
     * Sets the game window (YldWindow if CPU, Display if GPU) to fullscreen or windowed.
     *
     * @param fullscreen To set to fullscreen or windowed.
     * @since 4_1.1
     */
    public void setFullscreen(boolean fullscreen)
    {
        if (window != null)
        {
            if (!fullscreen)
                window.toWindow(getConfiguration());
            else
                window.toFullscreen(getConfiguration());
        }
        else
        {
            try
            {
                if (fullscreen)
                    Display.setDisplayModeAndFullscreen(Display.getDesktopDisplayMode());
                else
                {
                    Display.setDisplayMode(new DisplayMode(
                            configuration.width, configuration.height));
                    Display.setFullscreen(false);
                }
                slickApp.reinit();
            } catch (LWJGLException | SlickException e)
            {
                e.printStackTrace();
            }
        }
    }

    public static void launch(YldGame game)
    {
        launch(game, GameConfiguration.iniConfig(new Ini(YldGame.class.getResourceAsStream("/yieldconfig/game.ini"))));
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
        Locale.setDefault(Locale.US);
        if (View.getActView() == null)
        {
            new View(1280, 720);
        }
        if (configuration.hardwareAcceleration)
        {
            Yld.log("WARNING: hardware acceleration is a experimental feature!");

            if (configuration.autoNativesPath)
            {
                String lwjglNatives = "";
                try
                {
                    switch (LWJGLUtil.getPlatform())
                    {
                        case LWJGLUtil.PLATFORM_WINDOWS:
                            lwjglNatives = new File(Objects.requireNonNull(YldGame.class.getResource("/com/xebisco/yield/native/windows")).toURI()).getAbsolutePath();
                            break;
                        case LWJGLUtil.PLATFORM_MACOSX:
                            lwjglNatives = new File(Objects.requireNonNull(YldGame.class.getResource("/com/xebisco/yield/native/macosx")).toURI()).getAbsolutePath();
                            break;
                        case LWJGLUtil.PLATFORM_LINUX:
                            lwjglNatives = new File(Objects.requireNonNull(YldGame.class.getResource("/com/xebisco/yield/native/linux")).toURI()).getAbsolutePath();
                            break;
                    }
                } catch (URISyntaxException ignore)
                {
                }
                System.setProperty("org.lwjgl.librarypath", lwjglNatives);
                System.setProperty("net.java.games.input.librarypath", lwjglNatives);
            }
        }
        game.game = game;
        if (game.started)
            throw new AlreadyStartedException();
        game.started = true;
        game.configuration = configuration;
        if (configuration.title == null)
            configuration.title = game.getClass().getSimpleName() + " (Yield " + Yld.VERSION + ")";

        game.handler = new GameHandler(game);
        game.setGraphics(new YldGraphics());
        if (!configuration.hardwareAcceleration)
        {
            if (configuration.startYldWindow)
            {
                game.window = new YldWindow();
                game.window.getFrame().setTitle(configuration.title);
                game.window.getWindowG().setHandler(game.handler);
                if (!configuration.fullscreen)
                    game.window.toWindow(configuration);
                else
                    game.window.toFullscreen(configuration);
                game.window.setGraphics(game.getGraphics());
            }
        }
        else
        {
            lwjgl = true;
            try
            {
                game.slickApp = new AppGameContainer(game.slickGame = new SlickGame(game), game.configuration.width, game.configuration.height, game.configuration.fullscreen);
            } catch (SlickException e)
            {
                e.printStackTrace();
            }
        }
        game.addExtension(new YieldOverlay());
        game.addScene(game);
        game.setScene(game);
        if (game.slickApp != null)
        {
            try
            {
                game.slickApp.start();
                game.input = new YldInput(null, game.slickApp);
            } catch (SlickException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            game.input = new YldInput(game.window, null);
            game.handler.getThread().start();
        }
    }

    public final void updateScene(float delta)
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
        scene.process(delta);
    }

    /**
     * Getter for the window variable.
     *
     * @return The window variable (null if in GPU Mode).
     */
    public YldWindow getWindow()
    {
        return window;
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

    /**
     * Adds and sets the passed extension to the extensions list.
     *
     * @param extension The extension to be added.
     */
    public void addExtension(YldExtension extension)
    {
        extension.create();
        extension.setGame(this);
        extensions.add(extension);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    /**
     * Search for all the extensions in the extensions list.
     *
     * @param type The class type of the extension that's being searched.
     * @return The extension found (null if not found)
     */
    public <T extends YldExtension> YldExtension getExtension(Class<T> type)
    {
        YldExtension extension = null;
        for (YldExtension e : extensions)
        {
            if (e.getClass().getName().hashCode() == type.getName().hashCode())
            {
                if (e.getClass().getName().equals(type.getName()))
                {
                    extension = e;
                    break;
                }
            }
        }
        return extension;
    }

    /**
     * Removes an extension of the given type in the extensions list.
     *
     * @param type The extension type to be removed.
     */
    public <T extends YldExtension> void removeExtension(Class<T> type)
    {
        for (YldExtension e : extensions)
        {
            if (e.getClass().getName().hashCode() == type.getName().hashCode())
            {
                if (e.getClass().getName().equals(type.getName()))
                {
                    extensions.remove(e);
                    break;
                }
            }
        }
    }

    /**
     * Getter for the extensions list.
     *
     * @return The extensions list.
     */
    public ArrayList<YldExtension> getExtensions()
    {
        return extensions;
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

    /**
     * Setter for the window variable.
     */
    public void setWindow(YldWindow window)
    {
        this.window = window;
    }

    /**
     * Getter for the slickGame variable.
     *
     * @return The slickGame variable (null if in CPU mode)
     */
    public SlickGame getSlickGame()
    {
        return slickGame;
    }

    /**
     * Setter for the slickGame variable.
     */
    public void setSlickGame(SlickGame slickGame)
    {
        this.slickGame = slickGame;
    }

    /**
     * Getter for the slickApp variable.
     *
     * @return The slickApp variable (null if in CPU mode)
     */
    public AppGameContainer getSlickApp()
    {
        return slickApp;
    }

    /**
     * Setter for the slickApp variable.
     */
    public void setSlickApp(AppGameContainer slickApp)
    {
        this.slickApp = slickApp;
    }
}
