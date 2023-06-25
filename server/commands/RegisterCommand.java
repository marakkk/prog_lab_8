package server.commands;

import client.commandLine.BlankConsole;
import client.commandLine.Printable;
import common.exceptions.DatabaseHandlingException;
import common.exceptions.UserAlreadyExistException;
import common.exceptions.WrongAmountOfElementsException;
import common.util.ClientRequest;
import common.util.ResponseCode;
import common.util.ServerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.utility.*;

public class RegisterCommand extends AbstractCommand{
    private DatabaseUserManager databaseUserManager;
    private Printable console = new BlankConsole();
    private Logger logger = LoggerFactory.getLogger("Register command");

    public RegisterCommand(DatabaseUserManager databaseUserManager) {
        super("register",  "внутренняя команда");
        this.databaseUserManager = databaseUserManager;
    }

    /**
     * Executes the command.
     */
    @Override
    public ServerResponse execute(ClientRequest request) {
        try {
            if (!request.getCommandArguments().isEmpty() || request.getObjectArgument() != null) throw new WrongAmountOfElementsException();
            if (databaseUserManager.insertUser(request.getUser())) {
                ResponseOutputer.appendln("User " +
                        request.getUser() + " is registered.");

                logger.debug("User " +
                        request.getUser() + " is registered.");
            }
            else throw new UserAlreadyExistException();
        } catch (WrongAmountOfElementsException exception) {
            console.printError("Использование: эммм...эээ.это внутренняя команда...");
        } catch (ClassCastException exception) {
            console.printError("Переданный клиентом объект неверен!");
        } catch (DatabaseHandlingException exception) {
            console.printError(exception.getMessage());
            console.printError("Произошла ошибка при обращении к базе данных!");
        } catch (UserAlreadyExistException exception) {
            console.printError("Пользователь " + request.getUser() + " уже существует!");
        }
        return new ServerResponse("", ResponseCode.OK);
    }
}
