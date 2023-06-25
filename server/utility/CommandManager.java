package server.utility;

import server.commands.*;
import server.commands.AbstractCommand;
import server.commands.AddElementCommand;
import server.commands.AddIfMinCommand;

import java.util.HashMap;
import java.util.Map;

/**
 * Operates the commands.
 */
public class CommandManager {
    private final Map<String, AbstractCommand> commands = new HashMap<>();

    public CommandManager(CollectionManager collectionManager, DatabaseCollectionHandler databaseCollectionHandler, DatabaseUserManager databaseUserManager){

        AddElementCommand addElementCommand = new AddElementCommand(collectionManager, databaseCollectionHandler);
        commands.put(addElementCommand.getName(), addElementCommand);
        AddIfMinCommand addIfMinCommand = new AddIfMinCommand(collectionManager, databaseCollectionHandler);
        commands.put(addIfMinCommand.getName(), addIfMinCommand);
        AverageOfDiscountCommand averageOfDiscountCommand = new AverageOfDiscountCommand(collectionManager);
        commands.put(averageOfDiscountCommand.getName(), averageOfDiscountCommand);
        ClearCommand clearCommand = new ClearCommand(collectionManager, databaseCollectionHandler);
        commands.put(clearCommand.getName(), clearCommand);
        ExecuteScriptCommand executeScriptCommand = new ExecuteScriptCommand();
        commands.put(executeScriptCommand.getName(), executeScriptCommand);
        ExitCommand exitCommand = new ExitCommand(collectionManager);
        commands.put(exitCommand.getName(), exitCommand);
        GroupCountingCommand groupCountingCommand = new GroupCountingCommand(collectionManager);
        commands.put(groupCountingCommand.getName(), groupCountingCommand);
        HeadCommand headCommand = new HeadCommand(collectionManager);
        commands.put(headCommand.getName(), headCommand);
        HelpCommand helpCommand = new HelpCommand();
        commands.put(helpCommand.getName(), helpCommand);
        InfoCommand infoCommand = new InfoCommand(collectionManager);
        commands.put(infoCommand.getName(), infoCommand);
        PrintUniquePersonCommand printUniquePersonCommand = new PrintUniquePersonCommand(collectionManager);
        commands.put(printUniquePersonCommand.getName(), printUniquePersonCommand);
        RegisterCommand registerCommand = new RegisterCommand(databaseUserManager);
        commands.put(registerCommand.getName(), registerCommand);
        RemoveByIdCommand removeByIdCommand = new RemoveByIdCommand(collectionManager, databaseCollectionHandler);
        commands.put(removeByIdCommand.getName(), removeByIdCommand);
        RemoveGreaterCommand removeGreaterCommand = new RemoveGreaterCommand(collectionManager);
        commands.put(removeGreaterCommand.getName(), removeGreaterCommand);
        ShowCommand showCommand = new ShowCommand(collectionManager, databaseCollectionHandler);
        commands.put(showCommand.getName(), showCommand);
        UpdateCommand updateCommand = new UpdateCommand(collectionManager, databaseCollectionHandler);
        commands.put(updateCommand.getName(), updateCommand);
    }

    /**
     * @return list of commands.
     */
    public Map<String, AbstractCommand> getCommands() {
        return commands;
    }


    @Override
    public String toString() {
        return "CommandManager for working with commands";
    }
}
