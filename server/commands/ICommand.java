package server.commands;

import common.exceptions.DatabaseHandlingException;
import common.exceptions.WrongAmountOfElementsException;
import common.exceptions.WrongArgumentException;
import common.util.ClientRequest;
import common.util.ServerResponse;
import common.util.User;

import java.io.IOException;

/**
 * Interface for all commands.
 */
public interface ICommand{

    String getName();
    ServerResponse execute(ClientRequest request) throws WrongArgumentException, DatabaseHandlingException, WrongAmountOfElementsException, IOException;

}