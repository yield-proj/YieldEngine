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

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * It loads all the files in a from directory and its subdirectories and stores them in a map
 */
public final class Assets {
    private Map<String, RelativeFile> files = new HashMap<>();
    private final YldGame game;

    public Assets(File file, Class<? extends YldScene> scene, YldProgressScene progressScene, YldGame game) {
        this.game = game;
        if (file != null)
            addFile(file, file.getParent(), scene.getSimpleName(), progressScene);
        URL shared = scene.getResource("/shared");
        if (shared != null) {
            try {
                File sharedFile = new File(shared.toURI());
                addFile(sharedFile, sharedFile.getParent(), "shared", null);
            } catch (URISyntaxException e) {
                Yld.throwException(new RuntimeException(e));
            }
        }
    }

    /**
     * It adds a file to the game's file manager
     *
     * @param f              The file to add
     * @param removePath     The path to remove from the file name.
     * @param removeFromName This is the path that will be removed from the saved file name.
     */
    public void addFile(File f, String removePath, String removeFromName, YldProgressScene progressScene) {
        if (f.isDirectory()) {
            File[] fs = Objects.requireNonNull(f.listFiles());
            float progress = 1f / (fs.length - 1);
            for (int i = 0; i < fs.length; i++) {
                File f1 = fs[i];
                if (progressScene != null)
                    progressScene.setProgress(progress * i);
                addFile(f1, removePath, removeFromName, null);
            }
        } else {
            try {
                Class<?> c = Class.forName("com.xebisco.yield.RelativeFile");
                if (f.getName().contains(".")) {
                    Extension extension = Extension.valueOf(f.getName().split("\\.")[1].toUpperCase());
                    switch (extension) {
                        case JPG:
                        case PNG:
                        case JPEG:
                            c = Class.forName("com.xebisco.yield.Texture");
                            break;
                        case MP3:
                        case WAV:
                            c = Class.forName("com.xebisco.yield.AudioClip");
                            break;
                        case INI:
                            c = Class.forName("com.xebisco.yield.IniFileWrapper");
                            break;
                        case JSON:
                            c = Class.forName("com.xebisco.yield.JsonFileWrapper");
                            break;
                        default:
                            c = Class.forName("com.xebisco.yield.TextFile");
                            break;
                    }
                }
                RelativeFile r;
                String name = f.getName().replace(removeFromName, ""), path = f.getPath().replace(removePath, "");
                if (f.getName().contains("@")) {
                    String type = name.split("@")[1].split("\\.")[0];
                    r = (RelativeFile) c.getConstructor(String.class, Class.class).newInstance(path, Class.forName(type.replace("-", ".")));
                    name = name.replace("@" + type, "");
                } else {
                    r = (RelativeFile) c.getConstructor(String.class).newInstance(path);
                }
                if (r instanceof Texture)
                    game.loadTexture((Texture) r);
                name = path.replace("\\" + removeFromName + "\\", "").replace(f.getName(), "").replace("\\", "/").concat(name);
                files.put(name, r);
            } catch (ClassNotFoundException | InvocationTargetException | InstantiationException |
                     IllegalAccessException | NoSuchMethodException e) {
                Yld.throwException(new RuntimeException(e));
            }
        }
    }

    /**
     * Get the file with the given name.
     *
     * @param name The name of the file.
     * @return A file object
     */
    public RelativeFile get(String name) {
        return files.get(name);
    }

    /**
     * Get the file with the given name, and cast it to the given type.
     *
     * @param name The name of the file.
     * @param type The type of the file you want to get.
     * @return A file of the specified type.
     */
    public <T extends RelativeFile> T get(String name, Class<T> type) {
        return type.cast(files.get(name));
    }

    /**
     * Get the texture with the given name.
     *
     * @param name The name of the resource.
     * @return A Texture object
     */
    public Texture getTexture(String name) {
        return get(name, Texture.class);
    }

    /**
     * Get the audio clip with the given name.
     *
     * @param name The name of the resource.
     * @return An AudioClip object
     */
    public AudioClip getAudioClip(String name) {
        return get(name, AudioClip.class);
    }

    /**
     * Get the text file with the given name.
     *
     * @param name The name of the file.
     * @return A TextFile object
     */
    public TextFile getTextFile(String name) {
        return get(name, TextFile.class);
    }

    /**
     * Get the ini file with the given name.
     *
     * @param name The name of the file.
     * @return A IniFileWrapper object
     */
    public IniFileWrapper getIniFile(String name) {
        return get(name, IniFileWrapper.class);
    }

    /**
     * Get the json file with the given name.
     *
     * @param name The name of the file.
     * @return A JsonFileWrapper object
     */
    public <T> JsonFileWrapper getJsonFile(String name, Class<T> type) {
        return get(name, JsonFileWrapper.class);
    }

    /**
     * This function returns a map of all the files in the assets of the scene.
     *
     * @return A map of relative files.
     */
    public Map<String, RelativeFile> getFiles() {
        return files;
    }

    /**
     * This function sets the files variable to the files variable passed in.
     *
     * @param files A map of file names to RelativeFile objects.
     */
    public void setFiles(Map<String, RelativeFile> files) {
        this.files = files;
    }

    /**
     * This function returns the game object.
     *
     * @return The game object.
     */
    public YldGame getGame() {
        return game;
    }
}
