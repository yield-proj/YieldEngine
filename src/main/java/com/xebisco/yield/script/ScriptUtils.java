package com.xebisco.yield.script;

public class ScriptUtils {
    public static String getVar(String text, YldScript script) {
        boolean isText = text.startsWith("'") && text.endsWith("'");
        if (!isText) {
            ScriptVariable variable = null;
            for (ScriptVariable var : script.getVariables()) {
                if (var.getName().hashCode() == text.hashCode()) {
                    if (var.getName().equals(text)) {
                        variable = var;
                        break;
                    }
                }
            }
            if (variable != null && variable.getValue() != null)
                return variable.getValue();
            else return null;
        } else {
            return text.replace("\\s", " ").replace("\\v", ",").replace("\\p", ";").replace("'", "");
        }
    }

    public static void executeFunction(YldScript script, String funcName, String[] arguments) {
        ScriptFunction function = null;
        for (ScriptFunction function1 : script.getFunctions()) {
            if (function1.getName().hashCode() == funcName.hashCode()) {
                if (function1.getName().equals(funcName)) {
                    function = function1;
                    break;
                }
            }
        }
        if (function == null)
            throw new ScriptSyntaxException("cannot find function: " + funcName);
        function.call(arguments);
    }

    public static void callFunction(YldScript script, String funcName, String[] arguments) {
        ScriptFunction function = null;
        for (ScriptFunction function1 : script.getFunctions()) {
            if (function1.getName().hashCode() == funcName.hashCode()) {
                if (function1.getName().equals(funcName)) {
                    function = function1;
                    break;
                }
            }
        }
        if (function != null)
            function.call(arguments);
    }
}
