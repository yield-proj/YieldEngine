package com.xebisco.yield.components;

import com.xebisco.yield.Component;
import com.xebisco.yield.Yld;
import com.xebisco.yield.utils.TransformSave;
import com.xebisco.yield.utils.Vector2;

import java.io.*;
import java.util.Scanner;

public class Transform extends Component {
    public Vector2 position = new Vector2(), scale = new Vector2(1, 1), middle = new Vector2(0, 0);
    public float rotation = 0;
    public boolean middleRotation = true;

    public void translate(Vector2 vector2) {
        position.x += vector2.x;
        position.y += vector2.y;
    }

    public void scale(Vector2 vector2) {
        scale.x += vector2.x;
        scale.y += vector2.y;
    }

    public void translate(float x, float y) {
        translate(new Vector2(x, y));
    }

    public void scale(float x, float y) {
        scale(new Vector2(x, y));
    }

    public void rotate(float degrees) {
        rotation += degrees;
    }

    public void load(TransformSave save) {
        try {
            Scanner sc = new Scanner(new File(save.getUrl().getPath()));
            StringBuilder contents = new StringBuilder();
            while (sc.hasNextLine()) {
                contents.append(sc.nextLine());
            }
            processSave(contents.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void saveTo(TransformSave saveFile) {
        PrintWriter writer;
        try {
            writer = new PrintWriter(saveFile.getUrl().getPath(), "UTF-8");
            String toPrint = "";
            toPrint += ":";
            String chords = "";
            chords += "x,=," + position.x + ",y,=," + position.y + ",w,=," + scale.x + ",h,=," + scale.y + ",r,=," + rotation;
            char[] chars = chords.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                chars[i] = (char) ((int) chars[i] - 4);
            }
            chords = new String(chars);
            toPrint += chords;
            writer.print(toPrint);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void processSave(String saveContents) {
        String[] saveC = saveContents.split(":");
        StringBuilder chords = new StringBuilder();
        for (int i = 1; i < saveC.length; i++) {
            chords.append(saveC[i]);
        }
        char[] chars = chords.toString().toCharArray();
        for (int i = 0; i < chars.length; i++) {
            chars[i] = (char) ((int) chars[i] + 4);
        }
        String s = new String(chars);
        String[] ss1 = s.split(",");
        for (int i = 0; i < ss1.length; i++) {
            String act = ss1[i];
            if (act.hashCode() == "=".hashCode())
                if (act.equals("=")) {
                    String parameter = ss1[i - 1], value = ss1[i + 1];
                    switch (parameter) {
                        case "x":
                            position.x = Float.parseFloat(value);
                            break;
                        case "y":
                            position.y = Float.parseFloat(value);
                            break;
                        case "w":
                            scale.x = Float.parseFloat(value);
                            break;
                        case "h":
                            scale.y = Float.parseFloat(value);
                            break;
                        case "r":
                            rotation = Float.parseFloat(value);
                            break;
                    }
                }
        }
    }

    public static void setAs(Transform toSet, Transform transform) {
        toSet.position = transform.position.get();
        toSet.scale = transform.scale.get();
        if (transform.middle != null)
            toSet.middle = transform.middle.get();
        toSet.rotation = transform.rotation;
        toSet.middleRotation = transform.middleRotation;
    }
}