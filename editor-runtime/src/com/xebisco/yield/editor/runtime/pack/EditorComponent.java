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

package com.xebisco.yield.editor.runtime.pack;

import com.xebisco.yield.utils.Pair;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EditorComponent implements Serializable {
    @Serial
    private static final long serialVersionUID = -9027305243114159863L;
    private final List<Pair<Pair<String, String>, String[]>> fields = new ArrayList<>();
    private final String className;


    private boolean canRemove = true;

    public EditorComponent(String className) {
        this.className = className;
    }

    public List<Pair<Pair<String, String>, String[]>> fields() {
        return fields;
    }

    public EditorComponent sameAs(List<Pair<Pair<String, String>, String[]>> fields, EditorComponent editorComponent) {
        for (Pair<Pair<String, String>, String[]> field : fields) {
            editorComponent.fields.forEach(p -> {
                if (p.first().first().equals(field.first().first())) {
                    System.arraycopy(field.second(), 0, p.second(), 0, p.second().length);
                }
            });
        }
        return editorComponent;
    }

    public boolean canRemove() {
        return canRemove;
    }

    public EditorComponent setCanRemove(boolean canRemove) {
        this.canRemove = canRemove;
        return this;
    }

    public String className() {
        return className;
    }
}