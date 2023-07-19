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
import java.util.List;

public class DefaultParser extends Parser {
    private static final char[] CHARS_TO_REMOVE_SPACES = new char[]{'(', ')', '=', '.', ':', '+', '-', '^', '/', '|', '&', ',', '{', '}'};
    private static final char[] CHARS_TO_INSERT_PARENTHESIS_AFTER = new char[]{'.', '='};
    private static final char[] CHARS_TO_INSERT_PARENTHESIS_BEFORE = new char[]{'.'};
    public static final char STRING_CHAR = '\0';
    public static final char PART_CHAR = '\1';

    public ParserOutput output(String source) {
        StringBuilder builder = new StringBuilder(), actString = null, actPart = null;
        final ArrayList<String> lines = new ArrayList<>(), strings = new ArrayList<>(), partsString = new ArrayList<>();
        final ArrayList<StringBuilder> originalLinesBuilder = new ArrayList<>(), correspondingOriginalLines = new ArrayList<>();
        var partLvl = 0;
        var parenthesisLvl = 0;
        var onSpace = false;
        var insertParenthesis = 0;
        originalLinesBuilder.add(new StringBuilder());
        for (char c : source.toCharArray()) {
            if (actPart == null) {
                if (c == '\n') {
                    originalLinesBuilder.add(new StringBuilder());
                } else {
                    originalLinesBuilder.get(originalLinesBuilder.size() - 1).append(c);
                }
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
                    while (insertParenthesis > 0) {
                        insertParenthesis--;
                        builder.append(')');
                        parenthesisLvl--;
                    }
                    if (parenthesisLvl > 0)
                        throw new ParseException("'" + originalLinesBuilder.get(originalLinesBuilder.size() - 1) + "': expected ')'");
                    if (parenthesisLvl < 0)
                        throw new ParseException("'" + originalLinesBuilder.get(originalLinesBuilder.size() - 1) + "': expected '('");
                    String line = builder.toString();
                    while (line.startsWith(" ") || line.startsWith("\t")) {
                        line = line.substring(1);
                    }
                    while (line.endsWith(" ") || line.endsWith("\t")) {
                        line = line.substring(0, line.length() - 1);
                    }
                    if (builder.length() > 0) {
                        lines.add(line);
                        builder = new StringBuilder();
                    }
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
                correspondingOriginalLines.add(originalLinesBuilder.get(originalLinesBuilder.size() - 1));
                while (insertParenthesis > 0) {
                    insertParenthesis--;
                    builder.append(')');
                    parenthesisLvl--;
                }
                if (parenthesisLvl > 0)
                    throw new ParseException("'" + originalLinesBuilder.get(originalLinesBuilder.size() - 1) + "': expected ')'");
                if (parenthesisLvl < 0)
                    throw new ParseException("'" + originalLinesBuilder.get(originalLinesBuilder.size() - 1) + "': expected '('");
                String line = builder.toString();
                while (line.startsWith(" ") || line.startsWith("\t")) {
                    line = line.substring(1);
                }
                while (line.endsWith(" ") || line.endsWith("\t")) {
                    line = line.substring(0, line.length() - 1);
                }
                if (builder.length() > 0) {
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

            for (char c1 : CHARS_TO_INSERT_PARENTHESIS_BEFORE) {
                if (c == c1) {
                    int savedLvl = parenthesisLvl;
                    builder.append(')');
                    parenthesisLvl++;
                    for(int i = builder.length() - 2; i >= 0; i--) {
                        if(builder.charAt(i) == '(')
                            parenthesisLvl--;
                        else if(builder.charAt(i) == ')')
                            parenthesisLvl++;
                        if(parenthesisLvl == savedLvl) {
                            builder.insert(i, '(');
                            break;
                        }
                    }
                }
            }

            builder.append(c);
            for (char c1 : CHARS_TO_INSERT_PARENTHESIS_AFTER) {
                if (c == c1) {
                    builder.append('(');
                    parenthesisLvl++;
                    insertParenthesis++;
                    break;
                }
            }
            if (c == '(') {
                parenthesisLvl++;
            }
            if (c == ')') {
                parenthesisLvl--;
            }
        }
        if (builder.length() > 0) {
            throw new ParseException("'" + builder + "': expected ';'");
        }
        ParserOutput[] parts = new ParserOutput[partsString.size()];
        for (int i = 0; i < parts.length; ++i) {
            parts[i] = output(partsString.get(i));
        }
        final var originalLines = new String[correspondingOriginalLines.size()];
        for (int i = 0; i < originalLines.length; i++)
            originalLines[i] = correspondingOriginalLines.get(i).toString();
        return new ParserOutput(lines.toArray(new String[0]), originalLines, parts, strings.toArray(new String[0]));
    }

    private List<String> split(String source, char match) {
        final List<String> out = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        int p = 0, h = 0;
        for (char c : source.toCharArray()) {
            if (c == '(') p++;
            else if (c == ')') p--;
            else if (c == '[') h++;
            else if (c == ']') h--;
            else if (p == 0 && h == 0) if (c == match) {
                out.add(builder.toString());
                builder = new StringBuilder();
            }
            if (p > 0 || h > 0 || c != match)
                builder.append(c);
        }
        out.add(builder.toString());
        return out;
    }
}