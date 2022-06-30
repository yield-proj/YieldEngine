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

package com.xebisco.yield.cfg;

import com.xebisco.yield.GameConfiguration;
import com.xebisco.yield.RelativeFile;
import com.xebisco.yield.Yld;
import sun.reflect.misc.FieldUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;

public class Interpreter {

    private final String[] contents;
    private final Object object;

    public Interpreter(String[] contents, Object o) {
        this.contents = contents;
        this.object = o;
        for (String line : contents) {
            try {
                if (line.hashCode() != "".hashCode()) {
                    boolean toChar = false;
                    String[] pcs = line.split("=");
                    for (int i1 = 0; i1 < pcs.length; i1++)
                        pcs[i1] = pcs[i1].trim();
                    if (pcs.length < 2)
                        throw new Exception("'=' expected");
                    if (pcs.length > 2)
                        throw new Exception("Out of bounds");
                    Class<?> c = o.getClass().getField(pcs[0]).getType();
                    Constructor<?> cons = null;
                    if (c.isPrimitive()) {
                        if (int.class.hashCode() == c.hashCode())
                            cons = Integer.class.getDeclaredConstructor(String.class);
                        else if (long.class.hashCode() == c.hashCode())
                            cons = Long.class.getDeclaredConstructor(String.class);
                        else if (char.class.hashCode() == c.hashCode()) {
                            cons = Character.class.getDeclaredConstructor(char.class);
                            toChar = true;
                        } else if (short.class.hashCode() == c.hashCode())
                            cons = Short.class.getDeclaredConstructor(String.class);
                        else if (boolean.class.hashCode() == c.hashCode())
                            cons = Boolean.class.getDeclaredConstructor(String.class);
                        else if (byte.class.hashCode() == c.hashCode())
                            cons = Byte.class.getDeclaredConstructor(String.class);
                    } else {
                        cons = c.getDeclaredConstructor(String.class);
                    }
                    Object obj;
                    if (!toChar) {
                        assert cons != null;
                        obj = cons.newInstance(pcs[1]);
                    } else {
                        if (pcs[1].length() > 1)
                            throw new Exception("Character needs to have only one character");
                        obj = cons.newInstance(pcs[1].charAt(0));
                    }
                    o.getClass().getField(pcs[0]).set(o, obj);
                }
            } catch (Exception e) {
                Yld.throwException(e);
            }
        }
    }

    public String[] getContents() {
        return contents;
    }

    public Object getObject() {
        return object;
    }

    public static Interpreter file(RelativeFile file, Object o) {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Interpreter(stringBuilder.toString().split("\n"), o);
    }
}