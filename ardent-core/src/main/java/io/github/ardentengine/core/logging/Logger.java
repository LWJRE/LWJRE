package io.github.ardentengine.core.logging;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

    // TODO: Implement file logging

    // TODO: Abstract the logger for different platforms

    private static String currentSystemTime() {
        return new SimpleDateFormat("HH:mm:ss").format(new Date(System.currentTimeMillis()));
    }

    public static void info(String message) {
        System.out.println("[" + Thread.currentThread().getStackTrace()[2].getClassName() + "] [" + currentSystemTime() + "] [Info]: " + message);
    }

    public static void warning(String message) {
        var stackTrace = Thread.currentThread().getStackTrace();
        System.out.println("\u001B[33m[" + stackTrace[2].getClassName() + "] [" + currentSystemTime() + "] [Warning]: " + message);
        for(var i = 2; i < stackTrace.length; i++) {
            System.out.println("\t" + stackTrace[i]);
        }
        System.out.print("\u001B[0m");
    }

    public static void warning(String message, Throwable exception) {
        System.out.println("\u001B[33m[" + Thread.currentThread().getStackTrace()[2].getClassName() + "] [" + currentSystemTime() + "] [Warning]: " + message);
        exception.printStackTrace(System.out);
        System.out.print("\u001B[0m");
    }

    public static void error(String message) {
        var stackTrace = Thread.currentThread().getStackTrace();
        System.out.println("\u001B[31m[" + stackTrace[2].getClassName() + "] [" + currentSystemTime() + "] [Error]: " + message);
        for(var i = 2; i < stackTrace.length; i++) {
            System.out.println("\t" + stackTrace[i]);
        }
        System.out.print("\u001B[0m");
    }

    public static void error(String message, Throwable exception) {
        System.out.println("\u001B[31m[" + Thread.currentThread().getStackTrace()[2].getClassName() + "] [" + currentSystemTime() + "] [Error]: " + message);
        exception.printStackTrace(System.out);
        System.out.print("\u001B[0m");
    }
}