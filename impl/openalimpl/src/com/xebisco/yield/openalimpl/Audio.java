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

package com.xebisco.yield.openalimpl;

import java.nio.ByteBuffer;

public class Audio {
    private int[] buffer = new int[1];;
    private int[] source = new int[1];
    private final int[] format = new int[1];
    private final int[] size = new int[1];
    private final ByteBuffer[] data = new ByteBuffer[1];
    private final int[] freq = new int[1];
    private final int[] loop = new int[1];

    public int[] getFormat() {
        return format;
    }

    public int[] getSize() {
        return size;
    }

    public ByteBuffer[] getData() {
        return data;
    }

    public int[] getFreq() {
        return freq;
    }

    public int[] getLoop() {
        return loop;
    }

    public int[] getBuffer() {
        return buffer;
    }

    public int[] getSource() {
        return source;
    }
}
