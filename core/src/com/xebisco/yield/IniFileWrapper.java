/*
 * Copyright [2022] [Xebisco]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xebisco.yield;

import net.vanguarda.sini4j.Ini;

/**
 * It's a wrapper for an INI file that extends the RelativeFile class
 */
public class IniFileWrapper extends RelativeFile {
    private final Ini ini;

    public IniFileWrapper(String relativePath) {
        super(relativePath);
        ini = new net.vanguarda.sini4j.Ini(getInputStream());
    }

    /**
     * This function returns the Ini object.
     *
     * @return The ini object.
     */
    public Ini getIni() {
        return ini;
    }
}
