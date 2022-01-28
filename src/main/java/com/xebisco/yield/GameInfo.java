package com.xebisco.yield;

public class GameInfo {
    public final GameConfiguration config;
    public final Game game;
    public GameInfo(GameConfiguration config, Game game) {
        this.config = config;
        this.game = game;
    }
}
