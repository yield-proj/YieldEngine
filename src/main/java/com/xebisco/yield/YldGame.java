package com.xebisco.yield;

import com.xebisco.yield.engine.GameHandler;
import com.xebisco.yield.engine.YldWindow;
import com.xebisco.yield.exceptions.AlreadyStartedException;
import com.xebisco.yield.extensions.YieldOverlay;
import com.xebisco.yield.input.YldInput;
import com.xebisco.yield.utils.ChangeScene;

import java.util.ArrayList;
import java.util.Locale;

public class YldGame extends YldScene
{

    private GameConfiguration configuration;
    private boolean started = false;
    private YldWindow window;
    private GameHandler handler;
    private final ArrayList<YldExtension> extensions = new ArrayList<>();
    protected ArrayList<YldScene> scenes = new ArrayList<>();
    protected YldScene scene;

    @Override
    public final void start()
    {

    }

    public static void launch(YldGame game, GameConfiguration configuration)
    {
        Locale.setDefault(Locale.US);
        game.game = game;
        if (game.started)
            throw new AlreadyStartedException();
        game.started = true;
        game.configuration = configuration;
        game.window = new YldWindow();
        if (configuration.title == null)
            game.window.getFrame().setTitle(game.getClass().getSimpleName() + " (Yield " + Yld.VERSION + ")");
        else
            game.window.getFrame().setTitle(configuration.title);
        if (!configuration.fullscreen)
            game.window.toWindow(configuration);
        else
            game.window.toFullscreen(configuration);
        game.handler = new GameHandler(game);
        game.window.getWindowG().setHandler(game.handler);
        game.setGraphics(game.window.getGraphics());
        game.input = new YldInput(game.window);
        game.addExtension(new YieldOverlay());
        game.addScene(game);
        game.setScene(game);
        game.handler.getThread().start();
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

    public YldWindow getWindow()
    {
        return window;
    }

    public boolean isStarted()
    {
        return started;
    }

    public GameConfiguration getConfiguration()
    {
        return configuration;
    }

    public GameHandler getHandler()
    {
        return handler;
    }

    public void setHandler(GameHandler handler)
    {
        this.handler = handler;
    }

    public void addExtension(YldExtension extension)
    {
        extension.create();
        extension.setGame(this);
        extensions.add(extension);
    }

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

    public ArrayList<YldExtension> getExtensions()
    {
        return extensions;
    }

    public void addScene(YldScene scene)
    {
        scene.setInput(input);
        scene.game = this;
        scene.time = new YldTime(handler);
        scene.setMasterEntity(new Entity("MasterEntity", scene, null));
        scenes.add(scene);
    }

    public ArrayList<YldScene> getScenes()
    {
        return scenes;
    }

    public void setScenes(ArrayList<YldScene> scenes)
    {
        this.scenes = scenes;
    }

    public YldScene getScene()
    {
        return scene;
    }

    public void setScene(YldScene scene)
    {
        scene.setCallStart(true);
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
                    if(how == ChangeScene.DESTROY_LAST && getScene() != null)
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
    public <T extends YldScene> void setScene(Class<T> type)
    {
        setScene(type, ChangeScene.DESTROY_LAST);
    }
}
