package client.commandLine;

import client.utility.ScannerManager;

import java.util.Scanner;

public class ConsoleInput implements UserInput {
    private static final Scanner userScanner = ScannerManager.getUserScanner();

    @Override
    public String nextLine() {
        return userScanner.nextLine();
    }
}
