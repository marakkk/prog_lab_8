package server.commands;

import common.exceptions.NoSuchCommandException;
import common.util.ClientRequest;
import common.util.ResponseCode;
import common.util.ServerResponse;
import common.util.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.utility.CollectionManager;
import server.utility.ResponseOutputer;

import java.util.ResourceBundle;

/**
 * Command 'head'. Saves the collection to a file.
 */
public class HeadCommand extends AbstractCommand {
    CollectionManager collectionManager;
    private Logger logger = LoggerFactory.getLogger("Head command");

    public HeadCommand(CollectionManager collectionManager) {
        super("head", " show first element of collection");
        this.collectionManager = collectionManager;
    }

    /**
     * Execute of 'head' command.
     */

    @Override
    public ServerResponse execute(ClientRequest request) throws NoSuchCommandException {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("Response", request.getLocale());

        if (!request.getCommandArguments().isEmpty() || request.getObjectArgument() != null) {
            throw new NoSuchCommandException();
        }
        if (collectionManager.getCollection().isEmpty()) {
            ResponseOutputer.appendln("Collection is empty");
            logger.debug("Collection is empty");
        }else {
            ResponseOutputer.appendln(collectionManager.getFirst());
            logger.debug(String.valueOf(collectionManager.getFirst()));
        }
        return new ServerResponse(resourceBundle.getString("objAddMin"), ResponseCode.OK);

    }
}
