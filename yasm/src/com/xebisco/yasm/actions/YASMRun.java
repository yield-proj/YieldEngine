package com.xebisco.yasm.actions;

import com.xebisco.yasm.Program;
import com.xebisco.yasm.Rd;

import java.io.*;
import java.util.zip.InflaterInputStream;

public class YASMRun {
    public static void main(String[] args) throws IOException {
        try(ObjectInputStream oi = new ObjectInputStream(new InflaterInputStream(new FileInputStream(args[0])))) {
            Rd.run(((Program) oi.readObject()).global());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static String readFromInputStream(InputStream inputStream) {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return resultStringBuilder.toString();
    }
}
