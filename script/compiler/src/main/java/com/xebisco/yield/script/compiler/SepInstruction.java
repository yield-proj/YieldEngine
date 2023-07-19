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

import java.util.List;

public class SepInstruction {
    private final String text;
    private final List<SepInstruction> order, arguments;

    private final boolean parenthesis;
    private boolean ignoreCompiling;

    public SepInstruction(final List<SepInstruction> order, final boolean parenthesis) {
        this.parenthesis = parenthesis;
        this.arguments = null;
        this.text = null;
        this.order = order;
    }

    public SepInstruction(final String text, final boolean parenthesis) {
        this.text = text;
        this.arguments = null;
        this.parenthesis = parenthesis;
        this.order = null;
    }

    public SepInstruction(final List<SepInstruction> arguments) {
        this.arguments = arguments;
        this.order = null;
        this.text = null;
        this.parenthesis = true;
    }

    public String text() {
        return this.text;
    }

    public List<SepInstruction> order() {
        return this.order;
    }

    public List<SepInstruction> arguments() {
        return arguments;
    }

    public boolean parenthesis() {
        return parenthesis;
    }

    public boolean ignoreCompiling() {
        return ignoreCompiling;
    }

    public SepInstruction setIgnoreCompiling(boolean ignoreCompiling) {
        this.ignoreCompiling = ignoreCompiling;
        return this;
    }
}