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

package com.xebisco.yield.script;

import com.xebisco.yield.script.obj.*;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YS10 {

    private static final Pattern SIMPLIFIED_VARIABLE_DECLARATION = Pattern.compile("^(\\w+) *= *([^}]*)$"), ISOLATED_FUNCTION_CALL = Pattern.compile("^([^()]+)\\(([^}]*)\\)$"), IMPORT = Pattern.compile("^import +(\\w+.*) +as +(\\w+)$"), STRING_REF = Pattern.compile("^\2(-?[0-9]+)$"), FUNCTION_REF = Pattern.compile("^\3(-?[0-9]+)$"), FUNCTION_CAST = Pattern.compile(".* +as +([a-zA-z0-9.]+)$"), FUNCTION_DECLARATION = Pattern.compile("func +(\\w+) *\\(([^}]*)\\)([^}]+)");


    public static String ysExtractStrings(String line) {
        StringBuilder string = null;
        StringBuilder outLine = new StringBuilder();
        for (char c : line.toCharArray()) {
            if (c == '\'') {
                if (string == null) string = new StringBuilder();
                else {
                    int id = YS.STRINGS.size();
                    YS.STRINGS.add(new ObjectValue(new StandardGet(), new ImmutableSet(), string.toString(), String.class));
                    outLine.append("\2").append(id);
                    string = null;
                }
            } else if (string != null) string.append(c);
            else outLine.append(c);
        }
        return outLine.toString();
    }

    public static ReturnRunnable ysGetFunctionCall(String line, ReturnRunnable context, Function function) {
        CastRet castRet = ysGetCast(line, function);
        Class<?> cast = castRet.getType();
        line = castRet.getNewLine();

        Matcher matcher = ISOLATED_FUNCTION_CALL.matcher(line);
        if (matcher.matches()) {
            List<String> args;
            if (matcher.group(2).equals(""))
                args = new ArrayList<>();
            else
                args = ysGetSplitOutsideParenthesis(matcher.group(2), ',');
            ReturnRunnable[] argumentsRunnables = new ReturnRunnable[args.size()];
            for (int i = 0; i < argumentsRunnables.length; i++) {
                argumentsRunnables[i] = ysGetInstruction(args.get(i), function);
            }
            return () -> {
                Object contextObj = context.run().getValue();
                ObjectValue[] arguments = new ObjectValue[argumentsRunnables.length];
                for (int i = 0; i < arguments.length; i++) {
                    arguments[i] = argumentsRunnables[i].run();
                }
                Object ret;
                if (contextObj instanceof YSObject) {
                    try {
                        FunctionCall call;
                        try {
                            call = (FunctionCall) ((YSObject) contextObj).getVariables().get(matcher.group(1)).getValue();
                        } catch (NullPointerException e) {
                            throw new IllegalStateException("Could not find '" + matcher.group(1) + "'");
                        }
                        int i = 0;
                        for (String arg : call.getArgs().keySet()) {
                            call.getFunction().getVariables().put(arg, arguments[i]);
                            i++;
                        }
                        if (i > arguments.length) throw new IllegalSyntaxException("Wrong arguments length");
                        ret = ysRunFunction(call.getFunction());
                    } catch (ClassCastException e) {
                        throw new IllegalStateException(e);
                    }
                } else {
                    Object[] argumentsObj = new Object[arguments.length];
                    for (int i = 0; i < argumentsObj.length; i++) {
                        argumentsObj[i] = arguments[i].getValue();
                    }
                    Class<?>[] argumentsClasses = new Class<?>[argumentsObj.length];
                    for (int i = 0; i < argumentsClasses.length; i++) {
                        if (arguments[i].getCast() != null)
                            argumentsClasses[i] = arguments[i].getCast();
                        else try {
                            argumentsClasses[i] = argumentsObj[i].getClass();
                        } catch (NullPointerException e) {
                            throw new IllegalSyntaxException("'" + args.get(i) + "'. Cannot get class of null objects");
                        }
                    }
                    try {
                        Class<?> c;
                        if (contextObj instanceof Class<?>)
                            c = (Class<?>) contextObj;
                        else c = contextObj.getClass();
                        Method method = c.getMethod(matcher.group(1), argumentsClasses);
                        method.setAccessible(true);
                        ret = method.invoke(contextObj, argumentsObj);
                    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }
                }
                return new ObjectValue(new StandardGet(), new StandardSet(), ret, cast);
            };
        } else if (line.toLowerCase().matches("^-?[0-9.]+[fl]?$")) {
            Number n;
            if (line.toLowerCase().endsWith("f")) {
                n = Float.parseFloat(line.substring(0, line.length() - 1));
            } else if (line.toLowerCase().endsWith("l")) {
                n = Long.parseLong(line.substring(0, line.length() - 1));
            } else {
                try {
                    if(line.contains("."))
                        throw new NumberFormatException();
                    n = Integer.parseInt(line);
                } catch (NumberFormatException e) {
                    try {
                        n = Double.parseDouble(line);
                    } catch (NumberFormatException e1) {
                        throw new NumberFormatException(line);
                    }
                }
            }
            Number finalN = n;
            return () -> new ObjectValue(new StandardGet(), new ImmutableSet(), finalN, cast);
        } else if (line.contains("+") || line.contains("-") || line.contains("/") || line.contains("*") || line.contains("^")) {
            return null;
            //TODO
        } else {
            String finalLine = line;
            return () -> {
                ObjectValue ret;
                Object contextObj = context.run().getValue();
                if (contextObj instanceof YSObject) {
                    ObjectValue objectValue = ysGetVariable(finalLine, function);
                    if (cast == null)
                        ret = objectValue;
                    else
                        ret = new ObjectValue(objectValue.getObjectValueGet(), objectValue.getObjectValueSet(), objectValue.getValue(), cast);
                } else {
                    try {
                        if (contextObj instanceof Class<?>) {
                            ret = new ObjectValue(new StandardGet(), new ImmutableSet(), ((Class<?>) contextObj).getField(finalLine).get(null), cast);
                        } else
                            ret = new ObjectValue(new StandardGet(), new ImmutableSet(), contextObj.getClass().getField(finalLine).get(contextObj), cast);
                    } catch (IllegalAccessException | NoSuchFieldException e) {
                        throw new RuntimeException(e);
                    }
                }
                return ret;
            };
        }
    }

    public static CastRet ysGetCast(String line, Function function) {
        Matcher matcher = FUNCTION_CAST.matcher(line);
        Class<?> cast;
        if (matcher.matches()) {
            cast = function.getJavaImports().get(matcher.group(1));
            if (cast == null)
                throw new IllegalStateException("Could not find import: '" + matcher.group(1) + "'");
            line = line.substring(0, line.lastIndexOf(" as ")).trim();
        } else cast = null;
        return new CastRet(cast, line);
    }

    public static ReturnRunnable ysGetInstruction(String line, Function function) {
        Matcher matcher = SIMPLIFIED_VARIABLE_DECLARATION.matcher(line);
        if (matcher.matches()) {
            ReturnRunnable value = ysGetInstruction(matcher.group(2), function);
            assert value != null;
            CastRet castRet = ysGetCast(line, function);
            Class<?> cast = castRet.getType();
            return () -> ysLoadVariable(function, matcher.group(1), new ObjectValue(new StandardGet(), new StandardSet(), value.run().getValue(), cast));
        }
        matcher.usePattern(IMPORT);
        if (matcher.matches()) {
            try {
                function.getJavaImports().put(matcher.group(2), Class.forName(matcher.group(1)));
            } catch (ClassNotFoundException e) {
                try {
                    //TODO cast
                    Function imported = new Function(null);
                    imported.setDeleteVariablesAfterRun(false);
                    ysLoadVariable(function, matcher.group(2), new ObjectValue(new StandardGet(), new ImmutableSet(), imported, YSObject.class));
                    ysLoadBasicJavaLang(imported);
                    File file = new File(function.getDirectory(), matcher.group(1) + ".ys");
                    InputStream inputStream;
                    if (file.exists()) {
                        inputStream = new FileInputStream(file);
                    } else {
                        inputStream = YS.class.getResourceAsStream("/" + matcher.group(1) + ".ys");
                    }
                    ysCompileFunction(ysReadInputStream(inputStream), imported);
                    return () -> {
                        ysRunFunction(imported);
                        return null;
                    };
                } catch (Exception e1) {
                    e.printStackTrace();
                    throw new ImportException(e1);
                }
            }
            return null;
        }
        matcher.usePattern(FUNCTION_DECLARATION);
        if (matcher.matches()) {
            String[] args = matcher.group(2).split(",");
            HashMap<String, Class<?>> fArgs = new HashMap<>();
            if (!matcher.group(2).equals(""))
                for (String arg : args) {
                    String[] argSep = arg.trim().split(":");
                    if (argSep.length > 2 || argSep.length == 0)
                        throw new IllegalSyntaxException();
                    if (argSep.length == 1)
                        fArgs.put(argSep[0].trim(), null);
                    else fArgs.put(argSep[0].trim(), function.getJavaImports().get(argSep[1].trim()));
                }
            Matcher func = FUNCTION_REF.matcher(matcher.group(3));
            func.matches();
            FunctionCall call = new FunctionCall(fArgs, YS.PROGRAMS.get(Integer.parseInt(func.group(1))));
            call.getFunction().getJavaImports().putAll(function.getJavaImports());
            call.getFunction().setDeleteVariablesAfterRun(true);
            return () -> ysLoadVariable(function, matcher.group(1), new ObjectValue(new StandardGet(), new ImmutableSet(), call, FunctionCall.class));
        }
        matcher.usePattern(STRING_REF);
        if (matcher.matches()) {
            ObjectValue s = YS.STRINGS.get(Integer.parseInt(matcher.group(1)));
            return () -> s;
        }
        Class<?> imp = function.getJavaImports().get(line);
        if (imp != null) return () -> new ObjectValue(new StandardGet(), new ImmutableSet(), imp, Class.class);

        List<String> calls = ysGetSplitOutsideParenthesis(line, '.');
        ReturnRunnable[] rCalls = new ReturnRunnable[calls.size()];
        if (calls.size() > 1 && !line.matches("^[0-9][^{]*")) {
            rCalls[0] = ysGetInstruction(calls.get(0), function);
            for (int i = 1; i < calls.size(); i++) {
                rCalls[i] = ysGetFunctionCall(calls.get(i), rCalls[i - 1], function);
            }
            return rCalls[rCalls.length - 1];
        } else {
            return ysGetFunctionCall(line, () -> function.getVariables().get("self"), function);
        }
    }

    public static ObjectValue ysGetVariable(String var, YSObject object) {
        ObjectValue value = object.getVariables().get(var);
        if (value == null) throw new IllegalStateException("Couldn't found any match for '" + var + "'");
        return value;
    }

    public static Object ysCastObject(Object o, Class<?> type) {
        if(type.isPrimitive()) {
            //noinspection unchecked
            return ysCastNumber((Number) o, (Class<? extends Number>) type);
        } else {
            return type.cast(o);
        }
    }

    public static Number ysCastNumber(Number number, Class<? extends Number> primitive) {
        switch (primitive.getSimpleName()) {
            case "int": return number.intValue();
            case "float": return number.floatValue();
            case "double": return number.doubleValue();
            case "byte": return number.byteValue();
            case "long": return number.longValue();
            case "short": return number.shortValue();
        }
        throw new IllegalStateException("Not a primitive number type");
    }

    public static void ysCompileFunction(String[] lines, Function function) {
        String[] ext = new String[lines.length];
        for (int i = 0; i < ext.length; i++)
            ext[i] = ysExtractStrings(lines[i].trim());
        for (int i = 0; i < ext.length; i++) {
            try {
                if (!ext[i].equals("")) {
                    if (i < ext.length - 1 && ext[i + 1].contains("{")) {
                        int id = YS.PROGRAMS.size();
                        List<String> bLines = new ArrayList<>();
                        int l = 1;
                        int i1 = 2;
                        ext[i + 1] = ext[i + 1].substring(ext[i + 1].indexOf('{') + 1);
                        do {
                            StringBuilder builder = new StringBuilder();
                            for (char c : ext[i + i1].toCharArray()) {
                                if (c == '{') l++;
                                else if (c == '}') {
                                    l--;
                                    if (l == 0) {
                                        builder.append('}');
                                        break;
                                    }
                                }
                                if (l > 0) {
                                    builder.append(c);
                                }
                            }
                            if (l > 0) {
                                bLines.add(builder.toString());
                                ext[i + i1] = "";
                            } else {
                                ext[i + i1] = ext[i + i1].substring(builder.length());
                            }
                            i1++;
                        }
                        while (l > 0);
                        if (l < 0)
                            throw new IllegalSyntaxException("Closing non existent");
                        CastRet castRet = ysGetCast(ext[i], function);
                        Class<?> cast = castRet.getType();
                        ext[i] = castRet.getNewLine();
                        Function f = new Function(cast);
                        f.getJavaImports().putAll(function.getJavaImports());
                        ysCompileFunction(bLines.toArray(new String[0]), f);
                        YS.PROGRAMS.put(id, f);
                        ext[i] = ext[i].trim() + "\3" + id;
                    }
                    ReturnRunnable instruction = ysGetInstruction(ext[i], function);
                    if (instruction != null)
                        function.getInstructions().add(instruction);
                }
            } catch (Exception e) {
                throw new CompileException(lines[i], i + 1, e);
            }
        }
    }

    public static Object ysRunFunction(Function function) {
        Object ret = null;
        for (ReturnRunnable r : function.getInstructions()) {
            try {
                ObjectValue retV = r.run();
                if (r.isReturnCall()) {
                    ret = retV.getValue();
                    break;
                }
            } catch (Exception e) {
                throw new RunException(e);
            }
        }
        if (function.isDeleteVariablesAfterRun())
            function.getVariables().clear();
        return ret;
    }

    public static List<String> ysGetSplitOutsideParenthesis(String s, char match) {
        List<String> out = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        int p = 0;
        for (char c : s.toCharArray()) {
            if (c == '(') p++;
            else if (c == ')') p--;
            else if (p == 0) if (c == match) {
                out.add(builder.toString());
                builder = new StringBuilder();
            }
            if (p > 0 || c != match)
                builder.append(c);
        }
        out.add(builder.toString());
        return out;
    }

    public static ObjectValue ysLoadVariable(YSObject object, String name, ObjectValue value) {
        object.getVariables().put(name, value);
        return value;
    }

    public static void ysLoadBasicJavaLang(Function function) {
        function.getJavaImports().put("boolean", Boolean.class);
        function.getJavaImports().put("primitive_boolean", boolean.class);
        function.getJavaImports().put("byte", Byte.class);
        function.getJavaImports().put("primitive_byte", byte.class);
        function.getJavaImports().put("char", Character.class);
        function.getJavaImports().put("primitive_char", char.class);
        function.getJavaImports().put("class", Class.class);
        function.getJavaImports().put("double", Double.class);
        function.getJavaImports().put("primitive_double", double.class);
        function.getJavaImports().put("float", Float.class);
        function.getJavaImports().put("primitive_float", float.class);
        function.getJavaImports().put("int", Integer.class);
        function.getJavaImports().put("primitive_int", int.class);
        function.getJavaImports().put("long", Long.class);
        function.getJavaImports().put("primitive_long", long.class);
        function.getJavaImports().put("math", Math.class);
        function.getJavaImports().put("object", Object.class);
        function.getJavaImports().put("process", Process.class);
        function.getJavaImports().put("process_builder", ProcessBuilder.class);
        function.getJavaImports().put("runtime", Runtime.class);
        function.getJavaImports().put("short", Short.class);
        function.getJavaImports().put("primitive_short", short.class);
        function.getJavaImports().put("string", String.class);
        function.getJavaImports().put("string_builder", StringBuilder.class);
        function.getJavaImports().put("system", System.class);
        function.getJavaImports().put("thread", Thread.class);
        function.getJavaImports().put("void", Void.class);
        function.getJavaImports().put("primitive_void", void.class);
    }

    public static String[] ysReadInputStream(InputStream inputStream) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] lns = line.replace("{", "\4{").split("\4");
                if (lns.length == 0)
                    lines.add("{");
                else {
                    if (!lns[0].equals(""))
                        lines.add(lns[0]);
                    lines.addAll(Arrays.asList(lns).subList(1, lns.length));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return lines.toArray(new String[0]);
    }
}
