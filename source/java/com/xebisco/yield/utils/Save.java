package com.xebisco.yield.utils;

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
        boolean c = dir.mkdir();
        File file = new File(appdataPath + "\\YieldGames\\" + config.appName + ".ylds");
        try
        {
            boolean created = file.createNewFile();
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
        if(!file.exists())
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
}
