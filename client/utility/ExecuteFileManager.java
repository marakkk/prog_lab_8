package client.utility;

import client.commandLine.BlankConsole;
import client.commandLine.Printable;
import client.commandLine.UserInput;
import client.gui.GuiManager;
import client.commandLine.forms.TicketForm;
import common.exceptions.*;
import common.models.Ticket;
import common.util.*;

import java.io.*;
import java.text.MessageFormat;
import java.util.ArrayDeque;
import java.util.Objects;
import java.util.ResourceBundle;

public class ExecuteFileManager implements UserInput {
    private static final ArrayDeque<String> pathQueue = new ArrayDeque<>();
    private static final ArrayDeque<BufferedReader> fileReaders = new ArrayDeque<>();
    private static ResourceBundle resourceBundle = ResourceBundle.getBundle("gui", GuiManager.getLocale());


    private final ClientManager client;
    private User user;

    private Printable console;
    public ExecuteFileManager() {
        this.console = new BlankConsole();
        this.client = null;
        this.user = null;
    }
    public ExecuteFileManager(Printable console, ClientManager client, User user) {
        this.console = console;
        this.client = client;
        this.user = user;
    }

    public void fileExecution(String args) throws ExitObliged, LoginDuringExecuteFail {
        if (args == null || args.isEmpty()) {
            console.printError(resourceBundle.getString("ErrorFile"));
            return;
        }
        else console.println(ConsoleColors.toColor(resourceBundle.getString("FileGot"), ConsoleColors.PURPLE));
        args = args.trim();
        try {
            ExecuteFileManager.pushFile(args);
            for (String line = ExecuteFileManager.readLine(); line != null; line = ExecuteFileManager.readLine()) {
                String[] userCommand = (line + " ").split(" ", 2);
                if (userCommand[0].isBlank()) return;
                userCommand[0] = userCommand[0].trim();
                userCommand[1] = userCommand[1].trim();
                if (userCommand[0].equals("execute_script")){
                    if(ExecuteFileManager.fileRepeat(userCommand[1])){
                        console.printError(MessageFormat.format(resourceBundle.getString("FoundRecursion"), new File(userCommand[1]).getAbsolutePath()));
                        continue;
                    }
                }
                console.println(ConsoleColors.toColor(resourceBundle.getString("DoingCommand") + userCommand[0], ConsoleColors.YELLOW));
                ServerResponse response = (ServerResponse) client.processRequestToServer(new ClientRequest(userCommand[0], userCommand[1], user, GuiManager.getLocale()));
                this.printResponse(response);
                switch (response.getResponseCode()){
                    case ASK_OBJECT -> {
                        Ticket ticket;
                        try{
                            ticket = new TicketForm(console).build();
                            if (!ticket.validate()) throw new ExceptionInFileMode();
                        } catch (ExceptionInFileMode err){
                            console.printError(resourceBundle.getString("VariablesNotValid"));
                            continue;
                        }
                        ServerResponse newResponse = (ServerResponse) client.processRequestToServer(
                                new ClientRequest(
                                        userCommand[0].trim(),
                                        userCommand[1].trim(),
                                        ticket,
                                        user,
                                        GuiManager.getLocale()));
                        if (newResponse.getResponseCode() != ResponseCode.OK){
                            console.printError(String.valueOf(newResponse.getResponseCode()));
                        }
                        else {
                            this.printResponse(newResponse);
                        }
                    }
                    case EXIT -> throw new ExitObliged();
                    case EXECUTE_SCRIPT -> {
                        this.fileExecution(response.toString());
                    }
                    case LOGIN_FAILED -> {
                        console.printError("Ошибка с вашим аккаунтом. Зайдите в него снова");
                        this.user = null;
                        throw new LoginDuringExecuteFail();
                    }
                    default -> {}
                }
            }
            ExecuteFileManager.popFile();
        } catch (FileNotFoundException fileNotFoundException){
            console.printError(resourceBundle.getString("FileNotExists"));
        } catch (IOException | ConnectionErrorException | NotDeclaredLimitsException e) {
            console.printError("Ошибка ввода вывода");
        }
    }

    private void printResponse(ServerResponse response){
        switch (response.getResponseCode()){
            case OK -> {
                if ((Objects.isNull(response.getCollection()))) {
                    console.println(response.toString());
                } else {
                    console.println(response.toString() + "\n" + response.getCollection().toString());
                }
            }
            case ERROR -> console.printError(response.toString());
            case WRONG_ARGUMENTS -> console.printError("Неверное использование команды!");
            default -> {}
        }
    }

    public static void pushFile(String path) throws FileNotFoundException {
        pathQueue.push(new File(path).getAbsolutePath());
        fileReaders.push(new BufferedReader(new InputStreamReader(new FileInputStream(path))));
    }

    public static File getFile() {
        return new File(pathQueue.getFirst());
    }

    public static String readLine() throws IOException {
        return fileReaders.getFirst().readLine();
    }
    public static void popFile() throws IOException {
        fileReaders.getFirst().close();
        fileReaders.pop();
        if(pathQueue.size() >= 1) {
            pathQueue.pop();
        }
    }

    public static void popRecursion(){
        if(pathQueue.size() >= 1) {
            pathQueue.pop();
        }
    }

    public static boolean fileRepeat(String path){
        return pathQueue.contains(new File(path).getAbsolutePath());
    }

    @Override
    public String nextLine() {
        try{
            return readLine();
        } catch (IOException e){
            return "";
        }
    }
}
