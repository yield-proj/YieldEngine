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

import com.xebisco.yieldengine.*;
import com.xebisco.yieldengine.assets.decompressing.AssetsDecompressing;
import com.xebisco.yieldengine.editor.runtime.pack.EditorComponent;
import com.xebisco.yieldengine.editor.runtime.pack.EditorEntity;
import com.xebisco.yieldengine.editor.runtime.pack.EditorScene;
import com.xebisco.yieldengine.texture.TexturedRectangleMesh;
import com.xebisco.yieldengine.utils.Loading;

import java.io.*;
import java.lang.reflect.InvocationTargetException;

public final class Scenes {
    private static AssetsDecompressing SCENES_DECOMPRESSING;

    static void start() throws IOException {
        SCENES_DECOMPRESSING = new AssetsDecompressing(new File("scenes"));
    }

    public static Scene load(String sceneName, Application app) {
        try {
            File sceneFile = SCENES_DECOMPRESSING.getFile(sceneName);
            EditorScene editorScene;
            try (ObjectInputStream oi = new ObjectInputStream(new FileInputStream(sceneFile))) {
                editorScene = (EditorScene) oi.readObject();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            sceneFile.delete();
            Scene scene = new Scene(app) {
                @Override
                public void onStart() {
                    int entityIndex = 0;
                    for (EditorEntity entity : editorScene.entities()) {
                        ComponentCreation[] comps = new ComponentCreation[entity.components().size() - 1];
                        for (int i = 0; i < comps.length; i++) {
                            EditorComponent comp = entity.components().get(i + 1);
                            try {
                                //noinspection unchecked
                                comps[i] = new ComponentCreation((Class<? extends ComponentBehavior>) Scenes.class.getClassLoader().loadClass(comp.className()), component -> {
                                    Object[] extras = null;
                                    if(component instanceof TexturedRectangleMesh)
                                        extras = new Object[] {application().applicationPlatform().textureManager(), application().applicationPlatform().ioManager()};
                                    else if(component instanceof TextMesh)
                                        extras = new Object[] {application().applicationPlatform().fontManager(), application().applicationPlatform().ioManager()};
                                    try {
                                        Loading.applyPropsToObject(comp.fields(), component, extras);
                                    } catch (NoSuchFieldException | IllegalAccessException |
                                             NoSuchMethodException | InvocationTargetException | InstantiationException ex) {
                                        throw new RuntimeException(ex);
                                    }
                                });
                            } catch (ClassNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        int index = entityIndex++;
                        instantiate(new Entity2DPrefab(comps), e -> {
                            //Transform2D
                            try {
                                Loading.applyPropsToObject(entity.components().get(0).fields(), e.transform());
                                e.transform().position().multiplyLocal(2);
                            } catch (NoSuchFieldException | IllegalAccessException |
                                     NoSuchMethodException | InvocationTargetException | InstantiationException ex) {
                                throw new RuntimeException(ex);
                            }
                            e.setRenderIndex(index);
                        });
                    }
                }
            };
            scene.setBackGroundColor(new Color(editorScene.backgroundColor(), Color.Format.RGB));
            return scene;
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("no scene with name: '" + sceneName + "'");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static void close() {
        SCENES_DECOMPRESSING.close();
    }
}
