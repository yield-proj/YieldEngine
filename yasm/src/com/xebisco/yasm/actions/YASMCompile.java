package com.xebisco.yasm.actions;

import com.xebisco.yasm.Program;
import com.xebisco.yasm.Rd;
import com.xebisco.yasm.UnknownCallException;
import com.xebisco.yasm.calls.AllCalls;

import java.io.*;
import java.util.zip.DeflaterOutputStream;

public class YASMCompile {
    public static void main(String[] args) throws UnknownCallException, FileNotFoundException {
        Program program = Rd.program(readFromInputStream(new BufferedInputStream(new FileInputStream(args[0]))), AllCalls.calls());
        File newFile = new File("a.yxc");
        try(ObjectOutputStream oo = new ObjectOutputStream(new DeflaterOutputStream(new FileOutputStream(newFile)))) {
            oo.writeObject(program);
        } catch (IOException e) {
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
