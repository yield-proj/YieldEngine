package com.xebisco.yasm;

import java.io.File;
import java.io.IOException;

public final class Rd {
    public static final int VERSION = 100;
    public static Instruction[] instructions(String file, Program program, Call[] calls) throws UnknownCallException {
        if(file.isEmpty()) return new Instruction[0];
        String[] lines = file.split("\n");
        Instruction[] instructions = new Instruction[lines.length];
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            line = line.trim();
            String callString = line.substring(0, line.indexOf(' '));
            String[] argsString = line.substring(line.indexOf(' ') + 1).split(",");
            for (int i1 = 0; i1 < argsString.length; i1++) argsString[i1] = argsString[i1].trim();
            Call call = null;
            for (Call c : calls) if (c.name().equals(callString)) call = c;
            if (call == null) throw new UnknownCallException(callString);
            int[] args = new int[argsString.length];
            for (int i1 = 0; i1 < args.length; i1++) {
                int v;
                try {
                    v = Integer.parseInt(argsString[i1]);
                } catch (NumberFormatException e) {
                    v = program.regs().get(argsString[i1]);
                }
                args[i1] = v;
            }
            instructions[i] = new Instruction(call, args);
        }
        return instructions;
    }

    public static Program program(File local, String file, Call[] calls) throws UnknownCallException, IOException {
        String[] lines = file.split("\n");
        Program program = new Program();
        StringBuilder actScope = null;
        String actScopeString = null;
        program.properties().put("sdb0", local.getCanonicalPath());
        for (String line : lines) {
            line = line.trim();
            if(line.isEmpty())
                continue;
            if (line.endsWith(":")) {
                actScope = new StringBuilder();
                actScopeString = line.substring(0, line.length() - 1);
            } else if (line.equals("done")) {
                assert actScope != null;
                program.scopes().put(actScopeString, instructions(actScope.toString(), program, calls));
                actScope = null;
            } else if(actScope == null) {
                String propName = line.substring(0, line.indexOf(' '));
                String propValue = line.substring(line.indexOf(' ') + 1).replace("\\n", "\n").replace("\\s", " ").replace("\\t", "\t").replace("\\t", "\t");
                if(propName.startsWith("i_")) {
                    program.properties().put(propName.substring(2), Integer.parseInt(propValue));
                } else if(propName.startsWith("f_")) {
                    program.properties().put(propName.substring(2), Float.parseFloat(propValue));
                } else if(propName.startsWith("d_")) {
                    program.properties().put(propName.substring(2), Double.parseDouble(propValue));
                } else if(propName.startsWith("l_")) {
                    program.properties().put(propName.substring(2), Long.parseLong(propValue));
                } else {
                    program.properties().put(propName, propValue);
                }
            } else {
                actScope.append(line).append('\n');
            }
        }
        if((int) program.properties().get("version") != VERSION) System.err.println("WARNING: Not supported version. (program version = " + program.properties().getProperty("version") + ", compiler version = " + VERSION + ")");
        return program;
    }

    public static void run(Instruction[] instructions, Program program) {
        for (Instruction instruction : instructions) {
            instruction.run(program);
        }
    }
    public static void run(Program program) {
        Instruction[] instructions = program.global();
        for (Instruction instruction : instructions) {
            instruction.run(program);
        }
    }
}
