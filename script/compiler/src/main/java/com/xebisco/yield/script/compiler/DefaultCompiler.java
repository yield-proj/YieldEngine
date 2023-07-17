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

import com.xebisco.yield.script.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultCompiler implements Compiler {

    public static final Pattern NEW_IMMUTABLE_OBJECT = Pattern.compile("^let ([^0-9]\\w*)=(.+)$"), NEW_MUTABLE_OBJECT = Pattern.compile("^var ([^0-9]\\w*)=(.+)$"), CAST = Pattern.compile(" as ([.A-Za-z0-9]+)"), STRING = Pattern.compile("^" + DefaultParser.STRING_CHAR + "([0-9]+)$"), PART = Pattern.compile("^" + DefaultParser.PART_CHAR + "([0-9]+)$");

    @Override
    public Instruction createInstruction(String source, final String[] strings, final Instruction[][] partInstructions) throws CompileException {
        Instruction cast;
        final Matcher castMatcher = CAST.matcher(source);
        if (castMatcher.find()) {
            String n = castMatcher.group(1);
            cast = bank -> {
                try {
                    return Class.forName(n);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            };
            source = source.substring(0, castMatcher.start());
        } else {
            cast = bank -> null;
        }
        final String finalSource = source;
        System.out.println(finalSource);
        final Matcher matcher = NEW_IMMUTABLE_OBJECT.matcher(source);
        if (matcher.matches()) {
            Instruction value = createInstruction(matcher.group(2), strings, partInstructions);
            return bank -> bank.objectMap().put(new NameArgs(matcher.group(1), null), new ScriptObject(value.run(new Bank(bank)), (Class<?>) cast.run(null), new DefaultValueProcess(), new SealedValueProcess()));
        }

        matcher.usePattern(STRING);
        if (matcher.matches()) {
            final String s = strings[Integer.parseInt(matcher.group(1))];
            return bank -> s;
        }

        matcher.usePattern(PART);
        if (matcher.matches()) {
            final Instruction[] instructions = partInstructions[Integer.parseInt(matcher.group(1))];
            return bank -> new Bank(bank).attach(instructions);
        }

        try {
            Instruction jo = getJavaObject(finalSource, null);
            return bank -> ((Class<?>) cast.run(new Bank(bank))).cast(jo.run(new Bank(bank)));
        } catch (Exception e) {
            throw new CompileException(e);
        }
    }

    public Instruction getJavaObject(String s, Object context) {
        if (context == null)
            return bank -> {
                try {
                    return Class.forName(s);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            };

        try {
            Field field = context.getClass().getField(s);
            return bank -> {
                try {
                    return field.get(s);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            };
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SepInstruction createSepInstruction(final String source, final String[] strings, final Instruction[][] partInstructions) throws CompileException {
        ArrayList<SepInstruction> list = null;
        var builder = new StringBuilder();
        int sepLvl = 0;
        for (char c : source.toCharArray()) {
            if (c == '(') {
                if (sepLvl++ == 0) {
                    if (list != null) continue;
                    list = new ArrayList<>();
                    continue;
                }
            } else if (c == ')' && --sepLvl == 0) {
                if (list == null) {
                    throw new CompileException("illegal end of sep");
                }
                list.add(this.createSepInstruction(builder.toString(), strings, partInstructions));
                builder = new StringBuilder();
                continue;
            }
            builder.append(c);
        }
        return new SepInstruction(list);
    }
}