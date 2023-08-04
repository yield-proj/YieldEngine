package com.xebisco.yield.script.compiler;

import com.xebisco.yield.script.compiler.modifiers.UnknownModifierException;
import com.xebisco.yield.script.compiler.modifiers.VariableModifiers;
import com.xebisco.yield.script.interpreter.Declaration;
import com.xebisco.yield.script.interpreter.instruction.GetVariable;
import com.xebisco.yield.script.interpreter.instruction.IInstruction;
import com.xebisco.yield.script.interpreter.instruction.VariableDeclaration;
import com.xebisco.yield.script.interpreter.value.ValueGet;
import com.xebisco.yield.script.interpreter.value.ValueImmutable;
import com.xebisco.yield.script.interpreter.value.ValueSet;

import java.util.regex.Matcher;

public class DefaultCompiler {
    protected final CompilerType compilerType(final String source, CompilerBank bank) throws UnknownModifierException, NotAStatementException {
        if (bank == null) bank = new CompilerBank();
        var isFunction = false;
        var isImmutable = true;
        String name = null;
        Class<?> type = Object.class;
        IInstruction instruction = null;

        String text = source;

        Matcher castMatcher = CompilerPatterns.CAST_PATTERN.matcher(text);
        if (!castMatcher.find()) {
            text = text.substring(0, castMatcher.start());
            try {
                type = Class.forName(castMatcher.group(1));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        Matcher matcher = CompilerPatterns.ASSIGN_PATTERN.matcher(text);

        if (matcher.matches()) {
            var isDeclaration = false;
            String mod = matcher.group(1);
            if (!mod.isEmpty()) {
                isDeclaration = true;
                if (VariableModifiers.getMod(mod) == VariableModifiers.MUTABLE) {
                    isImmutable = false;
                }
            }

            if (isDeclaration) {
                instruction = new VariableDeclaration(name = matcher.group(2), compilerType(matcher.group(3), new CompilerBank(bank)).instruction(), type, new ValueGet(), isImmutable ? new ValueImmutable() : new ValueSet());
            } else {
                boolean local = !matcher.group(2).contains(".");
                if (local) {
                    instruction = new GetVariable(new Declaration(matcher.group(2), null));
                } else {

                }
                //TODO change value
            }
        }

        if (instruction == null) {
            throw new NotAStatementException(source);
        }

        final var out = new CompilerType(name, type, isImmutable, isFunction, instruction);
        bank.compilerTypes().add(out);
        return out;
    }
}
