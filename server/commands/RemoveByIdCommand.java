package server.commands;

import client.commandLine.BlankConsole;
import common.exceptions.DatabaseHandlingException;
import common.exceptions.ManualDatabaseEditException;
import common.exceptions.NoSuchCommandException;
import common.exceptions.PermissionDeniedException;
import common.models.Ticket;
import common.util.ClientRequest;
import common.util.ResponseCode;
import common.util.ServerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.utility.*;


/**
 * Command 'remove_by_id'. Saves the collection to a file.
 */
public class RemoveByIdCommand extends AbstractCommand{
    CollectionManager collectionManager;
    DatabaseCollectionHandler databaseCollectionHandler;
    private Logger logger = LoggerFactory.getLogger("Remove by id command");
    private BlankConsole console;

    public RemoveByIdCommand(CollectionManager collectionManager, DatabaseCollectionHandler databaseCollectionHandler){
        super("remove_by_id","delete an item from the collection by its id");
        this.collectionManager = collectionManager;
        this.databaseCollectionHandler = databaseCollectionHandler;
    }

    /**
     * Execute of 'remove_by_id' command.
     */


    @Override
    public ServerResponse execute(ClientRequest request) {
        if (request.getCommandArguments().isEmpty() || request.getObjectArgument() != null) {
            throw new NoSuchCommandException();
        }
        try {
            if (collectionManager.collectionSize() == 0) {
                console.println("Cannot remove object");
            }
            int id = Integer.parseInt(request.getCommandArguments());
            Ticket ticketToRemove = collectionManager.getById(id);

            if (ticketToRemove == null) {
                console.println("Cannot remove object");
            }
            if (ticketToRemove != null && !ticketToRemove.getUserLogin().equals(request.getUser())) throw new PermissionDeniedException();
            if (ticketToRemove != null && databaseCollectionHandler.checkTicketUserId(ticketToRemove.getId(), request.getUser()))
                throw new ManualDatabaseEditException();

            databaseCollectionHandler.deleteTicketById(id);
            collectionManager.removeFromCollection(ticketToRemove);
            ResponseOutputer.appendln("Ticket is deleted");
            logger.debug("Ticket " + ticketToRemove + " is deleted");
        } catch (NumberFormatException e) {
            console.println("the argument must be a long number");
        } catch (DatabaseHandlingException | ManualDatabaseEditException e) {
            console.printError(e.getMessage());
        } catch (PermissionDeniedException e) {
            throw new RuntimeException(e);
        }
        return new ServerResponse("", ResponseCode.OK);
    }

}
