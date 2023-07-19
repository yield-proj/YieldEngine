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

package com.xebisco.yield.script;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Bank implements Serializable {
    private final Map<NameArgs, ScriptObject> objectMap;
    private final Bank parent;

    public Bank(final Bank parent) {
        this.objectMap = new HashMap<>();
        this.objectMap.put(new NameArgs("this", null), new ScriptObject(this, Bank.class, new DefaultValueProcess(), new SealedValueProcess()));
        this.parent = parent;
    }

    public Object get(final NameArgs nameArgs) {
        final ScriptObject o = this.objectMap.get(nameArgs);
        if (o != null)
            return o.value();
        if (parent != null)
            return parent.get(nameArgs);
        throw new MissingObjectException("Could not find object: " + nameArgs);
    }

    public ScriptObject getO(final NameArgs nameArgs) {
        final ScriptObject o = this.objectMap.get(nameArgs);
        if (o != null)
            return o;
        if (parent != null)
            return parent.getO(nameArgs);
        throw new MissingObjectException("Could not find object: " + nameArgs);
    }

    public Object attach(final Instruction[] instructions) {
        return attach(instructions, this);
    }

    private Object attach(final Instruction[] instructions, Bank bank) {
        for(Instruction instruction : instructions) {
            Object ret = instruction.run(bank);
            if(instruction instanceof ReturnInstruction)
                return ret;
        }
        return null;
    }

    public Object get(final String name) {
        return get(new NameArgs(name, null));
    }

    public Map<NameArgs, ScriptObject> objectMap() {
        return objectMap;
    }

    public Bank parent() {
        return parent;
    }
}