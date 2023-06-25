package server.commands;

import common.exceptions.DatabaseHandlingException;
import common.exceptions.WrongAmountOfElementsException;
import common.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.utility.*;

import java.util.ResourceBundle;

/**
 * Command 'add_if_min'. Saves the collection to a file.
 */
public class AddIfMinCommand extends AbstractCommand {
    CollectionManager collectionManager;
    private DatabaseCollectionHandler databaseCollectionHandler;
    private Logger logger = LoggerFactory.getLogger("Add if min element command");

    public AddIfMinCommand(CollectionManager collectionManager, DatabaseCollectionHandler databaseCollectionHandler
    ) {
        super("add_if_min", "add a new item to the collection if its value is less than that of the smallest item in this collection");
        this.collectionManager = collectionManager;
        this.databaseCollectionHandler = databaseCollectionHandler;
    }

    /**
     * Execute of 'add_if_min' command.
     */
    @Override
    public ServerResponse execute(ClientRequest request) throws WrongAmountOfElementsException, DatabaseHandlingException {
            if (!request.getCommandArguments().isEmpty()|| request.getObjectArgument() == null) throw new WrongAmountOfElementsException();
            //TicketRaw ticketRaw = (TicketRaw) request.getObjectArgument();
            //var ticket = databaseCollectionHandler.insertTicket(ticketRaw, request.getUser());
            ResourceBundle resourceBundle = ResourceBundle.getBundle("Response", request.getLocale());

            //if (collectionManager.collectionSize() == 0 || ticket.compareTo(collectionManager.getFirst()) < 0) {
                //collectionManager.addToCollection(ticket);
                ResponseOutputer.appendln("Ticket is added successfully");
                //logger.debug("Ticket " + ticket + " is added");
           // } else
                ResponseOutputer.appenderror("The value of the ticket is greater than the value of the smallest of the tickets");
        return new ServerResponse(resourceBundle.getString("objAddMin"), ResponseCode.OK);

    }
}
