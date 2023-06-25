package client;

import client.commandLine.BlankConsole;
import client.commandLine.Printable;
import client.gui.GuiManager;
import client.utility.*;
import common.exceptions.ConnectionErrorException;
import common.exceptions.NotDeclaredLimitsException;
import common.exceptions.WrongAmountOfElementsException;

import java.util.Scanner;

/**
 * Client application class. Creates all instances and runs the program.
 *
 * @author Кобзарь Мария P3115
 */

public class Client {

    private static final int RECONNECTION_TIMEOUT = 5 * 1000;
    private static final int MAX_RECONNECTION_ATTEMPTS = 5;

    private static String host;
    private static int port;
    private static Printable console = new BlankConsole();

    private static boolean initialize(String[] hostAndPortArgs) {
        try {

            if (hostAndPortArgs.length != 2) throw new WrongAmountOfElementsException();
            host = hostAndPortArgs[0];
            port = Integer.parseInt(hostAndPortArgs[1]);
            if (port < 0) throw new NotDeclaredLimitsException();
            return true;
        } catch (WrongAmountOfElementsException exception) {

            String jarName = new java.io.File(Client.class.getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .getPath())
                    .getName();
            console.println("Used: 'java -jar " + jarName + " <host> <port>'");
        } catch (NumberFormatException exception) {
            console.printError("Port has to be a number");
        } catch (NotDeclaredLimitsException exception) {
            console.printError("Port cannot be negative number");
        }
        return false;
    }

    public static void main(String[] args) throws ConnectionErrorException, NotDeclaredLimitsException {
        if (!initialize(args)) return;

        Scanner userScanner = new Scanner(System.in);

        ClientManager clientManager = new ClientManager(host, port, RECONNECTION_TIMEOUT, MAX_RECONNECTION_ATTEMPTS);
        clientManager.run();

        new GuiManager(clientManager);
        userScanner.close();

    }
}
