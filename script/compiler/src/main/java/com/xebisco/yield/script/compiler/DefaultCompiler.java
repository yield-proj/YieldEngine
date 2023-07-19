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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class DefaultCompiler implements Compiler {

    private record ArrayReference<T>(T[] array, int index) {
        public T get() {
            return array[index];
        }
    }

    public static final Pattern NEW_IMMUTABLE_OBJECT = Pattern.compile("^let ([^0-9]\\w*)="), NEW_MUTABLE_OBJECT = Pattern.compile("^var ([^0-9]\\w*)=(.+)$"), CAST = Pattern.compile(" as ([.A-Za-z0-9]+)"), STRING = Pattern.compile("^" + DefaultParser.STRING_CHAR + "([0-9]+)$"), PART = Pattern.compile("^" + DefaultParser.PART_CHAR + "([0-9]+)$"), INSIDE = Pattern.compile("^\\.$");

    @Override
    public Instruction createInstruction(final SepInstruction sepInstruction, final String[] strings, final Instruction[][] partInstructions) throws CompileException {
        return createInstruction(sepInstruction, strings, partInstructions, null, null, null, null, false);
    }

    private Instruction createInstruction(final SepInstruction sepInstruction, final String[] strings, final Instruction[][] partInstructions, final ArrayReference<Instruction> frontInstruction, final SepInstruction backSep, final SepInstruction frontSep, final SepInstruction front2Sep, final boolean ignoreIgnoreCompiling) throws CompileException {
        if (!ignoreIgnoreCompiling && sepInstruction.ignoreCompiling())
            return null;
        if (sepInstruction.arguments() == null) {
            if (sepInstruction.text() != null) {
                final Matcher matcher = NEW_IMMUTABLE_OBJECT.matcher(sepInstruction.text());
                if (matcher.matches()) {
                    return bank -> {
                        bank.objectMap().put(
                                new NameArgs(matcher.group(1), null),
                                new ScriptObject(frontInstruction.get().run(new Bank(bank)), null, new DefaultValueProcess(), new SealedValueProcess())
                        );
                        return null;
                    };
                }
                matcher.usePattern(STRING);
                if (matcher.matches()) {
                    String s = strings[Integer.parseInt(matcher.group(1))];
                    return bank -> s;
                }
                matcher.usePattern(PART);
                if (matcher.matches()) {
                    Instruction[] instructions = partInstructions[Integer.parseInt(matcher.group(1))];
                    return bank -> instructions;
                }

                matcher.usePattern(INSIDE);
                if (matcher.matches()) {

                }

                if (frontSep != null && frontSep.parenthesis()) {
                    frontSep.setIgnoreCompiling(true);
                    if (sepInstruction.text().equals("if")) {
                        final Instruction instruction = createInstruction(front2Sep, strings, partInstructions, null, null, null, null, false);
                        final Instruction verify = createInstruction(frontSep, strings, partInstructions, null, null, null, null, true);
                        assert verify != null;
                        assert instruction != null;
                        return bank -> {
                            final boolean verifyBoolean = (boolean) verify.run(bank);
                            if(verifyBoolean) {
                                return new Bank(bank).attach((Instruction[]) instruction.run(new Bank(bank)));
                            };
                            return null;
                        };
                    }
                    return getMethod(sepInstruction.text(), createInstruction(frontSep, strings, partInstructions, null, null, null, null, true), frontSep.arguments() == null ? 1 : frontSep.arguments().size(), null);
                } else {
                    return getObject(sepInstruction.text(), null);
                }
            } else {
                Instruction[] instructions = new Instruction[sepInstruction.order().size()];
                for (int i = 0; i < instructions.length; i++) {
                    instructions[i] = createInstruction(
                            sepInstruction.order().get(i),
                            strings,
                            partInstructions,
                            i < instructions.length - 1 ? new ArrayReference<>(instructions, i + 1) : null,
                            i > 0 ? sepInstruction.order().get(i - 1) : null,
                            i < instructions.length - 1 ? sepInstruction.order().get(i + 1) : null,
                            i < instructions.length - 2 ? sepInstruction.order().get(i + 2) : null,
                            false
                    );
                }
                return instructions[0];
            }
        } else {
            final Instruction[] instructions = new Instruction[sepInstruction.arguments().size()];
            for (int i = 0; i < instructions.length; i++)
                instructions[i] = createInstruction(sepInstruction.arguments().get(i), strings, partInstructions);
            return bank -> {
                final Object[] args = new Object[instructions.length];
                final Semaphore semaphore = new Semaphore(0);
                final AtomicInteger pr = new AtomicInteger();
                IntStream.range(0, instructions.length).parallel().forEach(i -> {
                    args[i] = instructions[i].run(new Bank(null));
                    if (pr.addAndGet(1) == args.length) {
                        semaphore.release();
                    }
                });
                semaphore.acquireUninterruptibly();
                return args;
            };
        }
    }

    /*
            String source = sepInstruction.text();
            final Matcher castMatcher = CAST.matcher(source);
            final boolean casting = castMatcher.find();
            final Instruction castInstruction = casting ? bank -> null : bank -> {
                try {
                    return Class.forName(castMatcher.group(1));
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            };
            if(casting) {
                source = source.substring(0, castMatcher.start());
            }
     */


    /*
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
     */

    public Instruction getObject(String s, ArrayReference<?> context) {
        if (context == null)
            return bank -> {
                try {
                    return bank.get(s);
                } catch (MissingObjectException e) {
                    try {
                        return Class.forName(s);
                    } catch (ClassNotFoundException ignore) {
                    }
                    throw new RuntimeException(e);
                }
            };

        return bank -> {
            try {
                try {
                    Object o = context.get();
                    return o.getClass().getField(s).get(o);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        };
    }

    public Instruction getMethod(String s, Instruction args, int argsCount, ArrayReference<?> context) {
        if (context == null) {
            return bank -> {
                try {
                    return bank.get(new NameArgs(s, argsCount));
                } catch (MissingObjectException e) {
                    Method[] methods = Standard.class.getMethods();
                    try {
                        return invokeFromMethods(s, args, argsCount, methods, Standard.class, bank);
                    } catch (NoSuchMethodException ex) {
                        throw new RuntimeException(e);
                    }
                }
            };
        }

        return bank -> {
            Object o = context.get();
            Method[] methods = o.getClass().getMethods();
            try {
                return invokeFromMethods(s, args, argsCount, methods, o, bank);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        };
    }

    private Object invokeFromMethods(String s, Instruction args, int argsCount, Method[] methods, Object obj, Bank bank) throws NoSuchMethodException {
        for (Method m : methods) {
            if (s.hashCode() == m.getName().hashCode() && s.equals(m.getName())) {
                if (m.getParameterCount() == argsCount) {
                    try {
                        return m.invoke(obj, args.run(bank));
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        StringBuilder a = new StringBuilder();
        for (int i = 0; i < argsCount; i++) {
            if (a.length() > 0) a.append(", ");
            a.append("Object");
        }

        throw new NoSuchMethodException(s + "(" + a + ")");

    }

    @Override
    public SepInstruction createSepInstruction(final String source) throws CompileException {
        return createSepInstruction(source, false);
    }

    private SepInstruction createSepInstruction(final String source, final boolean inParenthesis) throws CompileException {
        ArrayList<SepInstruction> list = null, args = null;
        var builder = new StringBuilder();
        var argBuilder = new StringBuilder();
        int sepLvl = 0;
        for (char c : source.toCharArray()) {
            if (c == '(') {
                if (sepLvl++ == 0) {
                    if (list == null) list = new ArrayList<>();
                    if (builder.length() > 0)
                        list.add(createSepInstruction(builder.toString(), true));
                    builder = new StringBuilder();
                    continue;
                }
            } else if (c == ')' && --sepLvl == 0) {
                if (list == null) {
                    throw new CompileException("illegal end of sep");
                }
                list.add(createSepInstruction(builder.toString(), true));
                builder = new StringBuilder();
                continue;
            }
            if (c == ',') {
                if (!inParenthesis && sepLvl == 0)
                    throw new CompileException("wrong argument location");
                if (sepLvl == 0) {
                    if (args == null)
                        args = new ArrayList<>();
                    args.add(createSepInstruction(argBuilder.toString(), false));
                    argBuilder = new StringBuilder();
                    continue;
                }
            }
            builder.append(c);
            argBuilder.append(c);
        }
        if(builder.length() > 0 && list != null) {
            list.add(new SepInstruction(builder.toString(), false));
        }
        if (argBuilder.length() > 0 && args != null) {
            args.add(createSepInstruction(argBuilder.toString(), false));
        }
        if (args != null) return new SepInstruction(args);
        else if (list == null)
            return new SepInstruction(builder.toString(), inParenthesis);
        return new SepInstruction(list, inParenthesis);
    }
}