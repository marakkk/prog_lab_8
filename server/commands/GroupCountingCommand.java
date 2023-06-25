package server.commands;

import common.exceptions.NoSuchCommandException;
import common.models.Ticket;
import common.util.ClientRequest;
import common.util.ResponseCode;
import common.util.ServerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.utility.CollectionManager;

import java.util.ResourceBundle;

/**
 * Command 'group_counting'. Saves the collection to a file.
 */
public class GroupCountingCommand extends AbstractCommand{
    CollectionManager collectionManager;
    Ticket ticket;
    private Logger logger = LoggerFactory.getLogger("Group counting command");

    public GroupCountingCommand(CollectionManager collectionManager){
        super("group_counting","group the elements of the collection by the value of the CreationDate field, output the number of elements in each group");
        this.collectionManager = collectionManager;
    }

    /**
     * Execute of 'group_counting' command.
     */

    @Override
    public ServerResponse execute(ClientRequest request) throws NoSuchCommandException {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("Response", request.getLocale());

        if (!request.getCommandArguments().isEmpty() || request.getObjectArgument() != null) throw new NoSuchCommandException();
            collectionManager.groupCountingByCrDate();
            //logger.debug(String.valueOf(collectionManager.groupCountingByCrDate()));

        return new ServerResponse(resourceBundle.getString("groupCounting"), ResponseCode.OK);
    }

}
