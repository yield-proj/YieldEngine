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

package com.xebisco.yield.editor.prop;

import javax.swing.*;

public final class Props {
    public static Prop[] newProject() {
        return new Prop[] {
                new StringProp("Project Name", ""),
                new PathProp("Project Location" , System.getProperty("user.home") + "\\", JFileChooser.DIRECTORIES_ONLY),
                new ImageProp("Project Icon"),
                new BooleanProp("Create sample scene", true)
        };
    }

    public static Prop[] newScene() {
        return new Prop[] {
                new StringProp("Name", "")
        };
    }

    public static Prop get(Prop[] props, String name) {
        for(Prop p : props)
            if(p.getName().equals(name)) return p;
        return null;
    }

}
