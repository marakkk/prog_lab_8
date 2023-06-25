package server.commands;

import common.exceptions.DatabaseHandlingException;
import common.exceptions.WrongArgumentException;
import common.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.utility.CollectionManager;
import server.utility.DatabaseCollectionHandler;
import server.utility.ResponseOutputer;

import java.util.ResourceBundle;

/**
 * Command 'add_element'. Saves the collection to a file.
 */
public class AddElementCommand extends AbstractCommand{
    private CollectionManager collectionManager;
    private DatabaseCollectionHandler databaseCollectionHandler;
    private Logger logger = LoggerFactory.getLogger("Add element command");


    public AddElementCommand(CollectionManager collectionManager, DatabaseCollectionHandler databaseCollectionHandler){
        super("add","add a new item to the collection");
        this.collectionManager = collectionManager;
        this.databaseCollectionHandler = databaseCollectionHandler;

    }

    /**
     * Execute of 'add_element' command.
     *
     * @return
     */
    @Override
    public ServerResponse execute( ClientRequest request ) throws WrongArgumentException {
            if (!request.getCommandArguments().isBlank()) throw new WrongArgumentException();
            ResourceBundle resourceBundle = ResourceBundle.getBundle("Response", request.getLocale());

            //TicketRaw ticketRaw = (TicketRaw) request.getObjectArgument();
            //collectionManager.addToCollection(databaseCollectionHandler.insertTicket(ticketRaw, request.getUser())
            //);
            ResponseOutputer.appendln("Ticket is created");
            //logger.debug("Ticket" + ticketRaw + " is created");
            return new ServerResponse( resourceBundle.getString("objAddOl"), ResponseCode.OK);


    }
}
