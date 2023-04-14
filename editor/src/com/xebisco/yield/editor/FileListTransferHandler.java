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

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class FileListTransferHandler extends TransferHandler {
    private FileList list;

    public FileListTransferHandler(FileList list) {
        this.list = list;
    }

    public int getSourceActions(JComponent c) {
        return COPY_OR_MOVE;
    }

    public boolean canImport(TransferSupport ts) {
        return ts.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
    }

    public boolean importData(TransferSupport ts) {
        try {
            //noinspection unchecked
            List<File> data = (List<File>) ts.getTransferable().getTransferData(
                    DataFlavor.javaFileListFlavor);
            if (data.size() < 1) {
                return false;
            }

            DefaultListModel<File> listModel = new DefaultListModel<>();

            for (int i = 0; i < list.getModel().getSize(); i++) {
                listModel.addElement(list.getModel().getElementAt(i));
            }

            for (File item : data) {
                File file = new File(list.getDirectory(), item.getName());

                CompletableFuture.runAsync(() -> {
                    try (FileInputStream fis = new FileInputStream(item); FileOutputStream fos = new FileOutputStream(file)) {

                        int c;
                        while ((c = fis.read()) != -1) {
                            fos.write(c);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });


                listModel.addElement(file);
            }

            list.setModel(listModel);
            return true;

        } catch (UnsupportedFlavorException | IOException e) {
            return false;
        }
    }

    public FileList getList() {
        return list;
    }

    public void setList(FileList list) {
        this.list = list;
    }
}