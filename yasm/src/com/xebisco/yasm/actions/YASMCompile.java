package com.xebisco.yasm.actions;

import com.xebisco.yasm.Program;
import com.xebisco.yasm.Rd;
import com.xebisco.yasm.UnknownCallException;
import com.xebisco.yasm.AllCalls;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.DeflaterOutputStream;

public class YASMCompile {

    public static void main(String[] args) throws UnknownCallException, IOException {
        if (args.length == 0) {
            printTutorial();
        }

        boolean library = false;
        String output = "a";

        Pattern argP = Pattern.compile("^(\\w+)=(\\S+)$");

        for (int i = 1; i < args.length; i++) {
            String arg = args[i];
            Matcher matcher = argP.matcher(arg);
            if (matcher.matches()) {
                switch (matcher.group(1)) {
                    case "out" -> output = matcher.group(2);
                    case "lib" -> library = Boolean.parseBoolean(matcher.group(2));
                    default -> printTutorial();
                }
            } else {
                printTutorial();
            }
        }


        Program program = Rd.program(new File("."), readFromInputStream(new BufferedInputStream(new FileInputStream(args[0]))), AllCalls.calls());

        File outputFile = new File(output + "." + (library ? "ylib" : "yx"));

        OutputStream is = library ? new FileOutputStream(outputFile) : new DeflaterOutputStream(new FileOutputStream(outputFile));

        try (ObjectOutputStream oo = new ObjectOutputStream(is)) {
            oo.writeObject(program);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void printTutorial() {
        System.err.println("Incorect usage...");
        System.err.println("Example:");
        System.err.println("\tyasmc input.yasm");
        System.err.println("\tyasmc input.yasm out=output");
        System.err.println("\tyasmc input.yasm out=output lib=true");
        System.exit(1);
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
