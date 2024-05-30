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

package com.xebisco.yieldengine.save;

import com.xebisco.yieldengine.*;

import java.io.*;
import java.util.Date;

public class SaveManager {
    public static void saveSlot(byte slot, SaveObject saveObject) {
        checkSaveId();
        saveObject.setModifiedDate(new Date());
        String appdata = Global.defaultDirectory() + "/.yieldappdata";
        File dataDir = new File(appdata);
        if(!dataDir.exists()) {
            dataDir.mkdir();
        }
        File dataFile = new File(dataDir, Global.APP_SAVE_ID + "slot" + slot + ".save");
        try(ObjectOutputStream os = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(dataFile)))) {
            os.writeObject(saveObject);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void checkSaveId() {
        if(Global.APP_SAVE_ID == null)
            throw new IllegalStateException("The app does not have a APP_SAVE_ID");
    }

    public static SaveObject getSlot(byte slot) {
        checkSaveId();
        String appdata = Global.defaultDirectory() + "/.yieldappdata";
        File dataDir = new File(appdata);
        if(!dataDir.exists()) {
            dataDir.mkdir();
        }
        File dataFile = new File(dataDir, Global.APP_SAVE_ID + "slot" + slot + ".save");
        try(ObjectInputStream oi = new ObjectInputStream(new BufferedInputStream(new FileInputStream(dataFile)))) {
            return (SaveObject) oi.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
