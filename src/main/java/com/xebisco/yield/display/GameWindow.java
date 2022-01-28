package com.xebisco.yield.display;

import com.xebisco.yield.GameInfo;

import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame {

    private final GameInfo gameInfo;

    public GameWindow(GameInfo gameInfo) {
        this.gameInfo = gameInfo;
    }

    public void create() {
        this.setSize(new Dimension(gameInfo.config.width, gameInfo.config.height));
        this.setUndecorated(gameInfo.config.undecorated);
        this.setVisible(true);
    }

    public GameInfo getGameInfo() {
        return gameInfo;
    }
}
