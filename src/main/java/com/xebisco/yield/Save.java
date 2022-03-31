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

import com.xebisco.yield.GameConfiguration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Save
{

    public static void saveContents(String contents, GameConfiguration config, boolean encrypted)
    {
        if (config.appName == null)
        {
            throw new NullPointerException("GameConfiguration.appName can't be null!");
        }
        String appdataPath = System.getenv("APPDATA");
        File dir = new File(appdataPath + "\\YieldGames\\");
        dir.mkdir();
        File file = new File(appdataPath + "\\YieldGames\\" + config.appName + ".ylds");
        try
        {
            file.createNewFile();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        save(contents, file.getPath(), encrypted);
    }

    public static String getContents(GameConfiguration config)
    {
        if (config.appName == null)
        {
            throw new NullPointerException("GameConfiguration.appName can't be null!");
        }
        String appdataPath = System.getenv("APPDATA");
        File file = new File(appdataPath + "\\YieldGames\\" + config.appName + ".ylds");
        if (!file.exists())
            saveContents("", config, false);
        return load(file.getPath());
    }

    public static String load(String savePath)
    {
        try
        {
            Scanner sc = new Scanner(new File(savePath));
            StringBuilder contents = new StringBuilder();
            while (sc.hasNextLine())
            {
                contents.append(sc.nextLine());
            }
            return processSave(contents.toString());
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static void save(String contents, String savePath, boolean encrypted)
    {
        PrintWriter writer;
        try
        {
            writer = new PrintWriter(savePath, "UTF-8");
            String toPrint = "";
            if (encrypted)
                toPrint += "y";
            toPrint += ":";
            String c = "";
            c += contents;
            char[] chars = c.toCharArray();
            if (encrypted)
                for (int i = 0; i < chars.length; i++)
                {
                    chars[i] = (char) ((int) chars[i] - 4);
                }
            c = new String(chars);
            toPrint += c;
            writer.print(toPrint);
            writer.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    private static String processSave(String saveContents)
    {
        if (saveContents != null && saveContents.hashCode() != "".hashCode())
        {
            String[] saveC = saveContents.split(":");
            StringBuilder chords = new StringBuilder();
            for (int i = 1; i < saveC.length; i++)
            {
                chords.append(saveC[i]);
            }
            char[] chars = chords.toString().toCharArray();
            if (saveC[0].equals("y"))
                for (int i = 0; i < chars.length; i++)
                {
                    chars[i] = (char) ((int) chars[i] + 4);
                }
            return new String(chars);
        }
        else return "";
    }
}
