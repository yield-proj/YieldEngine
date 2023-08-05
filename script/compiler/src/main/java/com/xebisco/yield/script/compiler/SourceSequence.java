package com.xebisco.yield.script.compiler;

public record SourceSequence(String text, SourceSequence[] seq, boolean inParenthesis, String[] strings) {
}
