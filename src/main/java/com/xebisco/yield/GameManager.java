package com.xebisco.yield;

import com.xebisco.yield.display.GameWindow;

public class GameManager {

    public static void launch(Game game, GameConfiguration config) {
        GameInfo gameInfo = new GameInfo(config, game);
        GameWindow window = new GameWindow(gameInfo);

    }

}
