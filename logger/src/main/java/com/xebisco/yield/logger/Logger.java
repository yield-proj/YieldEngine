/*
 * Copyright [2022-2024] [Xebisco]
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

package com.xebisco.yield.logger;

import com.xebisco.yield.core.Global;

import java.io.PrintStream;

public final class Logger {
    public static void println(String tag, String message, PrintStream printStream) {
        printStream.println(((tag == null || tag.isEmpty()) ? "" : "[" + tag + "] ") + message);
    }

    public static void log(String tag, String message) {
        println(tag, message, System.out);
    }

    public static void log(String message) {
        log(null, message);
    }

    public static void debug(String tag, String message) {
        if (Global.DEBUG)
            println(tag, message, System.err);
    }

    public static void debug(String message) {
        debug(null, message);
    }
}
