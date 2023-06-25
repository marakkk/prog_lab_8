package client.commandLine;

import common.exceptions.CommandUsageException;
import common.exceptions.ExitObliged;

public interface Executable {
    void execute(String args) throws CommandUsageException, ExitObliged, IllegalArgumentException;

}
