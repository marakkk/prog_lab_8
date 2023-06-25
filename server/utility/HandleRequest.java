package server.utility;

import common.exceptions.*;
import common.models.Ticket;
import common.util.ClientRequest;
import common.util.ResponseCode;
import common.util.ServerResponse;
import common.util.User;
import server.commands.AbstractCommand;

import java.io.IOException;
import java.util.Collection;
import java.util.Comparator;
import java.util.concurrent.locks.ReentrantLock;


public class HandleRequest extends ReentrantLock {
    private CommandManager commandManager;
    private ServerResponse response;
    private CollectionManager collectionManager;

    public HandleRequest(CommandManager commandManager, CollectionManager collectionManager) {
        this.commandManager = commandManager;
        this.collectionManager = collectionManager;
    }

    protected ServerResponse compute(ClientRequest request) throws CommandUsageException, ExitObliged {

        executeCommand(request);
        return new ServerResponse(ResponseOutputer.getAndClear(), ResponseCode.OK, collectionManager.getCollection());
    }


    private synchronized ServerResponse executeCommand(ClientRequest request) throws CommandUsageException, ExitObliged {
        if (commandManager.getCommands().containsKey(request.getCommandName())) {
            AbstractCommand abstractCommand = commandManager.getCommands().get(request.getCommandName());
            try {
                 response = abstractCommand.execute(request);
            } catch (NoSuchCommandException e) {

                response = new ServerResponse("Unknown command detected: " + request.getCommandName(), ResponseCode.ERROR);

            } catch (WrongArgumentException | DatabaseHandlingException | IOException | WrongAmountOfElementsException e) {
                throw new RuntimeException(e);
            }
        } else {
            response = new ServerResponse("Unknown command detected: " + request.getCommandName(), ResponseCode.ERROR);

        }
        return new ServerResponse("", ResponseCode.OK, collectionManager.getCollection());
    }
}