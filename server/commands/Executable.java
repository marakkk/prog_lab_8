package server.commands;

import common.exceptions.*;
import common.util.ClientRequest;
import common.util.ServerResponse;

import java.io.IOException;

public interface Executable {
    ServerResponse execute(ClientRequest request) throws CommandUsageException, ExitObliged, IllegalArgumentException, WrongArgumentException, DatabaseHandlingException, IOException, WrongAmountOfElementsException;

}
