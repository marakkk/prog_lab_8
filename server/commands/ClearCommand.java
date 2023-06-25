package server.commands;

import client.commandLine.BlankConsole;
import common.exceptions.*;
import common.models.Ticket;
import common.util.ClientRequest;
import common.util.ResponseCode;
import common.util.ServerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.utility.CollectionManager;
import server.utility.DatabaseCollectionHandler;
import server.utility.ResponseOutputer;

import java.util.ResourceBundle;


/**
 * Command 'clear'. Saves the collection to a file.
 */
public class ClearCommand extends AbstractCommand{
    private CollectionManager collectionManager;
    private DatabaseCollectionHandler databaseCollectionHandler;
    private Logger logger = LoggerFactory.getLogger("Clear command");
    private BlankConsole console;


    public ClearCommand(CollectionManager collectionManager, DatabaseCollectionHandler databaseCollectionHandler) {
     super("clear", "clear collection");
     this.collectionManager = collectionManager;
     this.databaseCollectionHandler = databaseCollectionHandler;
    }

    /**
     * Execute of 'clear' command.
     */

    @Override
    public ServerResponse execute(ClientRequest request) throws NoSuchCommandException {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("Response", request.getLocale());

        try {
            if (!request.getCommandArguments().isEmpty() || request.getObjectArgument()!= null) throw new WrongAmountOfElementsException();
            for (Ticket ticket : collectionManager.getCollection()) {
                if (!request.getUser().equals(ticket.getUserLogin())) throw new PermissionDeniedException();
                if (databaseCollectionHandler.checkTicketUserId(ticket.getId(), request.getUser())) throw new ManualDatabaseEditException();
            }
            databaseCollectionHandler.clearCollection();
            collectionManager.clearCollection();
            ResponseOutputer.appendln("Collection is clear");
            logger.debug("Collection is clear");
        } catch (WrongAmountOfElementsException exception) {
            console.println("Used: '" + getName() + "'");
        } catch (PermissionDeniedException | DatabaseHandlingException | ManualDatabaseEditException e) {
            console.printError(e.getMessage());
        }
        return new ServerResponse(resourceBundle.getString("objAvr"), ResponseCode.OK);
    }
}