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

import com.xebisco.yield.script.math.MathOperation;
import com.xebisco.yield.script.math.Operation;
import com.xebisco.yield.script.obj.*;

import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YS10 {

    private static final Pattern SIMPLIFIED_VARIABLE_DECLARATION = Pattern.compile("^(\\w+) *= *([^}]*)$"), IMMUTABLE_VARIABLE_DECLARATION = Pattern.compile("^const +(\\w+) *= *([^}]*)$"), ISOLATED_FUNCTION_CALL = Pattern.compile("^([^()]+)\\(([^}]*)\\)$"), MACRO_CALL = Pattern.compile("^([^()]+)!\\(([^}]*)\\)$"), NO_ARGS_MACRO_CALL = Pattern.compile("^([^()]+)!$"), IMPORT_AS = Pattern.compile("^import +(\\w+.*) +as +(\\w+)$"), SIMPLE_IMPORT = Pattern.compile("^import +(\\w+.*)$"), STRING_REF = Pattern.compile("^STR::(-?[0-9]+)$"), FUNCTION_REF = Pattern.compile("^FUNC::(-?[0-9]+)$"), FUNCTION_CAST = Pattern.compile(".* +as +([a-zA-z0-9.]+)$"), FUNCTION_DECLARATION = Pattern.compile("^(\\w+) *\\(([^}]*)\\)([^}]+)$"), FUNCTION_RETURN = Pattern.compile("^return +([^}]+)$"), ARRAY_GET = Pattern.compile("^(\\w+) *\\[([^}]+)]$");
    public static final Macro[] STANDARD_MACROS = new Macro[]{
            new Macro() {
                @Override
                public String name() {
                    return "println";
                }

                @Override
                public Object run(ObjectValue[] args) {
                    if (args.length == 0) System.out.println();
                    else if (args.length == 1) System.out.println(args[0].getValue());
                    else throw new IllegalArgumentException("Unexpected argument count");
                    return null;
                }
            },
            new Macro() {
                @Override
                public String name() {
                    return "print";
                }

                @Override
                public Object run(ObjectValue[] args) {
                    if (args.length == 1) System.out.print(args[0].getValue());
                    else throw new IllegalArgumentException("Unexpected argument count");
                    return null;
                }
            },
            new Macro() {
                @Override
                public String name() {
                    return "concat";
                }

                @Override
                public Object run(ObjectValue[] args) {
                    if (args.length != 1)
                        throw new IllegalArgumentException("Unexpected argument count");
                    if (!args[0].getCast().isArray()) throw new IllegalArgumentException("Array is expected");
                    StringBuilder builder = new StringBuilder();
                    for (Object o : (Object[]) args[0].getValue()) {
                        builder.append(o.toString());
                    }
                    return builder.toString();
                }
            },
            new Macro() {
                @Override
                public String name() {
                    return "currentTimeMillis";
                }

                @Override
                public Object run(ObjectValue[] args) {
                    return System.currentTimeMillis();
                }
            },
            new Macro() {
                @Override
                public String name() {
                    return "sleep";
                }

                @Override
                public Object run(ObjectValue[] args) {
                    Macro.checkArgs(args, Long.class);
                    try {
                        Thread.sleep((Long) args[0].getValue());
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return null;
                }
            }
    };


    public static String ysExtractStrings(String line) {
        StringBuilder string = null;
        StringBuilder outLine = new StringBuilder();
        line = line.replace("\\'", "\12").replace("\\\"", "\13");
        for (char c : line.toCharArray()) {
            if (c == '\'' || c == '\"') {
                if (string == null) string = new StringBuilder();
                else {
                    int id = YS.STRINGS.size();
                    YS.STRINGS.add(new ObjectValue(new StandardGet(), new ImmutableSet(), string.toString(), String.class));
                    outLine.append("STR::").append(id);
                    string = null;
                }
            } else if (string != null) string.append(c);
            else outLine.append(c);
        }
        return outLine.toString().replace("\12", "'").replace("\13", "\"");
    }

    public static ReturnRunnable ysGetFunctionCall(String line, ReturnRunnable context, Function function, Class<?> cast) {
        Matcher matcher = NO_ARGS_MACRO_CALL.matcher(line);
        if (matcher.matches()) {
            return () -> {
                function.getMacros().get(matcher.group(1)).run(new ObjectValue[0]);
                return new ObjectValue(new StandardGet(), new ImmutableSet(), null, Object.class);
            };
        }
        matcher.usePattern(MACRO_CALL);
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
                ObjectValue[] arguments = new ObjectValue[argumentsRunnables.length];
                for (int i = 0; i < arguments.length; i++) {
                    arguments[i] = argumentsRunnables[i].run();
                }
                return new ObjectValue(new StandardGet(), new ImmutableSet(), function.getMacros().get(matcher.group(1)).run(arguments), Object.class);
            };
        }
        matcher.usePattern(ISOLATED_FUNCTION_CALL);
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
                Object[] argumentsObj = new Object[arguments.length];
                for (int i = 0; i < argumentsObj.length; i++) {
                    argumentsObj[i] = arguments[i].getValue();
                }
                Object ret;
                if (contextObj instanceof YSObject) {
                    try {
                        FunctionCall call;
                        call = (FunctionCall) ysGetVariable(matcher.group(1), (YSObject) contextObj).getValue();
                        ret = ysRunFunctionCall(call, argumentsObj, function);
                    } catch (ClassCastException e) {
                        throw new IllegalStateException(e);
                    }
                } else {
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
        }
        if (line.toLowerCase().matches("^-?[0-9.]+[fl]?$")) {
            Number n;
            if (line.toLowerCase().endsWith("f")) {
                n = Float.parseFloat(line.substring(0, line.length() - 1));
            } else if (line.toLowerCase().endsWith("l")) {
                n = Long.parseLong(line.substring(0, line.length() - 1));
            } else {
                try {
                    if (line.contains("."))
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
        }
        if (ysIsMath(line)) {
            StringBuilder left = new StringBuilder(), right = null;
            ReturnRunnable leftReturn = null;
            Operation operation = null;
            for (char c : line.toCharArray()) {
                if (c == '+' || c == '-' || c == '/' || c == '*' || c == '^') {
                    if (right != null) {
                        String l = left.toString().trim(), r = right.toString().trim();
                        Operation finalOperation = operation;
                        leftReturn = () -> new ObjectValue(new StandardGet(), new ImmutableSet(), new MathOperation(ysGetInstruction(l, function), ysGetInstruction(r, function), finalOperation).result(), null);
                    }
                    switch (c) {
                        case '+':
                            operation = Operation.SUM;
                            break;
                        case '-':
                            operation = Operation.SUBTRACT;
                            break;
                        case '/':
                            operation = Operation.DIVIDE;
                            break;
                        case '*':
                            operation = Operation.MULTIPLY;
                            break;
                        case '^':
                            operation = Operation.POWER;
                            break;
                    }
                    if (left.toString().trim().equals(""))
                        left.append("0");
                    right = new StringBuilder();
                } else Objects.requireNonNullElse(right, left).append(c);
            }
            if (operation == null)
                throw new ArithmeticException();

            if (leftReturn == null) {
                leftReturn = ysGetInstruction(left.toString().trim(), function);
            }
            String r = right.toString().trim();
            Operation finalOperation = operation;
            ReturnRunnable finalLeftReturn = leftReturn;
            return () -> new ObjectValue(new StandardGet(), new ImmutableSet(), new MathOperation(finalLeftReturn, ysGetInstruction(r, function), finalOperation).result(), cast);

        }
        String finalLine = line;
        return () -> {
            ObjectValue ret;
            Object contextObj = context.run().getValue();
            if (contextObj instanceof YSObject) {
                ObjectValue objectValue = ysGetVariable(finalLine, (YSObject) contextObj);
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

    public static CastRet ysGetCast(String line, Function function) {
        Matcher matcher = FUNCTION_CAST.matcher(line);
        Class<?> cast;
        if (matcher.matches()) {
            String cs = matcher.group(1);
            if (cs.endsWith("[]")) {
                cs = cs.substring(0, cs.length() - 2);
                cast = Array.newInstance(function.getJavaImports().get(cs), 0).getClass();
            } else
                cast = function.getJavaImports().get(cs);
            if (cast == null)
                throw new IllegalStateException("Could not find import: '" + matcher.group(1) + "'");
            line = line.substring(0, line.lastIndexOf(" as ")).trim();
        } else cast = null;
        return new CastRet(cast, line);
    }

    public static boolean ysIsMath(String line) {
        return line.contains("+") || line.contains("-") || line.contains("/") || line.contains("*") || line.contains("^");
    }

    public static ReturnRunnable ysLoadFunction(Function function, String name, String fileName, ClassNotFoundException e) {
        try {
            Function imported = new Function(null);
            imported.setDeleteVariablesAfterRun(false);
            ysLoadVariable(function, name, new ObjectValue(new StandardGet(), new ImmutableSet(), imported, YSObject.class), false);
            ysLoadBasicJavaLang(imported);
            File file = new File(function.getDirectory(), fileName + ".ys");
            InputStream inputStream;
            if (file.exists()) {
                inputStream = new FileInputStream(file);
            } else {
                inputStream = YS.class.getResourceAsStream("/" + fileName + ".ys");
            }
            ysCompileFunction(ysReadInputStream(inputStream), imported);
            return () -> {
                ysRunFunction(imported, function);
                return null;
            };
        } catch (Exception e1) {
            e.printStackTrace();
            throw new ImportException(e1);
        }
    }

    public static ReturnRunnable ysGetInstruction(String line, Function function) {
        CastRet castRet = ysGetCast(line, function);
        Class<?> cast = castRet.getType();
        line = castRet.getNewLine();
        //Array check
        boolean array = line.startsWith("[") && line.endsWith("]");
        if (array) {
            int al = 0;
            for (char c : line.toCharArray()) {
                if (c == '[') al++;
                else if (c == ']') al--;
            }
            if (al != 0) array = false;
        }
        if (array) {
            line = line.substring(1, line.length() - 1);
            List<String> objs = ysGetSplitOutsideParenthesis(line, ',');
            List<ReturnRunnable> returnRunnables = new ArrayList<>();
            for (String s : objs)
                returnRunnables.add(ysGetInstruction(s.trim(), function));
            return () -> {
                Class<?> type = null;
                List<Object> values = new ArrayList<>();
                for (ReturnRunnable r : returnRunnables) {
                    ObjectValue value = r.run();
                    Object o = value.getValue();
                    values.add(o);
                    if (type == null)
                        type = value.getCast();
                    else
                        type = ysInstanceOf(o, type);
                }
                if (type == null)
                    type = Object.class;
                Object a = Array.newInstance(type, objs.size());
                for (int i = 0; i < values.size(); i++)
                    Array.set(a, i, values.get(i));
                return new ObjectValue(new StandardGet(), new ImmutableSet(), a, cast);
            };
        }
        Matcher matcher = IMMUTABLE_VARIABLE_DECLARATION.matcher(line);
        if (matcher.matches()) {
            ReturnRunnable value = ysGetInstruction(matcher.group(2), function);
            assert value != null;
            return () -> ysLoadVariable(function, matcher.group(1), new ObjectValue(new StandardGet(), new ImmutableSet(), value.run().getValue(), cast), false);
        }
        matcher.usePattern(SIMPLIFIED_VARIABLE_DECLARATION);
        if (matcher.matches()) {
            ReturnRunnable value = ysGetInstruction(matcher.group(2), function);
            assert value != null;
            return () -> ysLoadVariable(function, matcher.group(1), new ObjectValue(new StandardGet(), new StandardSet(), value.run().getValue(), cast), true);
        }
        matcher.usePattern(ARRAY_GET);
        if (matcher.matches()) {
            ReturnRunnable r = ysGetInstruction(matcher.group(2), function);
            assert r != null;
            return () -> new ObjectValue(new StandardGet(), new ImmutableSet(), ((Object[]) ysGetVariable(matcher.group(1), function).getValue())[(int) r.run().getValue()], cast);
        }
        matcher.usePattern(IMPORT_AS);
        if (matcher.matches()) {
            try {
                function.getJavaImports().put(matcher.group(2), Class.forName(matcher.group(1)));
            } catch (ClassNotFoundException e) {
                return ysLoadFunction(function, matcher.group(2), matcher.group(1), e);
            }
            return null;
        }
        matcher.usePattern(SIMPLE_IMPORT);
        if (matcher.matches()) {
            try {
                Class<?> c = Class.forName(matcher.group(1));
                function.getJavaImports().put(c.getSimpleName(), c);
            } catch (ClassNotFoundException e) {
                return ysLoadFunction(function, matcher.group(1), matcher.group(1), e);
            }
            return null;
        }
        matcher.usePattern(FUNCTION_DECLARATION);
        if (matcher.matches()) {
            String[] args = matcher.group(2).split(",");
            if(args.length == 1 && args[0].equals("")) args = new String[0];
            Matcher func = FUNCTION_REF.matcher(matcher.group(3));
            func.matches();
            FunctionCall call = new FunctionCall(args, YS.FUNCTIONS.get(Integer.parseInt(func.group(1))));
            call.getFunction().getJavaImports().putAll(function.getJavaImports());
            call.getFunction().setDeleteVariablesAfterRun(true);
            return () -> ysLoadVariable(function, matcher.group(1), new ObjectValue(new StandardGet(), new ImmutableSet(), call, FunctionCall.class), false);
        }
        matcher.usePattern(STRING_REF);
        if (matcher.matches()) {
            ObjectValue s = YS.STRINGS.get(Integer.parseInt(matcher.group(1)));
            return () -> s;
        }
        matcher.usePattern(FUNCTION_RETURN);
        if (matcher.matches()) {
            ReturnRunnable rr = ysGetInstruction(matcher.group(1), function);
            assert rr != null;
            return new ReturnRunnable() {
                @Override
                public ObjectValue run() {
                    return rr.run();
                }

                @Override
                public boolean isReturnCall() {
                    return true;
                }
            };
        }
        Class<?> imp = function.getJavaImports().get(line);
        if (imp != null) return () -> new ObjectValue(new StandardGet(), new ImmutableSet(), imp, Class.class);

        List<String> calls = ysGetSplitOutsideParenthesis(line, '.');
        ReturnRunnable[] rCalls = new ReturnRunnable[calls.size()];
        if (calls.size() > 1 && !(line.matches("^[0-9][^{]*") || ysIsMath(line))) {
            rCalls[0] = ysGetInstruction(calls.get(0), function);
            for (int i = 1; i < calls.size(); i++) {
                String l = calls.get(i);
                CastRet castRet1 = ysGetCast(l, function);
                l = castRet1.getNewLine();
                rCalls[i] = ysGetFunctionCall(l, rCalls[i - 1], function, castRet1.getType());
            }
            return rCalls[rCalls.length - 1];
        } else {
            return ysGetFunctionCall(line, () -> ysGetVariable("self", function), function, cast);
        }
    }

    public static ObjectValue ysGetVariable(String var, YSObject object) {
        ObjectValue value = object.getVariables().get(var);
        if (value == null) {
            try {
                YSObject p = object;
                do {
                    p = (YSObject) p.getVariables().get("parent").getValue();
                    if (p != null) {
                        value = p.getVariables().get(var);
                        if (value != null) return value;
                    }
                } while (p != null);
                throw new NullPointerException();
            } catch (NullPointerException e) {
                throw new IllegalStateException("Couldn't found any match for '" + var + "'");
            }
        }
        return value;
    }

    public static Object ysCastObject(Object o, Class<?> type) {
        if (type.isPrimitive()) {
            //noinspection unchecked
            return ysCastNumber((Number) o, (Class<? extends Number>) type);
        } else {
            return type.cast(o);
        }
    }

    public static Number ysCastNumber(Number number, Class<? extends Number> primitive) {
        switch (primitive.getSimpleName()) {
            case "int":
                return number.intValue();
            case "float":
                return number.floatValue();
            case "double":
                return number.doubleValue();
            case "byte":
                return number.byteValue();
            case "long":
                return number.longValue();
            case "short":
                return number.shortValue();
        }
        throw new IllegalStateException("Not a primitive number type");
    }

    public static void ysCompileFunction(String[] lines, Function function) {
        for (int i = 0; i < lines.length; i++) {
            try {
                if (!lines[i].equals("")) {
                    if (i < lines.length - 1 && lines[i + 1].contains("{")) {
                        int id = YS.FUNCTIONS.size();
                        List<String> bLines = new ArrayList<>();
                        int l = 1;
                        int i1 = 2;
                        lines[i + 1] = lines[i + 1].substring(lines[i + 1].indexOf('{') + 1);
                        do {
                            StringBuilder builder = new StringBuilder();
                            for (char c : lines[i + i1].toCharArray()) {
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
                                lines[i + i1] = "";
                            } else {
                                lines[i + i1] = lines[i + i1].substring(builder.length());
                            }
                            i1++;
                        }
                        while (l > 0);
                        if (l < 0)
                            throw new IllegalSyntaxException("Closing non existent");
                        CastRet castRet = ysGetCast(lines[i], function);
                        Class<?> cast = castRet.getType();
                        lines[i] = castRet.getNewLine();
                        Function f = new Function(cast);
                        ysInherit(f, function);
                        ysCompileFunction(bLines.toArray(new String[0]), f);
                        YS.FUNCTIONS.put(id, f);
                        lines[i] = lines[i].trim() + "FUNC::" + id;
                    }
                    ReturnRunnable instruction = ysGetInstruction(lines[i], function);
                    if (instruction != null)
                        function.getInstructions().add(instruction);
                }
            } catch (Exception e) {
                throw new CompileException(lines[i], i + 1, e);
            }
        }
    }

    public static Object ysRunFunction(Function function, String functionName, Function caller) {
        return ysRunFunction(((FunctionCall) function.getVariables().get(functionName).getValue()).getFunction(), caller);
    }

    public static Object ysRunMainFunction(Function function, Object[] args) {
        return ysRunFunctionCall(((FunctionCall) function.getVariables().get("main").getValue()), new Object[] {args}, function);
    }

    public static Object ysRunFunctionCall(FunctionCall functionCall, Object[] args, Function caller) {
        int i = 0;
        for (String arg : functionCall.getArgs()) {
            CastRet cr = ysGetCast(arg, caller);
            functionCall.getFunction().getVariables().put(cr.getNewLine(), new ObjectValue(new StandardGet(), new ImmutableSet(), args[i], cr.getType()));
            i++;
        }
        if (i > functionCall.getArgs().length) throw new IllegalSyntaxException("Wrong arguments length");
        return ysRunFunction(functionCall.getFunction(), caller);
    }

    public static Object ysRunFunction(Function function, Function caller) {
        Map<String, ObjectValue> copy = new HashMap<>(function.getVariables());
        ysInherit(function, caller);
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
        function.getVariables().putAll(copy);
        System.gc();
        return ret;
    }

    public static void ysInherit(Function function, Function parent) {
        if (parent != null) {
            function.getVariables().put("parent", new ObjectValue(new StandardGet(), new ImmutableSet(), parent, null));
            function.getMacros().putAll(parent.getMacros());
            function.getJavaImports().putAll(parent.getJavaImports());
        }
    }

    public static List<String> ysGetSplitOutsideParenthesis(String s, char match) {
        List<String> out = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        int p = 0, h = 0;
        for (char c : s.toCharArray()) {
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

    public static ObjectValue ysLoadVariable(YSObject object, String name, ObjectValue value, boolean ignoreExistence) {
        if (!object.getVariables().containsKey(name)) {
            object.getVariables().put(name, value);
            return value;
        } else if (ignoreExistence) {
            try {
                object.getVariables().get(name).setValue(value.getValue());
                return value;
            } catch (ImmutableBreakException e) {
                throw new IllegalStateException("Variable '" + name + "' is immutable", e);
            }
        }
        throw new IllegalSyntaxException("Variable '" + name + "' already exists");
    }

    public static void ysLoadBasicJavaLang(Function function) {
        function.getJavaImports().put("Boolean", Boolean.class);
        function.getJavaImports().put("boolean", boolean.class);
        function.getJavaImports().put("Byte", Byte.class);
        function.getJavaImports().put("byte", byte.class);
        function.getJavaImports().put("Character", Character.class);
        function.getJavaImports().put("char", char.class);
        function.getJavaImports().put("Class", Class.class);
        function.getJavaImports().put("Double", Double.class);
        function.getJavaImports().put("double", double.class);
        function.getJavaImports().put("Float", Float.class);
        function.getJavaImports().put("float", float.class);
        function.getJavaImports().put("Integer", Integer.class);
        function.getJavaImports().put("int", int.class);
        function.getJavaImports().put("Long", Long.class);
        function.getJavaImports().put("long", long.class);
        function.getJavaImports().put("Math", Math.class);
        function.getJavaImports().put("Object", Object.class);
        function.getJavaImports().put("Process", Process.class);
        function.getJavaImports().put("ProcessBuilder", ProcessBuilder.class);
        function.getJavaImports().put("Runtime", Runtime.class);
        function.getJavaImports().put("Short", Short.class);
        function.getJavaImports().put("short", short.class);
        function.getJavaImports().put("String", String.class);
        function.getJavaImports().put("StringBuilder", StringBuilder.class);
        function.getJavaImports().put("System", System.class);
        function.getJavaImports().put("Number", Number.class);
        function.getJavaImports().put("Thread", Thread.class);
        function.getJavaImports().put("Void", Void.class);
        function.getJavaImports().put("void", void.class);
    }

    public static void ysLoadStandardMacros(Function function) {
        for (Macro macro : STANDARD_MACROS)
            function.getMacros().put(macro.name(), macro);
    }

    public static Class<?> ysInstanceOf(Object o, Class<?> c) {
        Class<?> s = c;
        while (!s.equals(o.getClass())) {
            s = s.getSuperclass();
            if (s == null)
                return null;
        }
        return s;
    }

    public static String[] ysReadInputStream(InputStream inputStream) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = ysExtractStrings(line.trim());
                String[] lns = line.replace("{", "\4{").replace(';', '\4').split("\4");
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
