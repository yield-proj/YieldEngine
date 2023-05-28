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

package com.xebisco.yield.ini;

import java.util.Properties;

/**
 * The IniProperties class extends the Properties class and provides methods to retrieve values of different data types
 * from properties.
 */
public class IniProperties extends Properties {
    public boolean getBoolean(String key) {
        return Boolean.parseBoolean(getProperty(key));
    }

    public int getInteger(String key) {
        return Integer.parseInt(getProperty(key));
    }

    public float getFloat(String key) {
        return Float.parseFloat(getProperty(key));
    }

    public double getDouble(String key) {
        return Double.parseDouble(getProperty(key));
    }

    public long getLong(String key) {
        return Long.parseLong(getProperty(key));
    }

    public byte getByte(String key) {
        return Byte.parseByte(getProperty(key));
    }

    public short getShort(String key) {
        return Short.parseShort(getProperty(key));
    }
}
