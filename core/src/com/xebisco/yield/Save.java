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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.EnumSet;
import java.util.Scanner;

import static java.nio.file.attribute.PosixFilePermission.*;

/**
 * It saves and loads data
 */
public class Save {


    /**
     * It creates a file in the user's home directory, and saves the contents to it
     *
     * @param contents The contents of the file.
     * @param config The GameConfiguration object that you created in the previous step.
     * @param encrypted If true, the contents will be encrypted using AES-256.
     */
    public static void saveContents(String contents, GameConfiguration config, boolean encrypted) {
        if (config.appName == null) {
            throw new NullPointerException("GameConfiguration.appName can't be null!");
        }
        String appdataPath = System.getProperty("user.home");
        File dir = new File(appdataPath + "/.YieldGames/");
        dir.mkdir();
        File file = new File(appdataPath + "/.YieldGames/" + config.appName + ".ylds");
        try {
            file.createNewFile();
        } catch (IOException e) {
            Yld.throwException(e);
        }
        try {
            Files.setPosixFilePermissions(file.toPath(),
                    EnumSet.of(OWNER_READ, OWNER_WRITE, OWNER_EXECUTE, GROUP_READ, GROUP_EXECUTE));
        } catch (UnsupportedOperationException | IOException ignore) {
        }
        save(contents, file.getPath(), encrypted);
    }

    /**
     * If the file exists, return its contents, otherwise return null.
     *
     * @param config The GameConfiguration object that you passed to the YieldGames.init() method.
     * @return The contents of the file.
     */
    public static String getContents(GameConfiguration config) {
        if (config.appName == null) {
            throw new NullPointerException("GameConfiguration.appName can't be null!");
        }
        String appdataPath = System.getProperty("user.home");
        File file = new File(appdataPath + "/.YieldGames/" + config.appName + ".ylds");
        if (!file.exists())
            return null;
        return load(file.getPath());
    }

    /**
     * It reads the contents of a file and returns it as a string
     *
     * @param savePath The path to the save file.
     * @return The contents of the file.
     */
    public static String load(String savePath) {
        try {
            Scanner sc = new Scanner(new File(savePath));
            StringBuilder contents = new StringBuilder();
            while (sc.hasNextLine()) {
                contents.append(sc.nextLine());
            }
            return processSave(contents.toString());
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    /**
     * It takes a string, a file path, and a boolean, and writes the string to the file path, and if the boolean is true,
     * it encrypts the string before writing it.
     *
     * @param contents The contents of the file
     * @param savePath The path to the file you want to save to.
     * @param encrypted Whether the file will be encrypted.
     */
    public static void save(String contents, String savePath, boolean encrypted) {
        PrintWriter writer;
        try {
            writer = new PrintWriter(savePath, "UTF-8");
            String toPrint = "";
            if (encrypted)
                toPrint += "y";
            toPrint += ":";
            String c = "";
            c += contents;
            char[] chars = c.toCharArray();
            if (encrypted)
                for (int i = 0; i < chars.length; i++) {
                    chars[i] = (char) ((int) chars[i] - 4);
                }
            c = new String(chars);
            toPrint += c;
            writer.print(toPrint);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * It decrypts a file if is encrypted.
     *
     * @param saveContents The contents of the save file.
     * @return The saveContents are being returned.
     */
    private static String processSave(String saveContents) {
        if (saveContents != null && saveContents.hashCode() != "".hashCode()) {
            String[] saveC = saveContents.split(":");
            StringBuilder chords = new StringBuilder();
            for (int i = 1; i < saveC.length; i++) {
                chords.append(saveC[i]);
            }
            char[] chars = chords.toString().toCharArray();
            if (saveC[0].equals("y"))
                for (int i = 0; i < chars.length; i++) {
                    chars[i] = (char) ((int) chars[i] + 4);
                }
            return new String(chars);
        } else return "";
    }
}
