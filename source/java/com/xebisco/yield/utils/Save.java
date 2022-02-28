package com.xebisco.yield.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Save {

    public static String load(SaveFile save) {
        try {
            Scanner sc = new Scanner(new File(save.getUrl().getPath()));
            StringBuilder contents = new StringBuilder();
            while (sc.hasNextLine()) {
                contents.append(sc.nextLine());
            }
            return processSave(contents.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void save(String contents, SaveFile saveFile, boolean encrypted) {
        PrintWriter writer;
        try {
            writer = new PrintWriter(saveFile.getUrl().getPath(), "UTF-8");
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

    private static String processSave(String saveContents) {
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
    }
}
