package server.commands;

import common.exceptions.NoSuchCommandException;
import common.exceptions.WrongAmountOfElementsException;
import common.util.ClientRequest;
import common.util.ResponseCode;
import common.util.ServerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.utility.CollectionManager;
import server.utility.ResponseOutputer;

import java.io.IOException;


/**
 * Command 'exit'. Saves the collection to a file.
 */
public class ExitCommand extends AbstractCommand{
    private CollectionManager collectionManager;
    private Logger logger = LoggerFactory.getLogger("Exit command");


    public ExitCommand(CollectionManager collectionManager){
        super("exit", "end the program (without saving to a file)");
        this.collectionManager = collectionManager;

    }

    /**
     * Execute of 'exit' command.
     */
    @Override
    public ServerResponse execute(ClientRequest request) throws NoSuchCommandException, IOException, WrongAmountOfElementsException {
            if (!request.getCommandArguments().isEmpty() || request.getObjectArgument() != null) throw new WrongAmountOfElementsException();
            ResponseOutputer.appendln(collectionManager.exit());
        return new ServerResponse("", ResponseCode.EXIT);
    }


}
