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

import com.xebisco.yieldengine.Scene;
import com.xebisco.yieldengine.assets.decompressing.AssetsDecompressing;
import com.xebisco.yieldengine.manager.ApplicationManager;
import com.xebisco.yieldengine.utils.Pair;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

public class Server {
    public static void main(String[] args) throws IOException, NoSuchFieldException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, InterruptedException {
        ServerSocket server = new ServerSocket(0);
        System.out.println("Started server on port: " + server.getLocalPort());
        InetAddress inet = server.getInetAddress();
        System.out.println("HostAddress = " + inet.getHostAddress());
        System.out.println("HostName = " + inet.getHostName());

        Pair<ApplicationManager, AssetsDecompressing> app = null;
        AtomicBoolean alive = new AtomicBoolean(true);

        while (true) {
            try {
                Socket client = server.accept();
                try {
                    ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
                    String msg = ((String) ois.readObject());
                    Object out = null;
                    switch (msg) {
                        case "exit" -> {
                            if (app != null) {
                                app.first().managerContext().running().set(false);
                                app.second().close();
                                Scenes.close();
                                alive.set(false);
                            }
                        }
                        case "pause" -> {
                            if (app != null) {
                                app.first().managerContext().pause();
                            }
                        }
                        case "resume" -> {
                            if (app != null) {
                                app.first().managerContext().resume();
                            }
                        }
                        case "start" -> {
                            app = Launcher.applicationManager(args);
                            Pair<ApplicationManager, AssetsDecompressing> finalApp = app;
                            CompletableFuture.runAsync(() -> {
                                finalApp.first().runAndWait();
                                alive.set(false);
                            }).exceptionally(ex -> {
                                throw new RuntimeException(ex);
                            });
                        }
                        case "running" -> {
                            if (app != null) {
                                try {
                                    out = app.first().managerContext().running().get();
                                } catch (Exception e) {
                                    out = false;
                                }
                            }
                        }
                        case "scene" -> {
                            if (app != null) {
                                out = app.first().applications().get(0).scene();
                            }
                        }
                    }
                    ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
                    oos.writeObject(out);
                    if (!alive.get()) break;
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            } catch (SocketException ignore) {

            }
        }

        System.out.println("Shutting down server...");

        Thread.sleep(2000);

        server.close();
    }
}
