package com.xebisco.yield;

import com.xebisco.yield.YldB;
import com.xebisco.yield.YldGame;

import java.awt.*;

public class YldExtension implements YldB {
    protected YldGame game;
    @Override
    public void create() {

    }

    @Override
    public void update(float delta) {

    }

    public void render(Graphics graphics) {

    }

    public YldGame getGame() {
        return game;
    }

    public void setGame(YldGame game) {
        this.game = game;
    }
}
