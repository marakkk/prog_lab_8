package client.commandLine;

import common.util.ConsoleColors;

/**
 * Operates command input.
 */
public class Console {
    private static boolean fileMode = false;

    public static boolean isFileMode() {
        return fileMode;
    }

    public static void setFileMode(boolean fileMode) {
        Console.fileMode = fileMode;
    }

    public static void println(String a) {
        System.out.println(a);
    }


    public static void print(String a) {
        System.out.print(a);
    }

    public static void printError(String a) {
        System.out.println(ConsoleColors.RED + a + ConsoleColors.RESET);
    }
}