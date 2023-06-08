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

package com.xebisco.yield.editor;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.Random;

public class Project implements Serializable {
    private String name;
    private final File projectLocation;
    private final int logoVariation = new Random().nextInt(3);
    private final Date createdDate = new Date();

    public Project(String name, File projectLocation) {
        this.name = name;
        this.projectLocation = projectLocation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public File getProjectLocation() {
        return projectLocation;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public int getLogoVariation() {
        return logoVariation;
    }
}
