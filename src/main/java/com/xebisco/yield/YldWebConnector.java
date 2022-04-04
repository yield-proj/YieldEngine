/*
 * Copyright [2022] [Xebisco]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xebisco.yield;

import com.xebisco.yield.engine.YldWindow;
import com.xebisco.yield.graphics.AWTGraphics;
import io.github.synonware.sini4j.Ini;
import io.github.synonware.sini4j.IniSection;

import java.applet.Applet;
import java.awt.*;

public class YldWebConnector extends Applet
{
    private static YldGame game;
    private final AWTGraphics sampleGraphics = new AWTGraphics();

    @Override
    public void init()
    {
        Ini ini = new Ini(YldWebConnector.class.getResourceAsStream("/yieldconfig/game.ini"));
        IniSection gameSection = ini.getSection("YldGame");
        String cName = gameSection.getString("path");
        try
        {
            game = (YldGame) Class.forName(cName).newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e)
        {
            e.printStackTrace();
        }
        YldGame.launch(game, GameConfiguration.iniConfig(ini));
    }

    @Override
    public void paint(Graphics g)
    {
        sampleGraphics.setGraphics(g);
        g.fillRect(0, 0, View.getActView().getWidth(), View.getActView().getHeight());
        if (game.getScene() != null)
            YldWindow.YldWindowG.handleGraphics(g, game.getScene().getGraphics());
        if (game.getExtensions() != null)
        {
            for (int i = 0; i < game.getExtensions().size(); i++)
            {
                YldExtension extension = game.getExtensions().get(i);
                sampleGraphics.setGraphics(g);
                extension.render(sampleGraphics);
            }
        }
        if (game.getConfiguration().sync)
        {
            Toolkit.getDefaultToolkit().sync();
        }
    }

    public static YldGame getGame()
    {
        return game;
    }

    public static void setGame(YldGame game)
    {
        YldWebConnector.game = game;
    }

    public AWTGraphics getSampleGraphics()
    {
        return sampleGraphics;
    }
}
