package server.commands;

import client.commandLine.BlankConsole;
import common.exceptions.NoSuchCommandException;
import common.util.ClientRequest;
import common.util.ResponseCode;
import common.util.ServerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.utility.*;

import java.util.ResourceBundle;


/**
 * Command 'execute_script'. Saves the collection to a file.
 */
public class ExecuteScriptCommand extends AbstractCommand{
    private Logger logger = LoggerFactory.getLogger("Execute script command");

    public ExecuteScriptCommand( ){
        super("execute_script", "read and execute the script from the specified file");
    }

    /**
     * Execute of 'execute_script' command.
     */
    BlankConsole console;

    @Override
    public ServerResponse execute(ClientRequest request) throws NoSuchCommandException {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("Response", request.getLocale());

        if (request.getCommandArguments().isEmpty() || request.getObjectArgument() != null) {
            console.println("Used: '" + getName() + "'");
            throw new NoSuchCommandException();
        }
        ResponseOutputer.appendln("Reading the given script ...");
        //ResponseOutputer.appendln(userIO.startReadScript(request.getCommandArguments()));

        logger.debug("Reading the given script ...");
        //logger.debug((String) userIO.startReadScript(request.getCommandArguments()));
        return new ServerResponse(request.getCommandArguments(), ResponseCode.EXECUTE_SCRIPT);

    }
}

