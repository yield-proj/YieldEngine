package com.xebisco.yield;

import com.xebisco.yield.config.GameConfiguration;
import com.xebisco.yield.engine.GameHandler;
import com.xebisco.yield.engine.YldWindow;
import com.xebisco.yield.exception.AlreadyStartedException;
import com.xebisco.yield.input.YldInput;

import java.util.ArrayList;

public class YldGame implements YldB {

    private GameConfiguration configuration;
    private boolean started = false;
    private YldWindow window;
    private GameHandler handler;
    private final ArrayList<YldExtension> extensions = new ArrayList<>();
    private int frames;
    protected YldGraphics graphics;
    protected YldInput input;

    @Override
    public void create() {

    }

    @Override
    public void update(float delta) {

    }

    public static void launch(YldGame game, GameConfiguration configuration) {
        if (game.started)
            throw new AlreadyStartedException();
        game.started = true;
        game.configuration = configuration;
        game.window = new YldWindow();
        if (configuration.title == null)
            game.window.getFrame().setTitle(game.getClass().getSimpleName() + " (Yield " + Yld.VERSION + ")");
        if (!configuration.fullscreen)
            game.window.toWindow(configuration);
        else
            game.window.toFullscreen(configuration);
        game.handler = new GameHandler(game);
        game.window.getWindowG().setHandler(game.handler);
        game.setGraphics(game.window.getGraphics());
        YldInput input = new YldInput();
        game.window.getFrame().addKeyListener(input);
        game.input = input;
        game.handler.getGameThread().start();
    }

    public YldWindow getWindow() {
        return window;
    }

    public boolean isStarted() {
        return started;
    }

    public GameConfiguration getConfiguration() {
        return configuration;
    }

    public GameHandler getHandler() {
        return handler;
    }

    public void setHandler(GameHandler handler) {
        this.handler = handler;
    }

    public int getFrames() {
        return frames;
    }

    public void setFrames(int frames) {
        this.frames = frames;
    }

    public void addExtension(YldExtension extension) {
        extension.create();
        extension.setGame(this);
        extensions.add(extension);
    }

    public ArrayList<YldExtension> getExtensions() {
        return extensions;
    }

    public YldGraphics getGraphics() {
        return graphics;
    }

    public void setGraphics(YldGraphics graphics) {
        this.graphics = graphics;
    }

    public YldInput getInput() {
        return input;
    }

    public void setInput(YldInput input) {
        this.input = input;
    }
}
