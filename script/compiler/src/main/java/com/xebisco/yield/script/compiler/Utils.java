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
    public static Instruction[] instructions(final String source) throws CompileException {
        Compiler compiler = new DefaultCompiler();
        final var output = new DefaultParser().output(source);
        return instructions(output, compiler);
    }

    public static Instruction[] instructions(final ParserOutput output, final Compiler compiler) throws CompileException {
        final var partInstructions = new ArrayList<Instruction[]>();
        for (int i = 0; i < output.parts().length; i++)
            partInstructions.add(instructions(output.parts()[i], compiler));
        return compiler.createInstructions(output.formatted(), output.strings(), partInstructions.toArray(new Instruction[0][0]));
    }

    public static List<String> split(String source, char match) {
        final List<String> out = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        int p = 0, h = 0;
        for (char c : source.toCharArray()) {
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
}
