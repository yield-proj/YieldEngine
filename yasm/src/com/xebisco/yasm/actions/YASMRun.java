package com.xebisco.yasm.actions;

import com.xebisco.yasm.Program;
import com.xebisco.yasm.Rd;

import java.io.*;
import java.util.zip.InflaterInputStream;

public class YASMRun {
    public static void main(String[] args) throws IOException {
        try(ObjectInputStream oi = new ObjectInputStream(new InflaterInputStream(new FileInputStream(args[0] + ".yx")))) {
            Rd.run(((Program) oi.readObject()).global());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
