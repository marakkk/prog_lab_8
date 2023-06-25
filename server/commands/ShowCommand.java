package server.commands;

import client.commandLine.BlankConsole;
import common.exceptions.DatabaseHandlingException;
import common.exceptions.NoSuchCommandException;
import common.models.Ticket;
import common.util.ClientRequest;
import common.util.ResponseCode;
import common.util.ServerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.utility.*;

import java.util.Collection;

/**
 * This is command 'show'. Prints all elements of collection.
 */

public class ShowCommand extends AbstractCommand{
    private CollectionManager collectionManager;
    private Logger logger = LoggerFactory.getLogger("Show command");
    private DatabaseCollectionHandler databaseCollectionHandler;

    public ShowCommand(CollectionManager collectionManager, DatabaseCollectionHandler databaseCollectionHandler) {
        super("show", "show all collection's elements");
        this.collectionManager = collectionManager;
        this.databaseCollectionHandler = databaseCollectionHandler;
    }
    BlankConsole console;

    /**
     * Execute of 'show' command.
     */
    @Override
    public ServerResponse execute(ClientRequest request) throws DatabaseHandlingException {
            if (!request.getCommandArguments().isEmpty() || request.getObjectArgument() != null) throw new NoSuchCommandException();
        Collection<Ticket> collection = collectionManager.getCollection();


        if (collection == null || collection.isEmpty()) {
            return new ServerResponse("Коллекция еще не инициализирована", ResponseCode.ERROR);
        }


            //ResponseOutputer.appendln("\nAll elements of collection: \n" +  collectionManager.toString());
            //logger.debug("\nAll elements of collection: \n" +  collectionManager.toString());
            //logger.debug("\nAll elements of collection: \n" + databaseCollectionHandler.getCollection().toString());



        return new ServerResponse("", ResponseCode.OK, collection);
    }
}
