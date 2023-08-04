package com.xebisco.yield.script.interpreter.instruction;

import com.xebisco.yield.script.interpreter.Declaration;
import com.xebisco.yield.script.interpreter.value.ObjectValue;

import java.util.Map;

public interface IInstruction {
    Object run(Map<Declaration, ObjectValue> declarations);
}
