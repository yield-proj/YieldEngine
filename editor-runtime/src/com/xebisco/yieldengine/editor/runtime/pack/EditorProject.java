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

package com.xebisco.yieldengine.editor.runtime.pack;

import com.xebisco.yieldengine.editor.annotations.Config;
import com.xebisco.yieldengine.editor.annotations.Visible;

import java.io.Serializable;

@Config
public class EditorProject implements Serializable {
    @Visible
    private String name, description = "", version = "1.0", startScene;

    public String name() {
        return name;
    }

    public EditorProject setName(String name) {
        this.name = name;
        return this;
    }

    public String description() {
        return description;
    }

    public EditorProject setDescription(String description) {
        this.description = description;
        return this;
    }

    public String version() {
        return version;
    }

    public EditorProject setVersion(String version) {
        this.version = version;
        return this;
    }

    public String startScene() {
        return startScene;
    }

    public EditorProject setStartScene(String startScene) {
        this.startScene = startScene;
        return this;
    }
}
