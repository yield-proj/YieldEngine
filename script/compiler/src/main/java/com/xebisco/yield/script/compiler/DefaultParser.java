/*
 * Copyright [2022-2023] [Xebisco]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xebisco.yield.script.compiler;

import java.util.ArrayList;

public class DefaultParser extends Parser {
    private static final char[] CHARS_TO_REMOVE_SPACES = new char[]{'(', ')', '=', '.', ':', '+', '-', '^', '/', '|', '&', ',', '{', '}'};
    public static final char STRING_CHAR = '\0';
    public static final char PART_CHAR = '\1';

    public ParserOutput output(String source) {
        StringBuilder builder = new StringBuilder(), actString = null, actPart = null;
        ArrayList<String> lines = new ArrayList<>(), strings = new ArrayList<>(), partsString = new ArrayList<>();
        int partLvl = 0;
        boolean onSpace = false;
        for (char c : source.toCharArray()) {
            if (actPart == null) {
                if (c == '\"') {
                    onSpace = false;
                    if (actString == null)
                        actString = new StringBuilder();
                    else {
                        builder.append(STRING_CHAR).append(strings.size());
                        strings.add(actString.toString());
                        actString = null;
                    }
                    continue;
                }
                if (actString != null) {
                    actString.append(c);
                    continue;
                }
            }
            if (c == '}' && --partLvl == 0) {
                if (actPart != null) {
                    builder.append(PART_CHAR).append(partsString.size());
                    partsString.add(actPart.toString());
                    actPart = null;
                    continue;
                }
                throw new ParseException("'" + builder + "': expected '{'");
            }
            if (c == '{' && partLvl++ == 0) {
                onSpace = false;
                actPart = new StringBuilder();
                continue;
            }
            if (actPart != null) {
                actPart.append(c);
                continue;
            }
            if (c == ';') {
                String line = builder.toString();
                while (line.startsWith(" ") || line.startsWith("\t")) {
                    line = line.substring(1);
                }
                while (line.endsWith(" ") || line.endsWith("\t")) {
                    line = line.substring(0, line.length() - 1);
                }
                if(builder.length() > 0) {
                    lines.add(line);
                    builder = new StringBuilder();
                }
                continue;
            }
            boolean append = true;
            if (c == ' ') {
                if (onSpace)
                    append = false;
                onSpace = true;
            } else {
                onSpace = false;
                for (char c1 : CHARS_TO_REMOVE_SPACES) {
                    if (c != c1) continue;
                    onSpace = true;
                    if (builder.length() <= 0) break;
                    while (builder.charAt(builder.length() - 1) == ' ') {
                        builder.setLength(builder.length() - 1);
                    }
                    break;
                }
            }
            if (!append || c == '\n') continue;
            builder.append(c);
        }
        if (builder.length() > 0) {
            throw new ParseException("'" + builder + "': expected ';'");
        }
        ParserOutput[] parts = new ParserOutput[partsString.size()];
        for (int i = 0; i < parts.length; ++i) {
            parts[i] = output(partsString.get(i));
        }
        return new ParserOutput(lines.toArray(new String[0]), parts, strings.toArray(new String[0]));
    }
}