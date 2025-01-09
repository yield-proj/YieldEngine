package com.xebisco.yieldengine.utils;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Logger {
    public static boolean DEBUG = true;
    private static long startTime = System.nanoTime();
    public static final Pattern CLASS_NAME_PATTERN = Pattern.compile("[.$]([^.$]+)$");

    public static void log(String msg) {
        System.out.println(msg);
    }

    public static void debug(String msg) {
        if (!DEBUG) return;
        StackTraceElement element = Thread.currentThread().getStackTrace()[2];
        Matcher matcher = CLASS_NAME_PATTERN.matcher(element.getClassName());
        String className = "";
        if (matcher.find())
            className = matcher.group(1);
        System.err.println('(' + getExecutionTimeString() + ") " + className + '.' + element.getMethodName() + ": " + msg);
    }

    public static String getExecutionTimeString() {
        double seconds = getExecutionTime() / 1_000_000_000f;
        int minutes = 0;
        while (seconds >= 60) {
            seconds -= 60;
            minutes++;
        }
        int hours = 0;
        while (minutes >= 60) {
            minutes -= 60;
            hours++;
        }

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(2);
        df.setMinimumIntegerDigits(2);

        return String.format("%02d:%02d:%s", hours, minutes, df.format(seconds));
    }

    public static long getExecutionTime() {
        if (getStartTime() == 0) return 0;
        return System.nanoTime() - getStartTime();
    }

    public static long getStartTime() {
        return startTime;
    }

    public static void setStartTime(long startTime) {
        Logger.startTime = startTime;
    }
}
