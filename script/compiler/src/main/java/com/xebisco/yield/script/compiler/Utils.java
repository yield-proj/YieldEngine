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

import com.xebisco.yield.script.Instruction;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static Instruction[] instructions(final String source) {
        Compiler compiler = new DefaultCompiler();
        final var output = new DefaultParser().output(source);
        return instructions(output, compiler);
    }

    public static Instruction[] instructions(final ParserOutput output, final Compiler compiler) {
        final var partInstructions = new ArrayList<Instruction[]>();
        for (int i = 0; i < output.parts().length; i++)
            partInstructions.add(instructions(output.parts()[i], compiler));

        final Instruction[] instructions = new Instruction[output.formatted().length];
        for (int i = 0; i < instructions.length; i++) {
            try {
                instructions[i] = compiler.createInstruction(compiler.createSepInstruction(output.formatted()[i]), output.strings(), partInstructions.toArray(new Instruction[0][0]));
            } catch (CompileException e) {
                e.printStackTrace();
                throw new IllegalStateException(e.getClass().getSimpleName() + " in: " + output.originalLines()[i]);
            }
        }
        return instructions;
    }
}
