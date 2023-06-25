package client.utility;

import client.commandLine.BlankConsole;
import client.commandLine.Printable;
import client.gui.GuiManager;
import common.exceptions.*;
import common.exceptions.ConnectionErrorException;
import common.util.ClientRequest;
import common.util.ResponseCode;
import common.util.ServerResponse;
import common.util.User;

import javax.swing.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * manages all actions to run Client
 */
public class ClientManager  implements Runnable{
    private ResourceBundle resourceBundle = ResourceBundle.getBundle("gui", GuiManager.getLocale());

    private String host;
    private int port;
    private int reconnectionTimeout;
    private int reconnectionAttempts;
    private int maxReconnectionAttempts;
    private SocketChannel socketChannel;
    private ObjectOutputStream serverWriter;
    private ObjectInputStream serverReader;
    private User user;
    private Locale locale;
    private Printable console = new BlankConsole();


    public ClientManager(String host, int port, int reconnectionTimeout, int maxReconnectionAttempts) {
        this.host = host;
        this.port = port;
        this.reconnectionTimeout = reconnectionTimeout;
        this.maxReconnectionAttempts = maxReconnectionAttempts;
    }

    /**
     * Begins main.client operation.
     */
    /*
    public void run() {
        try {
            while (true) {
                try {
                    connectToServer();
                    //processAuthentication();
                    //processRequestToServer(new ClientRequest(user, locale));
                    break;
                } catch (ConnectionErrorException exception) {
                    if (reconnectionAttempts >= maxReconnectionAttempts) {
                        console.printError("Too much attempts of reconnection");
                        break;
                    }
                    try {
                        Thread.sleep(reconnectionTimeout);
                    } catch (IllegalArgumentException timeoutException) {
                        console.printError("Connection time out '" + reconnectionTimeout +
                                "' is beyond possible values");
                        console.println("New reconnection will be started right now.");
                    } catch (Exception timeoutException) {
                        console.printError("Mistake occurred while trying to connect");
                        console.println("New reconnection will be started right now.");
                    }
                }
                reconnectionAttempts++;
            }
            if (socketChannel != null) socketChannel.close();
            console.println("Work of main.client is ended.");
        } catch (NotDeclaredLimitsException exception) {
            console.printError("Client cannot be started");
        } catch (IOException exception) {
            console.printError("Mistake occurred while trying to connect");
        }
    }
    */
    @Override
    public void run() {
        try {
            connectToServer();
        } catch (NotDeclaredLimitsException exception) {
            console.printError("ClientException");
            System.exit(0);
        } catch (ConnectionErrorException exception) { /* ? */ }
    }

    /**
     * Connecting to main.client.server.
     */
    private void connectToServer() throws ConnectionErrorException, NotDeclaredLimitsException {
        try {
            if (reconnectionAttempts >= 1) console.println("Reconnecting ...");
            socketChannel = SocketChannel.open(new InetSocketAddress(host, port));
            console.println("Connection with server is set successfully.");
            console.println("Waiting for permission to exchange data ...");
            serverWriter = new ObjectOutputStream(socketChannel.socket().getOutputStream());
            serverReader = new ObjectInputStream(socketChannel.socket().getInputStream());
            console.println("Permission to exchange data is received.");
        } catch (IllegalArgumentException exception) {
            console.printError("Incorrect address of main.client.server");
            throw new NotDeclaredLimitsException();
        } catch (IOException exception) {
            console.printError("Mistake occurred while trying to connect to main.client.server");
            throw new ConnectionErrorException();
        }
    }

    /**
     * server.Server request process.
     */
    public ServerResponse processRequestToServer(ClientRequest requestToServer) throws ConnectionErrorException, NotDeclaredLimitsException {
        ServerResponse serverResponse = null;
        try {
            if (Objects.isNull(serverWriter) || Objects.isNull(serverReader)) throw new IOException();
            if (requestToServer.isEmpty())
                return new ServerResponse(resourceBundle.getString("EmptyRequest"), ResponseCode.ERROR);
            System.out.println(requestToServer.getCommandName());
            //if (requestToServer.isEmpty()) continue;
            serverWriter.writeObject(requestToServer);
            serverWriter.flush();
             serverResponse = (ServerResponse) serverReader.readObject();
            //console.println(serverResponse.getMessage());
            System.out.println(serverResponse);
        } catch (IOException e) {
            if (reconnectionAttempts == 0) {
                connectToServer();
                reconnectionAttempts++;
            }
            try {
                reconnectionAttempts++;
                if (reconnectionAttempts >= maxReconnectionAttempts) {
                    JOptionPane.showMessageDialog(null,
                            resourceBundle.getString("ToMuchTries"),
                            resourceBundle.getString("ServerNotAvailable"),
                            JOptionPane.ERROR_MESSAGE);
                    System.exit(666);
                }
                AtomicInteger seconds = new AtomicInteger(reconnectionTimeout);
                JOptionPane optionPane = new JOptionPane(resourceBundle.getString("ServerConnectionBreaked"));
                JDialog dialog = optionPane.createDialog(null, resourceBundle.getString("ServerConnectionBreaked"));
                dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
                new Timer(1000, (i) -> {
                    seconds.decrementAndGet();
                    if (seconds.get() <= 0) {
                        dialog.dispose();
                    } else {
                        optionPane.setMessage(MessageFormat.format(
                                resourceBundle.getString("NextTryIn") + seconds, resourceBundle.getString("ServerConnectionBreaked"), seconds));
                    }
                }).start();
                dialog.setVisible(true);
                connectToServer();
            } catch (Exception exception) {
                console.printError("Попытка соединения с сервером неуспешна");
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return serverResponse == null? null: serverResponse ;
    }


}