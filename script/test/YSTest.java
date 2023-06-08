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

import com.xebisco.yield.script.Function;

import java.util.HashMap;

import static com.xebisco.yield.script.YS10.*;

public class YSTest {
    public static void main(String[] args) {
        Function function = new Function(null);
        ysLoadBasicJavaLang(function);
        ysCompileFunction(ysReadInputStream(YSTest.class.getResourceAsStream("/test.ys")), function);
        ysRunFunction(function);
    }
}