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

package com.xebisco.yieldengine.editor.runtime;

import com.xebisco.yieldengine.manager.FileIOManager;
import com.xebisco.yieldengine.assets.decompressing.AssetsDecompressing;

import java.io.IOException;

public class EditorIOManager implements FileIOManager {
    private final AssetsDecompressing assetsDecompressing;

    public EditorIOManager(AssetsDecompressing assetsDecompressing) {
        this.assetsDecompressing = assetsDecompressing;
    }

    @Override
    public String loadPath(String fileName) {
        try {
            return assetsDecompressing.getFile(fileName).getAbsolutePath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void releaseFile(String fileName) {
        try {
            assetsDecompressing.releaseFile(fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
