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
    /**
     * The center point of an entity.
     */
    public Vector2 position;
    /**
     * The scale of an entity. (normal is (1, 1)).
     */
    public Vector2 scale;
    /**
     * The rotation center of an entity
     */
    public Vector2 middle;
    /**
     * Rotation value in degrees of a entity.
     */
    public float rotation = 0;
    /**
     * If the entity rotate from the middle or not.
     */
    public boolean middleRotation;
    private Vector2 transformed;

    /**
     * Reset the transform to its default state.
     */
    public void reset() {
        scale = new Vector2(1, 1);
        position = new Vector2();
        middle = new Vector2(0, 0);
        middleRotation = true;
        transformed = new Vector2();
    }

    public Transform() {
        reset();
    }

    /**
     * Adds the given vector to the position and transformed vectors.
     *
     * @param vector2 The vector to translate the position by.
     */
    public void translate(Vector2 vector2) {
        position.x += vector2.x;
        position.y += vector2.y;
        transformed.x += vector2.x;
        transformed.y += vector2.y;
    }

    /**
     * This function sets the position of the object to the position of the vector2.
     *
     * @param vector2 The position to go to.
     */
    public void goTo(Vector2 vector2) {
        position.x = vector2.x;
        position.y = vector2.y;
    }

    /**
     * This function adds the x and y values of the vector2 parameter to the x and y values of the scale variable.
     *
     * @param vector2 The vector to scale the object by.
     */
    public void scale(Vector2 vector2) {
        scale.x += vector2.x;
        scale.y += vector2.y;
    }

    /**
     * Translate the object by the given vector.
     *
     * @param x The x-coordinate of the translation.
     * @param y The y-coordinate of the translation.
     */
    public void translate(float x, float y) {
        translate(new Vector2(x, y));
    }

    /**
     * This function takes in a Vector2 and calls the goTo function that takes in two floats.
     *
     * @param x The x coordinate of the destination.
     * @param y The y coordinate of the destination.
     */
    public void goTo(float x, float y) {
        goTo(new Vector2(x, y));
    }

    /**
     * Scale the object by the given amount.
     *
     * @param x The x-axis scale factor.
     * @param y The y-axis scale factor.
     */
    public void scale(float x, float y) {
        scale(new Vector2(x, y));
    }

    /**
     * This function scales the entity by the given value.
     *
     * @param value The value to scale the entity by.
     */
    public void scale(float value) {
        scale(value, value);
    }

    /**
     * Rotate() adds the parameter to the rotation variable.
     *
     * @param degrees The amount of degrees to rotate the entity.
     */
    public void rotate(float degrees) {
        rotation += degrees;
    }

    /**
     * Read the file, and then process the file. Applying its value to the current Transform object.
     *
     * @param save The TransformSave object that was passed to the load method.
     */
    public void load(TransformSave save) {
        try {
            Scanner sc = new Scanner(save.getInputStream());
            StringBuilder contents = new StringBuilder();
            while (sc.hasNextLine()) {
                contents.append(sc.nextLine());
            }
            processSave(contents.toString());
        } catch (NullPointerException e) {
            Yld.throwException(e);
        }
    }

    /**
     * It takes the position, scale, and rotation of the object and converts it into a string that can be saved to a file.
     *
     * @param saveFile The save file that the object is being saved to.
     * @return A string of the transform's position, scale, and rotation.
     */
    public String save(TransformSave saveFile) {
        PrintWriter writer;
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
        return toPrint;
    }

    /**
     * It takes a string, and apply its values to the current Transform object.
     *
     * @param saveContents The string that is passed to the method.
     */
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

    /**
     * This function returns a copy of the Transform object.
     *
     * @return A copy of the transform object.
     */
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

    /**
     * This function returns the transformed vector.
     *
     * @return The transformed vector.
     */
    public Vector2 getTransformed() {
        return transformed;
    }
}
