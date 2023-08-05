package com.xebisco.yield.script.compiler;

import com.xebisco.yield.script.compiler.modifiers.UnknownModifierException;
import com.xebisco.yield.script.compiler.modifiers.VariableModifiers;
import com.xebisco.yield.script.interpreter.Declaration;
import com.xebisco.yield.script.interpreter.instruction.*;
import com.xebisco.yield.script.interpreter.value.ValueGet;
import com.xebisco.yield.script.interpreter.value.ValueImmutable;
import com.xebisco.yield.script.interpreter.value.ValueSet;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;

public class DefaultCompiler {
    public final CompilerType compilerType(final SourceSequence source, final CompilerType back, final CompilerBank bank) throws UnknownModifierException, NotAStatementException, ValueAlreadyExistsException {
        if (source.text() == null) {
            CompilerType[] ct = new CompilerType[source.seq().length - 1];
            CompilerBank b = new CompilerBank(bank);
            for (int i = 0; i < source.seq().length; i++) {
                CompilerType c = compilerType(source.seq()[i], i == 0 ? null : ct[i - 1], b);
                if (i < source.seq().length - 1)
                    ct[i] = c;
                b = new CompilerBank(b);
            }
        }

        var isFunction = false;
        var isImmutable = true;
        String name = null;
        Class<?> type = Object.class;
        IInstruction instruction = null;

        String text = source.text();

        Matcher castMatcher = CompilerPatterns.CAST_PATTERN.matcher(text);
        if (castMatcher.find()) {
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

            final var valueInstruction = compilerType(new SourceSequence(matcher.group(3), null, false, source.strings()), null, new CompilerBank(bank)).instruction();

            name = matcher.group(2);

            if (isDeclaration) {
                if (bank.compilerTypes().contains(new CompilerType(name, type, false, false, null)))
                    throw new ValueAlreadyExistsException(name);
                instruction = new VariableDeclaration(name, valueInstruction, type, new ValueGet(), isImmutable ? new ValueImmutable() : new ValueSet());
            } else {
                final var local = !name.contains(".");
                if (local) {
                    instruction = new SetVariable(name, valueInstruction);
                } else {
                    //TODO change value
                }
            }
        }

        matcher.usePattern(CompilerPatterns.STRING_PATTERN);
        if(matcher.matches()) {
            return new CompilerType(null, type, false, false, new SimpleReturn(source.strings()[Integer.parseInt(matcher.group(1))]));
        }

        if (instruction == null) {
            throw new NotAStatementException(text);
        }

        final var out = new CompilerType(name, type, isImmutable, isFunction, instruction);
        bank.compilerTypes().add(out);
        return out;
    }

    public final SourceSequence sourceSequence(String source) throws ParenthesisLevelException {
        final var s = new ArrayList<String>();
        final var builder = new StringBuilder();
        List<String> strings = new ArrayList<>();
        source = source.trim();
        var inParenthesis = source.startsWith("(") && source.endsWith(")");

        if (inParenthesis) source = source.substring(1, source.length() - 1);

        var parenthesisLvl = new AtomicInteger();
        StringBuilder inString = null;

        var addParenthesisOnEnd = 0;

        int charIndex = 0;

        for (char c : source.toCharArray()) {
            inString = sourceCharProcess(parenthesisLvl, c, inString, builder, strings, s);
            if (parenthesisLvl.get() == 0 && inString == null) {
                if (charIndex == source.length() - 1)
                    for (char c1 : CompilerPatterns.ADD_LVL_CHARS) {
                        if (c == c1) {
                            addParenthesisOnEnd++;
                            inString = sourceCharProcess(parenthesisLvl, '(', inString, builder, strings, s);
                            break;
                        }
                    }
            }

            if (addParenthesisOnEnd > 0) {
                if (addParenthesisOnEnd - parenthesisLvl.get() <= -1)
                    for (int i = 0; i < addParenthesisOnEnd; i++) {
                        addParenthesisOnEnd--;
                        sourceCharProcess(parenthesisLvl, ')', inString, builder, strings, s);
                    }
            }
            charIndex++;
        }

        if (addParenthesisOnEnd > 0) {
            if (addParenthesisOnEnd - parenthesisLvl.get() == 0)
                for (int i = 0; i < addParenthesisOnEnd; i++) {
                    addParenthesisOnEnd--;
                    sourceCharProcess(parenthesisLvl, ')', inString, builder, strings, s);
                }
        }

        if (parenthesisLvl.get() != 0)
            throw new ParenthesisLevelException(source);

        if (!builder.toString().trim().isEmpty()) {
            if (s.isEmpty()) {
                return new SourceSequence(builder.toString().trim(), null, inParenthesis, strings.toArray(new String[0]));
            } else {
                s.add(builder.toString().trim());
            }
        }
        var sourceSequence = new SourceSequence[s.size()];
        for (int i = 0; i < sourceSequence.length; i++) sourceSequence[i] = sourceSequence(s.get(i));
        return new SourceSequence(null, sourceSequence, inParenthesis, strings.toArray(new String[0]));
    }

    private StringBuilder sourceCharProcess(AtomicInteger parenthesisLvl, char c, StringBuilder inString, StringBuilder builder, List<String> strings, List<String> s) {
        if (parenthesisLvl.get() == 0 && c == '"') {
            if (inString == null) {
                inString = new StringBuilder();
            } else {
                builder.append("STR::").append(strings.size());
                strings.add(inString.toString());
                inString = null;
            }
        } else if (inString == null) {
            if (c == '(') {
                if (!builder.toString().trim().isEmpty()) {
                    s.add(builder.toString().trim());
                    builder.setLength(0);
                }
                parenthesisLvl.incrementAndGet();
            }

            if (parenthesisLvl.get() == 0) {
                builder.append(c);
            }

            if (c == ')') {
                parenthesisLvl.decrementAndGet();
            }
        } else {
            inString.append(c);
        }
        return inString;
    }
}
