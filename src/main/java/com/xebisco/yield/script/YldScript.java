package com.xebisco.yield.script;

import com.xebisco.yield.RelativeFile;
import com.xebisco.yield.Yld;
import com.xebisco.yield.script.console.Log;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public final class YldScript extends RelativeFile {
    private String rawScriptContents = "";
    private String[] scriptContents;
    private ArrayList<ScriptFunction> functions;
    private ArrayList<ScriptFunction> exFunctions = new ArrayList<>();
    private ArrayList<ScriptVariable> variables = new ArrayList<>();

    private YldScript(String relativePath) {
        super(relativePath + ".yld");
    }

    public static YldScript load(String relativePath) {
        YldScript script = new YldScript(relativePath);
        readScript(script);
        formatScript(script);
        script.load();
        return script;
    }

    public void load() {
        ArrayList<ScriptFunction> functions = new ArrayList<>();
        ArrayList<String> actFuncContents = new ArrayList<>();
        String actFuncName = "";
        functions.add(new Log(this));
        boolean funcStatement;
        boolean isMet = false;
        for (String scriptContent : scriptContents) {
            String[] wds = scriptContent.split(" ");
            if (scriptContent.contains("global var")) {
                ScriptVariable variable = new ScriptVariable(null);
                variable.setName(wds[2]);
                if (wds.length > 3) {
                    if (wds[3].hashCode() == "=".hashCode() && wds[3].equals("=")) {
                        variable.setValue(wds[4]);
                    } else {
                        throw new ScriptSyntaxException("'=' expected");
                    }
                }
                variables.add(variable);
            } else {
                funcStatement = scriptContent.contains("fun") || scriptContent.contains("met");
                isMet = scriptContent.contains("met");
                if (funcStatement) {
                    if (!actFuncName.equals("")) {
                        ScriptFunction function = new ScriptFunction(actFuncName, actFuncContents.toArray(new String[0]), this);
                        if (!isMet) {
                            functions.add(new ScriptFunction(actFuncName, actFuncContents.toArray(new String[0]), this));
                        } else {
                            exFunctions.add(new ScriptFunction(actFuncName, actFuncContents.toArray(new String[0]), this));
                        }
                    }
                    while (actFuncContents.size() > 0)
                        actFuncContents.clear();
                    actFuncName = wds[1];
                } else {
                    actFuncContents.add(scriptContent);
                }
            }
        }
        if (!actFuncName.equals("")) {
            if (actFuncContents.size() > 0) {
                ScriptFunction function = new ScriptFunction(actFuncName, actFuncContents.toArray(new String[0]), this);
                if (!isMet) {
                    functions.add(new ScriptFunction(actFuncName, actFuncContents.toArray(new String[0]), this));
                } else {
                    exFunctions.add(new ScriptFunction(actFuncName, actFuncContents.toArray(new String[0]), this));
                }
            }
        }
        this.functions = functions;
        callMet("load", null);
    }

    private void callMet(String funcName, String[] arguments) {
        ScriptFunction function = null;
        for (ScriptFunction function1 : getExFunctions()) {
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

    public static void readScript(YldScript script) {
        try {
            Scanner sc = new Scanner(script.getStream());
            while (sc.hasNextLine()) {
                script.setRawScriptContents(script.getRawScriptContents() + sc.nextLine() + ";");
            }
        } catch (NullPointerException e) {
            try {
                throw new FileNotFoundException(script.getRelativePath());
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void formatScript(YldScript script) {
        StringBuilder formatted = new StringBuilder();
        boolean lastIsSpace = false, inText = false, fillSpaces;
        String before, after;
        for (char c : script.getRawScriptContents().toCharArray()) {
            before = "";
            after = "";
            fillSpaces = false;
            if (c == '\'') {
                inText = !inText;
            }
            if (inText) {
                lastIsSpace = false;
                if (c == ' ') {
                    c = '\\';
                    after = "s";
                }
                if (c == ',') {
                    c = '\\';
                    after = "v";
                }
                if (c == ';') {
                    c = '\\';
                    after = "p";
                }
            } else {
                if (c == ',' || c == ':') {
                    fillSpaces = true;
                }
                if (c == ' ') {
                    if (lastIsSpace) c = '\20';
                    lastIsSpace = true;
                } else {
                    lastIsSpace = false;
                }
            }

            if (fillSpaces) {
                before = ' ' + before;
                lastIsSpace = true;
                after += ' ';
            }
            if (c != '\20') {
                formatted.append(before).append(c).append(after);
            }
        }

        ArrayList<String> lines = new ArrayList<>();

        for (String line : formatted.toString().split(";")) {
            while (line.startsWith(" "))
                line = line.substring(1);
            while (line.endsWith(" "))
                line = line.substring(0, line.length() - 1);
            if (line.hashCode() != "".hashCode() && !line.equals(""))
                lines.add(line);
        }
        script.setScriptContents(lines.toArray(new String[0]));
    }

    public String getRawScriptContents() {
        return rawScriptContents;
    }

    public void setRawScriptContents(String rawScriptContents) {
        this.rawScriptContents = rawScriptContents;
    }

    public String[] getScriptContents() {
        return scriptContents;
    }

    public void setScriptContents(String[] scriptContents) {
        this.scriptContents = scriptContents;
    }

    public ArrayList<ScriptFunction> getFunctions() {
        return functions;
    }

    public void setFunctions(ArrayList<ScriptFunction> functions) {
        this.functions = functions;
    }

    public ArrayList<ScriptVariable> getVariables() {
        return variables;
    }

    public void setVariables(ArrayList<ScriptVariable> variables) {
        this.variables = variables;
    }

    public ArrayList<ScriptFunction> getExFunctions() {
        return exFunctions;
    }

    public void setExFunctions(ArrayList<ScriptFunction> exFunctions) {
        this.exFunctions = exFunctions;
    }
}
