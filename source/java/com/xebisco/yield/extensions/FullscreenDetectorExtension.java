package com.xebisco.yield.extensions;

import com.xebisco.yield.YldExtension;

import java.awt.event.KeyEvent;

public class FullscreenDetectorExtension extends YldExtension {

    private boolean fullscreen;

    private int fullscreenKey = KeyEvent.VK_F11;
    private boolean importConfig = true;

    @Override
    public void update(float delta) {
        if(importConfig) {
            importConfig = false;
            fullscreen = game.getConfiguration().fullscreen;
        }
        if(game.getInput().justPressed(fullscreenKey)) {
            setFullscreen(!fullscreen);
        }
    }

    public int getFullscreenKey() {
        return fullscreenKey;
    }

    public void setFullscreenKey(int fullscreenKey) {
        this.fullscreenKey = fullscreenKey;
    }

    public boolean isFullscreen() {
        return fullscreen;
    }

    public void setFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
        if(fullscreen) {
            game.getWindow().toFullscreen(game.getConfiguration());
        } else {
            game.getWindow().toWindow(game.getConfiguration());
        }
    }
}