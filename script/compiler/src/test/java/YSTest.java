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

import com.xebisco.yield.script.compiler.CompileException;
import com.xebisco.yield.script.Program;
import com.xebisco.yield.script.compiler.Utils;

public class YSTest {
    public static void main(String[] args) throws CompileException {
        Program program = new Program();
        program.attach(Utils.instructions("""
                let test = "aaa";
                {
                    let test2 = "bbbb";
                };
                """));
        System.out.println(program.objectMap());
    }
}
