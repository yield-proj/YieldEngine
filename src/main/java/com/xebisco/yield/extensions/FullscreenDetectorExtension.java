package com.xebisco.yield.extensions;

import com.xebisco.yield.YldExtension;

import java.awt.event.KeyEvent;

public class FullscreenDetectorExtension extends YldExtension {

    private boolean fullscreen;

    private int frames, fullscreenKey = KeyEvent.VK_F11;

    @Override
    public void update(float delta) {
        frames++;
        if(frames == 1 && fullscreen) {
            setFullscreen(true);
        }
        if(game.getInput().justPressed(fullscreenKey)) {
            setFullscreen(!fullscreen);
        }
    }

    public int getFrames() {
        return frames;
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