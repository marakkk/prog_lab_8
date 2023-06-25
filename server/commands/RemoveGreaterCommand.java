package server.commands;

import client.commandLine.BlankConsole;
import common.util.*;
import common.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.utility.*;

/**
 * This is command 'remove_greater_key'. Remove elements which have key that is more than given.
 */
public class RemoveGreaterCommand extends AbstractCommand implements ICommand {
    CollectionManager collectionManager;
    BlankConsole console;
    private Logger logger = LoggerFactory.getLogger("Remove greater command");

    public RemoveGreaterCommand(CollectionManager collectionManager){
        super("remove_greater", "remove all items from the collection that exceed the specified");
        this.collectionManager = collectionManager;
    }

    /**
     * Execute of 'remove_greater' command.
     */

    @Override
    public ServerResponse execute(ClientRequest request) {
        try {
            if (request.getCommandArguments().isEmpty() || request.getObjectArgument() != null) {
                throw new NoSuchCommandException();
            }
            if (collectionManager.collectionSize() == 0) {
                console.println("Cannot remove object");
            }
            /**TicketRaw ticketRaw = (TicketRaw) request.getObjectArgument();
           var ticket = new Ticket(
                   0,
                    ticketRaw.getName(),
                    ticketRaw.getCoordinates(),
                    ticketRaw.getCreationDate(),
                    ticketRaw.getPrice(),
                    ticketRaw.getDiscount(),
                    ticketRaw.getRefundable(),
                    ticketRaw.getType(),
                    ticketRaw.getPerson(),
                    request.getUser().getUsername()
            );
            Ticket ticketColl = collectionManager.getByValue(ticket);
            if (ticketColl == null) {
                console.println("Cannot remove object");
            }
            collectionManager.removeGreater(ticketColl);
            ResponseOutputer.appendln("Greater of ticket is removed");
            logger.debug("Greater of " + ticketColl + " is deleted");**/
        } catch (NoSuchCommandException e) {
            console.printError(e.getMessage());
        }
        return new ServerResponse("", ResponseCode.OK);
    }
}
