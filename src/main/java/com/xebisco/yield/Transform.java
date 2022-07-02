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

import java.io.*;
import java.util.Scanner;

public class Transform extends Component {
    public Vector2 position = new Vector2(), scale = new Vector2(1, 1), middle = new Vector2(0, 0);
    public float rotation = 0;
    public boolean middleRotation = true;
    private Vector2 transformed = new Vector2();

    public void translate(Vector2 vector2) {
        position.x += vector2.x;
        position.y += vector2.y;
        transformed.x += vector2.x;
        transformed.y += vector2.y;
    }

    public void goTo(Vector2 vector2) {
        position.x = vector2.x;
        position.y = vector2.y;
    }

    public void scale(Vector2 vector2) {
        scale.x += vector2.x;
        scale.y += vector2.y;
    }

    public void translate(float x, float y) {
        translate(new Vector2(x, y));
    }

    public void goTo(float x, float y) {
        goTo(new Vector2(x, y));
    }

    public void scale(float x, float y) {
        scale(new Vector2(x, y));
    }

    public void scale(float value) {
        scale(value, value);
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
            Yld.throwException(e);
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
            Yld.throwException(e);
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

    public Transform get() {
        Transform t = new Transform();
        t.position = position.get();
        t.scale = scale.get();
        if (middle != null)
            t.middle = middle.get();
        t.rotation = rotation;
        t.middleRotation = middleRotation;
        t.position = position.get();
        t.transformed = transformed.get();
        return t;
    }

    @Deprecated
    public static void setAs(Transform toSet, Transform transform) {
        toSet.position = transform.position.get();
        toSet.scale = transform.scale.get();
        if (transform.middle != null)
            toSet.middle = transform.middle.get();
        toSet.rotation = transform.rotation;
        toSet.middleRotation = transform.middleRotation;
        toSet.position = transform.position.get();
        toSet.transformed = transform.transformed.get();
    }

    public Vector2 getTransformed()
    {
        return transformed;
    }
}
